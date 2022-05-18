package com.itravel.api.shop.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RequiredArgsConstructor
@Component
public class DateTimeUtils {
    private final DateTimeFormatter formatter;

    public ZonedDateTime toZonedDate(String string){
        try{
            return ZonedDateTime.parse(string);
        }catch (DateTimeParseException e) {
            return LocalDate.parse(string, formatter).atStartOfDay(ZoneId.systemDefault());
        }
    }
}
