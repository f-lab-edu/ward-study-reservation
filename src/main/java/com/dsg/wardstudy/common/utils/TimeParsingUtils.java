package com.dsg.wardstudy.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TimeParsingUtils {

    public static LocalDateTime formatterLocalDateTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(time, formatter);
    }

    public static String formatterString(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }
}
