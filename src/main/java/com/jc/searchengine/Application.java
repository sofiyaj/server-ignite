package com.jc.searchengine;

import com.jc.searchengine.po.Person;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

import java.util.Set;

/**
 * @Author: wangjie
 * @Description:
 * @Date: Created in 10:20 2018/3/23
 */
public class Application {

    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        CacheConfiguration cacheCfg = new CacheConfiguration();
        cacheCfg.setName("serverCache");
        cacheCfg.setCacheMode(CacheMode.PARTITIONED);

        IgniteCache<Long,Person> cache = ignite.getOrCreateCache(cacheCfg);

        ClusterGroup clusterGroup = ignite.cluster().forServers();

        cache.put(1L,new Person(1,"Sofiya",2));
        cache.put(2L,new Person(1,"Sofiya",2));
        cache.put(3L,new Person(1,"Sofiya",666666666));
        }
    }

