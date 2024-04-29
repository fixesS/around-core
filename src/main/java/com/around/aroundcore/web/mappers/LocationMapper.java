package com.around.aroundcore.web.mappers;

import com.around.aroundcore.web.dtos.coords.Location;
import com.around.aroundcore.web.dtos.events.timepad.TimepadLocation;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class LocationMapper implements Function<TimepadLocation, Location> {
    @Override
    public Location apply(TimepadLocation timepadLocation) {
        return Location.builder()
                .country(timepadLocation.getCountry())
                .city(timepadLocation.getCity())
                .address(timepadLocation.getAddress())
                .build();
    }
}
