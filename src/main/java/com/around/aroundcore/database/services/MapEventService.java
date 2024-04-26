package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.MapEvent;
import com.around.aroundcore.database.repositories.CategoryRepository;
import com.around.aroundcore.database.repositories.MapEventRepository;
import com.around.aroundcore.web.exceptions.entity.MapEventNullException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class MapEventService {
    private final MapEventRepository mapEventRepository;

    public void create(MapEvent event){
        mapEventRepository.save(event);
    }
    public void createAndFlush(MapEvent event){
        mapEventRepository.saveAndFlush(event);
    }
    public void createAll(List<MapEvent> events){
        mapEventRepository.saveAll(events);
    }
    public void createAllAndFlush(List<MapEvent> events){
        mapEventRepository.saveAllAndFlush(events);
    }
    public void update(MapEvent event){
        mapEventRepository.save(event);
    }
    public MapEvent findById(Integer id){
        return mapEventRepository.findById(id).orElseThrow(MapEventNullException::new);
    }
    public List<MapEvent> findAll(){
        return mapEventRepository.findAll();
    }
    public List<MapEvent> findAllVerified(){
        return mapEventRepository.findAllByVerified(true);
    }
    public boolean existByUrl(String url){
        return mapEventRepository.existsByUrl(url);
    }
}
