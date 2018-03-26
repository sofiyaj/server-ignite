package com.jc.searchengine;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteSystemProperties;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteBiPredicate;

import javax.cache.Cache;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryUpdatedListener;

/**
 * @Author: wangjie
 * @Description:
 * @Date: Created in 18:46 2018/3/23
 */
public class CacheContinuousQueryServer {

    public static void main(String[] args) throws InterruptedException{

        System.out.println("************start insert data*************");

       /* IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setPeerClassLoadingEnabled(true);*/
       //igniteConfiguration
             Ignite ignite = Ignition.start("example-cache.xml");
             CacheConfiguration cacheCfg = new CacheConfiguration();
             cacheCfg.setName("serverCache");
             cacheCfg.setCacheMode(CacheMode.PARTITIONED);
             IgniteCache<Integer,String> cache = ignite.getOrCreateCache(cacheCfg);



            int keyCnt = 20;
            for (int i = 0; i < keyCnt; i++) {
                cache.put(i, Integer.toString(i));
            }


        System.out.println("************insert data is ok !************");

        Thread.sleep(8000);

        System.out.println("wait break!");

        for (int i = keyCnt; i < keyCnt + 10; i++) {
                cache.put(i, Integer.toString(i));
                System.out.println("insert + " + Integer.toString(i));
            }


        ContinuousQuery<Integer, String> qry = new ContinuousQuery<>();
        qry.setInitialQuery(new ScanQuery<>(new IgniteBiPredicate<Integer, String>() {
            @Override public boolean apply(Integer key, String val) {
                return key > 10;
            }
        }));

        System.out.println("init query is ok!");

        qry.setLocalListener(new CacheEntryUpdatedListener<Integer, String>() {
            @Override public void onUpdated(Iterable<CacheEntryEvent<? extends Integer, ? extends String>> evts) {
                for (CacheEntryEvent<? extends Integer, ? extends String> e : evts) {
                    System.out.println("Updated entry [key=" + e.getKey() + ", val=" + e.getValue() + ']');
                }
            }
        });
        System.out.println("local lisetner created  ****************");

        try (QueryCursor<Cache.Entry<Integer, String>> cur = cache.query(qry)) {
            // Iterate through existing data.
            for (Cache.Entry<Integer, String> e : cur) {
                System.out.println("Queried existing entry [key=" + e.getKey() + ", val=" + e.getValue() + ']');
            }
        }
        finally {
            ignite.destroyCache("serverCache");
        }

        }



    }

