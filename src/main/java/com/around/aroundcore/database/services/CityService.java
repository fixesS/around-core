package com.around.aroundcore.database.services;

import com.around.aroundcore.database.models.City;
import com.around.aroundcore.database.repositories.CityRepository;
import com.around.aroundcore.web.exceptions.api.entity.CityNullException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CityService {
    private final CityRepository cityRepository;
    @Cacheable(value = "findCityById", key = "#id")
    public City findById(Integer id) throws CityNullException {
        return cityRepository.findById(id).orElseThrow(CityNullException::new);
    }
    @Cacheable(value = "checkCity", key = "#cityId")
    public void checkById(Integer cityId) throws CityNullException {
        if(!cityRepository.existsById(cityId)){
            throw new CityNullException();
        }
    }
    public List<City> findAll(){
        return cityRepository.findAll();
    }
}
