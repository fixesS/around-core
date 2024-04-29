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
            stringBuilder.append(getAddressWithoutExtraInformation());
        }
        return removeTrash(stringBuilder.toString());
    }

    public String toStringWithoutCountry(){
        StringBuilder stringBuilder = new StringBuilder();
        if(city!=null && !city.equals("") && address!=null && !address.equals("")){
            stringBuilder.append(city).append(", ");
            stringBuilder.append(getAddressWithoutExtraInformation());
        }
        return removeTrash(stringBuilder.toString());
    }
    private String removeTrash(String string){
        String result = string;
        result = result.replace("&quot;","");
        if(result.contains("\n")){
            result = result.split("\n")[0];
        }
        if(result.contains("\r")){
            result = result.split("\r")[0];
        }
        return result;
    }
    private String getAddressWithoutExtraInformation(){
        String result = address;
        if(result.contains(",")){
            long count = result.chars().filter(ch -> ch == ',').count();
            if(count>2){
                String[] list = result.split(",");
                result = String.join(", ",list[0],list[1]);
            }
        }
        return result;
    }
}
