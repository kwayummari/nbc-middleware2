package com.itrust.middlewares.nbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeUtil {

    static Logger logger;

    public DateTimeUtil() {
        logger = LoggerFactory.getLogger(DateTimeUtil.class);
    }

    public static String dateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Africa/Nairobi"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return zonedDateTime.format(dateFormatter);
    }

    public static String dateOnly() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Africa/Nairobi"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return zonedDateTime.format(dateFormatter);
    }

    public static String dateFormat(String format) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Africa/Nairobi"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
        return zonedDateTime.format(dateFormatter);
    }

}
