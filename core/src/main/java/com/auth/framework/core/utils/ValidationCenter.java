package com.auth.framework.core.utils;

import org.apache.commons.lang3.StringUtils;

public class ValidationCenter {

    public static boolean isValidString(String string) {
        return StringUtils.isNotEmpty(string) && StringUtils.isNotBlank(string);
    }

    public static boolean isPositiveValidNumber(Integer number) {
        return number != null && number > 0;
    }

    public static Integer validatedNumberOrDefault(Integer number, Integer defaultValue) {
        return isPositiveValidNumber(number)
                ? number : defaultValue;
    }
}
