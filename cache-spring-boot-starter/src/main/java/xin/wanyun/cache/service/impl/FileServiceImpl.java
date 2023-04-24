package xin.wanyun.cache.service.impl;

import xin.wanyun.cache.service.CacheService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service("file")
public class FileServiceImpl implements CacheService {
    @Override
    public <T> T setKey(String key, T clas) {
        return null;
    }

    @Override
    public <T> T setKey(String key, T clas, int expired) {
        return null;
    }

    @Override
    public <T> T setKey(String key, T clas, Duration duration) {
        return null;
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
}
