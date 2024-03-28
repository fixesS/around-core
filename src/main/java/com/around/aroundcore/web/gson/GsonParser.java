package com.around.aroundcore.web.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;

public class GsonParser {
    private Gson gson;

    @PostConstruct
    private void Init(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        this.gson = gsonBuilder.setPrettyPrinting().create();
    }
    public String toJson(Object object){
        return gson.toJson(object);
    }
}
