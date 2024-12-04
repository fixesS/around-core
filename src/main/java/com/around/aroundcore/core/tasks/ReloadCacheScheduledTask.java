package com.around.aroundcore.core.tasks;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@AllArgsConstructor
public class ReloadCacheScheduledTask {
    private final CacheManager cacheManager;

    public void evictAllCaches() {
        try{
            cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Scheduled(fixedRate = 900000)
    public void evictAllCachesAtIntervals() {
        log.debug("Caches have been evicted");
        evictAllCaches();
    }
}
