package com.around.aroundcore.web.dtos.coords;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Location {
    private String country;
    private String city;
    private String address;

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

    public String toStringWithoutCountry(){
        StringBuilder stringBuilder = new StringBuilder();
        if(city!=null && !city.equals("") && address!=null && !address.equals("")){
            stringBuilder.append(city).append(", ");
            stringBuilder.append(address);
        }
        return stringBuilder.toString();
    }
}
