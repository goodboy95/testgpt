package com.seekerhut.utils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.seekerhut.model.config.RedisConfig;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolAbstract;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.params.SetParams;

@EnableConfigurationProperties(RedisConfig.class)
public class JedisHelper {
    private static JedisPoolAbstract jedisPool;
    private static JedisPoolConfig jedisPoolConfig;
    private static String prefix;

    public static void init(RedisConfig redisConfig) {
        // jedis pool configuration
        jedisPoolConfig = new JedisPoolConfig();
        var jedisConfigData = redisConfig.getJedis().getPool();
        int maxIdle = jedisConfigData.getMaxIdle();
        int minIdle = jedisConfigData.getMinIdle();
        int maxTotal = jedisConfigData.getMaxActive();
        int maxWaitTime = jedisConfigData.getMaxWait();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitTime);

        // redis common-data configuration
        String password = redisConfig.getPassword();
        int database = redisConfig.getDatabase();
        int timeout = redisConfig.getTimeout();
        prefix = redisConfig.getPrefix() + ":";

        // redis single-node or cluster(with sentinels) configuration
        var sentinelData = redisConfig.getSentinel();
        if (sentinelData == null) {
            String host = redisConfig.getHost();
            int port = redisConfig.getPort();
            jedisPool = password.equals("")
                ? new JedisPool(jedisPoolConfig, host, port, timeout, null, database)
                : new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
        } else {
            var masterName = redisConfig.getSentinel().getMaster();
            var sentinelNodes = redisConfig.getSentinel().getNodes();
            jedisPool = password.equals("")
                ? new JedisSentinelPool(masterName, sentinelNodes, jedisPoolConfig, timeout, password, database)
                : new JedisSentinelPool(masterName, sentinelNodes, jedisPoolConfig, timeout, password, database);
        }
    }
    //#region ordinary data
    public static void set(String key, Object val) {
        set(key, val, 0);
    }

    public static void set(String key, Object val, int seconds) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        SetParams params = SetParams.setParams().ex(seconds);
        jedis.set(key, val.toString(), params);
        jedis.close();
    }

    public static void setnx(String key, Object val) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        jedis.setnx(key, val.toString());
        jedis.close();
    }

    public static <T> void bulk_set(List<String> keys, List<T> value, int seconds) {
        if (keys.size() != value.size()) {
            // throw an error message
            return;
        }
        SetParams params = SetParams.setParams().ex(seconds);
        Jedis jedis = jedisPool.getResource();
        var ppl = jedis.pipelined();
        for (int i = 0; i < keys.size(); i++) {
            ppl.set(prefix + keys.get(i), value.get(i).toString(), params);
        }
        ppl.sync();
        jedis.close();
    }

    public static void expire(String key, int seconds) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        jedis.expire(key, seconds);
        jedis.close();
    }

    public static void bulk_expire(List<String> keys, int seconds) {
        Jedis jedis = jedisPool.getResource();
        var ppl = jedis.pipelined();
        for (int i = 0; i < keys.size(); i++) {
            ppl.expire(prefix + keys.get(i), seconds);
        }
        ppl.sync();
        jedis.close();
    }

    public static <T> T get(String key, Class<T> clazz) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result == null ? null : JSONObject.parseObject(result, clazz);
    }

    public static <T> List<T> bulk_get(List<?> keys, Class<T> clazz) {
        Jedis jedis = jedisPool.getResource();
        var ppl = jedis.pipelined();
        for (Object key : keys) {
            ppl.get(prefix + key.toString());
        }
        var rawResult = ppl.syncAndReturnAll();
        var result = rawResult.stream().map(obj -> obj == null ? null : JSONObject.parseObject(obj.toString(), clazz))
                .collect(Collectors.toList());
        jedis.close();
        return result;
    }

    public static boolean exists(String key) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        boolean result = jedis.exists(key);
        jedis.close();
        return result;
    }

    public static long incr(String key) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        long result = jedis.incr(key);
        jedis.close();
        return result;
    }

    public static long incrBy(String key, int num) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        long result = jedis.incrBy(key, num);
        jedis.close();
        return result;
    }

    public static long decr(String key) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        long result = jedis.decr(key);
        jedis.close();
        return result;
    }

    public static long decrBy(String key, int num) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        long result = jedis.decrBy(key, num);
        jedis.close();
        return result;
    }
    //#endregion

    //#region hashtable
    public static void hset(String key, Object hashKey, Object val) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        jedis.hset(key, hashKey.toString(), val.toString());
        jedis.close();
    }

    public static void hset(String key, Object hashKey, Object val, int seconds) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        jedis.hset(key, hashKey.toString(), val.toString());
        jedis.close();
    }

    public static void hmset(String key, Map<String, String> hash) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        jedis.hmset(key, hash);
        jedis.close();
    }

    public static <T> T hget(String key, Object hashKey, Class<T> clazz) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hget(key, hashKey.toString());
        jedis.close();
        return JSONObject.parseObject(result, clazz);
    }

    public static <T, U extends Serializable> List<U> hmget(String key, List<T> hashKey, Class<U> clazz) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        String[] hashKeyStr = hashKey.stream().map(k -> k.toString()).collect(Collectors.toList()).toArray(new String[0]);
        List<U> result = jedis.hmget(key, hashKeyStr).stream().map(d -> JSONObject.parseObject(d, clazz)).collect(Collectors.toList());
        jedis.close();
        return result;
    }

    public static boolean hexists(String key, String hashKey) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        boolean result = jedis.hexists(key, hashKey);
        jedis.close();
        return result;
    }
    //#endregion

    //#region list
    public static void rpush(String key, Object data) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        jedis.rpush(key, data.toString());
        jedis.close();
    }

    public static <T> List<T> lrange(String key, long begin, long end, Class<T> clazz) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        var result = jedis.lrange(key, begin, end).stream().map(d -> JSONObject.parseObject(d, clazz)).collect(Collectors.toList());
        jedis.close();
        return result;
    }
    //#endregion
    //#region set/zset
    public static boolean sadd(String key, Object data) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        var result = jedis.sadd(key, data.toString());
        jedis.close();
        return result == 1;
    }

    public static long sadd(String key, List<Object> data) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        var dataArrList = data.stream().toArray(String[] :: new);
        var result = jedis.sadd(key, dataArrList);
        jedis.close();
        return result;
    }

    public static boolean srem(String key, Object data) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        var result = jedis.srem(key, data.toString());
        jedis.close();
        return result == 1;
    }

    public static long srem(String key, List<Object> data) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        var dataArrList = data.stream().toArray(String[] :: new);
        var result = jedis.srem(key, dataArrList);
        jedis.close();
        return result;
    }

    public static Set<String> smembers(String key) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        var result = jedis.smembers(key);
        jedis.close();
        return result;
    }

    public static Set<String> bulk_smembers(List<?> keys) {
        Jedis jedis = jedisPool.getResource();
        var ppl = jedis.pipelined();
        for (Object key : keys) {
            ppl.smembers(prefix + key.toString());
        }
        var rawResult = ppl.syncAndReturnAll();
        var result = rawResult.stream().flatMap(obj -> obj == null ? new HashSet<String>().stream() : ((HashSet<String>)obj).stream())
                .collect(Collectors.toSet());
        jedis.close();
        return result;
    }

    public static long scard(String key) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        var result = jedis.scard(key);
        jedis.close();
        return result;
    }

     public static void zadd(String key, Object... data) {
         key = prefix + key;
         Jedis jedis = jedisPool.getResource();
         var scoreMemberMap = Arrays.stream(data).collect(Collectors.toMap(c -> String.valueOf(c), c -> 0.0d));
         jedis.zadd(key, scoreMemberMap);
         jedis.close();
     }

    public static Set<String> zrange(String key, int start, int end) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        var result = jedis.zrange(key, start, end);
        jedis.close();
        return result;
    }

    public static Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        var result = jedis.zrangeByScore(key, min, max, offset, count);
        jedis.close();
        return result;
    }

    public static Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        key = prefix + key;
        Jedis jedis = jedisPool.getResource();
        var result = jedis.zrangeByLex(key, min, max, offset, count);
        jedis.close();
        return result;
    }
    //#endregion
}
