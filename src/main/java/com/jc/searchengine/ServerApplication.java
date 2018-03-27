package com.jc.searchengine;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wangjie
 * @Description:
 * @Date: Created in 10:13 2018/3/27
 */

@SpringBootApplication
@RestController
public class ServerApplication {

    //cache name
    private static final String CACHE_NAME = "serverCache";

    private static Ignite ignite = Ignition.start("example-cache.xml");

    public static void main(String[] args) throws InterruptedException {

        SpringApplication.run(ServerApplication.class,args);

    }

    @RequestMapping(value = "/testIgnite",method = RequestMethod.GET)
    public String testIgnite(Integer key,String value) throws InterruptedException{
        ignite.active(true);
        System.out.println("*******insert data begins*********");

        try(IgniteCache<Integer, String> cache = ignite.getOrCreateCache(CACHE_NAME)){
            cache.put(key,value);
            Thread.sleep(2000);
        }
        return "*******insert data succeed*********";
    }
}
