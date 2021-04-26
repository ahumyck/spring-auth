package com.auth.framework.core.utils;

import org.apache.commons.lang3.StringUtils;

public class ValidationCenter {

    public static boolean isValidString(String string) {
        return StringUtils.isNotEmpty(string) && StringUtils.isNotBlank(string);
    }

    public static boolean isValidPort(Integer integer) {
        if (integer == null) return false;
        return integer > 0 && integer < Math.pow(2, 16);
    }
}
