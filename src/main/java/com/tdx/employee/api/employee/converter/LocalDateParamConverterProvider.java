package com.tdx.employee.api.employee.converter;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;

// Original code from: https://blog.sebastian-daschner.com/entries/jaxrs-convert-params

@Provider
public class LocalDateParamConverterProvider implements ParamConverterProvider {
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType,
                                              Annotation[] annotations) {
        if (rawType.equals(LocalDate.class))
            return (ParamConverter<T>) new LocalDateConverter();
        return null;
    }
}
