package com.around.aroundcore.core.services.apis.oauth;

import com.around.aroundcore.config.AroundConfig;
import com.around.aroundcore.core.exceptions.api.validation.EmailValidationException;
import com.around.aroundcore.core.exceptions.api.validation.PasswordValidationException;
import com.around.aroundcore.core.exceptions.api.validation.UsernameValidationException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidationService {
    public void validateEmail(String email) throws EmailValidationException {
        if(!validate(email, AroundConfig.EMAIL_REGEX)){
            throw new EmailValidationException();
        }
    }
    public void validateUsername(String username) throws UsernameValidationException{
        if(!validate(username, AroundConfig.USERNAME_REGEX)){
            throw new UsernameValidationException();
        }
    }
    public void validatePassword(String password) throws PasswordValidationException {
        if(!validate(password, AroundConfig.PASSWORD_REGEX)){
            throw new PasswordValidationException();
        }
    }
    private boolean validate(String value, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
