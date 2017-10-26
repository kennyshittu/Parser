package com.ef.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ParserUtil {

  public static final String INPUT_DATE_PATTERN = "yyyy-MM-dd.HH:mm:ss";
  public static final String LOG_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat(INPUT_DATE_PATTERN);
  public static final SimpleDateFormat LOG_DATE_FORMAT = new SimpleDateFormat(LOG_DATE_PATTERN);

  public static Date addHours(Date oldDate, int hours) {
    return new Date(oldDate.getTime() + TimeUnit.HOURS.toMillis(hours));
  }

  public static void setLevel(Level newLvl) {
    Logger rootLogger = LogManager.getLogManager().getLogger("");
    Handler[] handlers = rootLogger.getHandlers();
    rootLogger.setLevel(newLvl);
    for (Handler h : handlers) {
      h.setLevel(newLvl);
    }
  }
}
