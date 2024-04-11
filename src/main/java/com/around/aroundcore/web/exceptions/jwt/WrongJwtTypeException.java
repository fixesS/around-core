package com.around.aroundcore.web.exceptions.jwt;

import io.jsonwebtoken.JwtException;

public class WrongJwtTypeException extends JwtException {
    public WrongJwtTypeException(String message) {
        super(message);
    }

    public WrongJwtTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
