package com.around.aroundcore.database.services.event;

import com.around.aroundcore.database.models.event.MapEvent;
import com.around.aroundcore.database.repositories.event.MapEventRepository;
import com.around.aroundcore.core.exceptions.api.entity.MapEventNullException;
import com.around.aroundcore.core.services.MapEventParsingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MapEventService {
    private final MapEventRepository mapEventRepository;
    private final MapEventParsingService mapEventParsingService;
    @Value("${around.time.locale}")
    private String timeLocale;
    private void create(MapEvent event){
        mapEventRepository.save(event);
    }
    private void createAndFlush(MapEvent event){
        mapEventRepository.saveAndFlush(event);
    }
    private void createAll(List<MapEvent> events){
        mapEventRepository.saveAll(events);
    }
    private void createAllAndFlush(List<MapEvent> events){
        mapEventRepository.saveAllAndFlush(events);
    }
    private void update(MapEvent event){
        mapEventRepository.save(event);
    }
    public MapEvent findById(Integer id){
        return mapEventRepository.findById(id).orElseThrow(MapEventNullException::new);
    }
    @CacheEvict(value = "verifiedAndActiveEventsByCity")
    public void disableEndedEvents(){
        List<MapEvent> events = findAll();
        events.forEach(event -> {
            if(event.getEnds()!=null &&
                    event.getEnds().isBefore(new Date().toInstant().atZone(ZoneId.of(timeLocale)).toLocalDateTime())){
                event.setActive(false);
            }
        });
        mapEventRepository.saveAllAndFlush(events);
    }
    public List<MapEvent> findAll(){
        return mapEventRepository.findAll();
    }
    @Cacheable(value = "verifiedAndActiveEventsByCity", key = "#cityId")
    public List<MapEvent> findAllVerifiedInCity(Integer cityId) {
        return mapEventRepository.findAllByVerifiedAndActiveAndCityId(true,true, cityId);
    }
    public List<MapEvent> findVerifiedActiveByChunksAndNotVisitedByUser(List<String> chunkIds,Integer userId) {
        return mapEventRepository.findVerifiedActiveByChunksAndNotVisitedByUser(chunkIds, userId);
    }
    public boolean existByUrl(String url){
        return mapEventRepository.existsByUrl(url);
    }

    @Transactional
    public void createFromEventAPI(){
        List<MapEvent> events = mapEventParsingService.parseEvents();
        log.debug("Creating {} events",events.size());
        createAllAndFlush(events);
    }
}
