package com.around.aroundcore.web.dtos.events.timepad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TimepadLocation {
    private String country;
    private String city;
    private String address;
    private List<Double> coordinates;

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        if(city!=null && !city.equals("") && address!=null && !address.equals("")){
            if(country!=null && !country.equals("")){
                stringBuilder.append(country).append(", ");
            }
            stringBuilder.append(city).append(", ");
            stringBuilder.append(address);
        }
        return stringBuilder.toString();
    }
}
