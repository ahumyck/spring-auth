package com.auth.framework.core.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    private final static Map<TimeUnit, Long> convertTime = new LinkedHashMap<>(6, 1.75f);

    static {
        convertTime.put(TimeUnit.SECONDS, 1000L);
        convertTime.put(TimeUnit.MINUTES, 60 * convertTime.get(TimeUnit.SECONDS));
        convertTime.put(TimeUnit.HOURS, 60 * convertTime.get(TimeUnit.MINUTES));
        convertTime.put(TimeUnit.DAYS, 24 * convertTime.get(TimeUnit.HOURS));
    }

    public static Date createDateFromNow(float units, TimeUnit unit) {
        return new Date(System.currentTimeMillis() + (long) (units * convertTime.get(unit)));
    }
}
