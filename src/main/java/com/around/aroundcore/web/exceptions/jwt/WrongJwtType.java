package com.around.aroundcore.web.exceptions.jwt;

import io.jsonwebtoken.JwtException;

public class WrongJwtType extends JwtException {
    public WrongJwtType(String message) {
        super(message);
    }

    public WrongJwtType(String message, Throwable cause) {
        super(message, cause);
    }
}
