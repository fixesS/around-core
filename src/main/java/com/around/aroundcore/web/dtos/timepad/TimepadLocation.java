package com.around.aroundcore.web.dtos.timepad;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TimepadLocation {
    private String country;
    private String city;
    private String address;
    private List<Double> coordinates;
    @Override
    public String toString(){
        return String.format("%s, %s, %s",country,city,address);
    }

}
