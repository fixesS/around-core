package com.around.aroundcore.web.services;

import com.around.aroundcore.database.models.MapEvent;
import com.around.aroundcore.database.services.MapEventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MapEventCreationService {
    private final MapEventParsingService mapEventParsingService;
    private final MapEventService mapEventService;

    @Transactional
    public void create(){
        List<MapEvent> events = mapEventParsingService.parseEvents();
        log.info("Creating {} events",events.size());
        events.forEach(mapEventService::createAndFlush);
        //mapEventService.createAllAndFlush(events);
    }
}
