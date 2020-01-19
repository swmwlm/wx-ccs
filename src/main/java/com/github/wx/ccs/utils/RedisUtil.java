package com.github.wx.ccs.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public RedisUtil set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        return this;
    }

    public RedisUtil remove(String key) {
        redisTemplate.delete(key);
        return this;
    }

    public RedisUtil setex(String key, String value, Long milliseconds) {
        redisTemplate.opsForValue().set(key, value, milliseconds, TimeUnit.MILLISECONDS);
        return this;
    }

    public RedisUtil setex(String key, String value, Integer seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
        return this;
    }

}
