package com.around.aroundcore.web.services;

import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CacheReloadService {
    CacheManager cacheManager;

    public void evictAllCaches() {
        cacheManager.getCacheNames()
                .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

    @Scheduled(fixedRate = 900000)
    public void evictAllCachesAtIntervals() {
        evictAllCaches();
    }
}
