package com.auth.framework.core.utils;

import java.net.URL;

public class ResourceFileProvider {

    public static String provideFullPath(String resourcePath) {
        ClassLoader classLoader = ResourceFileProvider.class.getClassLoader();
        URL resource = classLoader.getResource(resourcePath);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + resourcePath);
        } else {
            return resource.getPath().substring(1).replace("%20", " ");
        }
    }
}
