package com.around.aroundcore.core.services;

import com.around.aroundcore.database.models.*;
import com.around.aroundcore.database.models.chunk.GameChunk;
import com.around.aroundcore.database.models.event.Category;
import com.around.aroundcore.database.models.event.EventProvider;
import com.around.aroundcore.database.models.event.MapEvent;
import com.around.aroundcore.database.models.round.Round;
import com.around.aroundcore.database.repositories.event.CategoryRepository;
import com.around.aroundcore.database.repositories.GameChunkRepository;
import com.around.aroundcore.database.services.CityService;
import com.around.aroundcore.database.services.event.EventProviderService;
import com.around.aroundcore.database.services.round.RoundService;
import com.around.aroundcore.web.dtos.ChunkDTO;
import com.around.aroundcore.web.dtos.coords.Location;
import com.around.aroundcore.web.dtos.events.timepad.TimepadEvent;
import com.around.aroundcore.web.dtos.events.timepad.TimepadEvents;
import com.around.aroundcore.web.dtos.events.timepad.TimepadLocation;
import com.around.aroundcore.core.enums.CityEnum;
import com.around.aroundcore.core.enums.EventProvidersEnum;
import com.around.aroundcore.core.exceptions.api.CoordsNotFoundException;
import com.around.aroundcore.core.exceptions.api.EventsNotFoundException;
import com.around.aroundcore.core.exceptions.api.LocationNullException;
import com.around.aroundcore.core.exceptions.api.entity.EventProviderNullException;
import com.around.aroundcore.web.mappers.event.CategoryMapper;
import com.around.aroundcore.web.mappers.chunk.GameChunkMapper;
import com.around.aroundcore.web.mappers.LocationMapper;
import com.around.aroundcore.core.services.apis.coords.CoordsAPI;
import com.around.aroundcore.core.services.apis.events.TimepadAPIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class MapEventParsingService {
    private final CoordsAPI coordsAPI;
    private final TimepadAPIService timepadAPIService;
    private final CategoryRepository categoryRepository;
    private final GameChunkRepository gameChunkRepository;
    private final RoundService roundService;
    private final CityService cityService;
    private final EventProviderService eventProviderService;
    private final H3ChunkService h3ChunkService;
    private final CategoryMapper categoryMapper;
    private final GameChunkMapper gameChunkMapper;
    private final LocationMapper locationMapper;

    @Value("${around.mapevents.parsing.limit}")
    private Integer limit;
    @Value("${around.mapevents.parsing.radius}")
    private Integer radius;

    @Value("${around.mapevents.parsing.city}")
    private String cityName;

    public List<MapEvent> parseEvents() throws EventsNotFoundException{
        log.debug(cityName);
        CityEnum cityEnum = CityEnum.valueOf(cityName);
        City city = cityService.findById(cityEnum.getId());
        Round currentRound = roundService.getCurrentRound();
        TimepadEvents subEvents = timepadAPIService.getEventsForCity(limit,cityEnum.getName());
        List<TimepadEvent> events = subEvents.getValues();
        List<MapEvent> mapEvents = new ArrayList<>();
        for(TimepadEvent event : events){
            try{
                MapEvent mapEvent = new MapEvent();
                mapEvent.setName(event.getName());
                mapEvent.setStarts(event.getStarts_at().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                if(event.getEnds_at()!=null){
                    mapEvent.setEnds(event.getEnds_at().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                }
                mapEvent.setUrl(event.getUrl());
                List<Category> categories = event.getCategories().stream().map(categoryMapper).toList();
                List<Category> categoriesList = categories.stream().map(category -> {
                    if(categoryRepository.existsById(category.getId())){
                        return categoryRepository.findById(category.getId()).orElse(null);
                    }else{
                        return categoryRepository.saveAndFlush(category);
                    }
                }).toList();
                mapEvent.setCategories(categoriesList);
                log.info(mapEvent.getCategories().toString());

                EventProvider eventProvider = eventProviderService.findById(EventProvidersEnum.TIMEPAD.getId());
                mapEvent.setProvider(eventProvider);

                List<Double> coords= getCoords(event.getLocation());
                List<ChunkDTO> chunkDTOS = h3ChunkService.getChunkByLatLon(coords.get(0),coords.get(1),radius);
                List<GameChunk> chunks = chunkDTOS.stream().map(gameChunkMapper).toList();
                List<GameChunk> chunkList = chunks.stream().map(chunk -> {
                    if(Boolean.TRUE.equals(gameChunkRepository.existsByIdAndRoundId(chunk.getId(), currentRound.getId()))){
                        return gameChunkRepository.findByIdAndRoundId(chunk.getId(),currentRound.getId()).orElse(null);
                    }else{
                        chunk.setCity(city);
                        chunk.setRound(currentRound);
                        return gameChunkRepository.saveAndFlush(chunk);
                    }
                }).toList();
                mapEvent.setChunks(chunkList);
                mapEvents.add(mapEvent);
            }catch (EventProviderNullException | CoordsNotFoundException | EventsNotFoundException |
                    LocationNullException e){
                log.error("Error during parsing events :{}",e.getClass().getName());
            }
        }
        return mapEvents;
    }
    private List<Double> getCoords(TimepadLocation timepadLocation){
        List<Double> coords;
        if(timepadLocation==null){
            throw new LocationNullException();
        }
        if(timepadLocation.getCoordinates()!=null){
            coords = timepadLocation.getCoordinates();
        }else{
            Location location = locationMapper.apply(timepadLocation);
            coords = List.of(coordsAPI.getCoordsForLocation(location));
        }
        return coords;
    }
}
