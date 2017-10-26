package com.ef.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ParserUtil {
    public static final String INPUT_DATE_PATTERN = "yyyy-MM-dd.HH:mm:ss";
    public static final String LOG_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final DateTimeFormatter INPUT_DATE_FORMAT = DateTimeFormat.forPattern(INPUT_DATE_PATTERN);
    public static final DateTimeFormatter LOG_DATE_FORMAT = DateTimeFormat.forPattern(LOG_DATE_PATTERN);

    public static Date addHours(Date oldDate,int hours) {
        return new Date(oldDate.getTime() + TimeUnit.HOURS.toMillis(hours));
    }

    public static void printResult(Object result) {
        if (result == null) {
            System.out.print("NULL");
        } else if (result instanceof Object[]) {
            Object[] row = (Object[]) result;
            System.out.print("[");
            for (int i = 0; i < row.length; i++) {
                printResult(row[i]);
            }
            System.out.print("]");
        } else if (result instanceof Long || result instanceof Double
                   || result instanceof String) {
            System.out.print(result.getClass().getName() + ": " + result);
        } else {
            System.out.print(result);
        }
        System.out.println();
    }
}
