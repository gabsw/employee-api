package com.tdx.employee.api.employee.converter;

import javax.ws.rs.ext.ParamConverter;
import java.time.LocalDate;

// Original code from: https://blog.sebastian-daschner.com/entries/jaxrs-convert-params

public class LocalDateConverter implements ParamConverter<LocalDate> {
    @Override
    public LocalDate fromString(String value) {
        if (value == null)
            return null;
        return LocalDate.parse(value);
    }

    @Override
    public String toString(LocalDate value) {
        if (value == null)
            return null;
        return value.toString();
    }
}
