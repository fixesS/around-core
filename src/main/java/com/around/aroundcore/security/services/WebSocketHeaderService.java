package com.around.aroundcore.security.services;

import com.around.aroundcore.web.exceptions.auth.AuthHeaderNullException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;

public class WebSocketHeaderService {

    public String getLogin(ServerHttpRequest request){
        return getAuthHeaderByName(request, "login");
    }
    public String getLogin(HttpServletRequest request){
        return getAuthHeaderByName(request, "login");
    }
    public String getPasscode(ServerHttpRequest request){
        return getAuthHeaderByName(request, "passcode");
    }
    public String getPasscode(HttpServletRequest request){
        return getAuthHeaderByName(request, "passcode");
    }
    private String getAuthHeaderByName(ServerHttpRequest request, String headerName){
        String value = request.getHeaders().getFirst(headerName);
        if(value == null) throw new AuthHeaderNullException();
        return value;
    }
    private String getAuthHeaderByName( HttpServletRequest request, String headerName){
        String value = request.getHeader(headerName);
        if(value == null) throw new AuthHeaderNullException();
        return value;
    }
    public String getHeader(ServerHttpRequest request, String headerName){
        return request.getHeaders().getFirst(headerName);
    }
}
