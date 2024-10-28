package com.around.aroundcore.web.services;

import com.around.aroundcore.annotations.NotRead;
import com.around.aroundcore.annotations.SubObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

@Service
@Slf4j
public class EntityPatcher {
    public <T, U> void  patch(T to, U from) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        Field[] internFields = from.getClass().getDeclaredFields();
        for(Field field : internFields){
            if (Modifier.isPrivate(field.getModifiers())) {
                if(field.isAnnotationPresent(NotRead.class)){
                    continue;
                }
                PropertyDescriptor pdFrom;
                PropertyDescriptor pdTo;
                Object classOfField;
                if(field.isAnnotationPresent(SubObject.class)){
                    SubObject subObject = field.getAnnotation(SubObject.class);

                    pdFrom = new PropertyDescriptor(field.getName(), from.getClass());
                    PropertyDescriptor pdTo1 = new PropertyDescriptor(subObject.subClassName() , to.getClass());
                    Object subObj = pdTo1.getReadMethod().invoke(to);

                    pdTo = new PropertyDescriptor(subObject.fieldOfSubClass(), subObj.getClass());
                    classOfField = subObj;
                }else{
                    pdFrom = new PropertyDescriptor(field.getName(), from.getClass());
                    pdTo = new PropertyDescriptor(field.getName(), to.getClass());
                    classOfField = to;
                }
                Object value = pdFrom.getReadMethod().invoke(from);
                if(value!=null){
                    pdTo.getWriteMethod().invoke(classOfField ,value);
                }
            }
        }
    }
}
