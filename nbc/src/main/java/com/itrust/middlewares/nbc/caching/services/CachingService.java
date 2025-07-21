package com.itrust.middlewares.nbc.caching.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CachingService {

    @Autowired
    CacheManager cacheManager;

    @Cacheable("auths") // Specify the cache name
    public String getCachedData(String key) {
        if(!Objects.isNull(cacheManager.getCache("auths"))){
            if(!Objects.isNull(cacheManager.getCache("auths").get(key))){
                Object cached = cacheManager.getCache("auths").get(key).get();
                ObjectMapper mapper = new ObjectMapper();
                return mapper.convertValue(cached, String.class);
            }
            return "";
        }
        return "";
    }

    @CachePut("auths") // Specify the cache name
    public void updateCachedData(String key, String newData) {
        Objects.requireNonNull(cacheManager.getCache("auths")).put(key, newData);
    }
}
