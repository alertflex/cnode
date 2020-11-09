/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alertflex.redis;

/**
 *
 * @author root
 */
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@ApplicationScoped
public class PooledJedisProducer {

    private JedisPool pool = null;

    /**
     * sets up the pool.. invoked only once since bean is ApplicaitionScoped
     */
    @PostConstruct
    public void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setTestOnBorrow(true);
        config.setMaxWaitMillis(5000);
        config.setMaxTotal(15);
        String redisHost = System.getProperty("RedisHost", "no");
        String redisPort = System.getProperty("RedisPort", "6379");

        if (!redisHost.equals("no")) {
            pool = new JedisPool(config, redisHost, Integer.valueOf(redisPort), 10000);
        }
    }

    @RequestScoped
    @Produces
    @FromJedisPool
    public Jedis get() {

        if (pool != null) {
            Jedis jedis = pool.getResource();
            return jedis;
        }

        return null;
    }

    public void backToPool(@Disposes @FromJedisPool Jedis jedis) {
        if (pool != null) {
            pool.returnResource(jedis);
        }
    }

    @PreDestroy
    public void close() {
        if (pool != null) {
            pool.close();
        }
    }
}
