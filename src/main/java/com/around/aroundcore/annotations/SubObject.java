package com.around.aroundcore.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SubObject {
     String subClassName();
     String fieldOfSubClass();
}
