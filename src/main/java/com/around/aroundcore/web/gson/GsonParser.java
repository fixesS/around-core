package com.around.aroundcore.web.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.annotation.PostConstruct;

public class GsonParser {
    private Gson gson;

    @PostConstruct
    private void Init(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        this.gson = gsonBuilder.setPrettyPrinting().create();
    }
    public String toJson(Object object){
        return gson.toJson(object);
    }
}
