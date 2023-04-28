package xin.wanyun.cache.service;

import java.time.Duration;
import java.util.List;

public interface CacheService {

    /**
     * 设置key缓存
     */
    public void setKey(String key, Object obj);

    /**
     * 设置带有过期秒的key
     */
    public void setKey(String key, Object obj, int expired);

    /**
     * 设置带有过期的key
     */
    public void setKey(String key, Object clas, Duration duration);

    /**
     * 获取key对象
     */
    public <T> T getKey(String key, T clas);

    /**
     * 获取多个Key
     */
    public <T> T getKeys(String ...keys);

    /**
     * 根据List key取值
     */
    public <T> T getKeys(List<String> keys);

    /**
     * 判断Key存在性
     */
    public boolean hasKey(String key);

    /**
     * 删除Key
     */
    public void delKey(String key);

    public String genderKey(String key);

}
