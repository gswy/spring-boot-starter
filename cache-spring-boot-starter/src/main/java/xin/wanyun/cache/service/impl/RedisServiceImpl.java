package xin.wanyun.cache.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import xin.wanyun.cache.config.CacheConfig;
import xin.wanyun.cache.service.CacheService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service("redis")
public class RedisServiceImpl implements CacheService {

    @Autowired
    CacheConfig cacheConfig;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Override
    public void setKey(String key, Object obj) {

    }

    @Override
    public void setKey(String key, Object obj, int expired) {

    }

    @Override
    public void setKey(String key, Object clas, Duration duration) {

    }

    @Override
    public <T> T getKey(String key, T clas) {
        return null;
    }

    @Override
    public <T> T getKeys(String... keys) {
        return null;
    }

    @Override
    public <T> T getKeys(List<String> keys) {
        return null;
    }

    @Override
    public boolean hasKey(String key) {
        return false;
    }

    @Override
    public void delKey(String key) {

    }

    @Override
    public String genderKey(String key) {
        return cacheConfig.getPrefix() + ":" + key;
    }

}
