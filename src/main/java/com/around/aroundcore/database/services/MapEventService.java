package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.MapEvent;
import com.around.aroundcore.database.repositories.MapEventRepository;
import com.around.aroundcore.web.exceptions.entity.MapEventNullException;
import com.around.aroundcore.web.services.MapEventParsingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class MapEventService {
    private final MapEventRepository mapEventRepository;
    private final MapEventParsingService mapEventParsingService;
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
    @Scheduled(fixedRate = 900000)
    public void makeEndedEventsNotVerified(){
        List<MapEvent> events = findAll();
        events.forEach(event -> {
            if(event.getEnds()!=null &&
                    event.getEnds().isAfter(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())){
                event.setVerified(false);
            }
        });
        mapEventRepository.saveAllAndFlush(events);
    }
    public List<MapEvent> findAll(){
        return mapEventRepository.findAll();
    }
    @Cacheable(value = "verifiedEventsByCity", key = "#cityId")
    public List<MapEvent> findAllVerifiedInCity(Integer cityId) {
        return mapEventRepository.findAllByVerifiedAndCityId(true, cityId);
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
