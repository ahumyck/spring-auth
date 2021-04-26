package com.auth.framework.core.utils.inverter;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapInverter {

    private final Map<String, Collection<String>> mapToInvert;

    public MapInverter(Map<String, Collection<String>> mapToInvert) {
        this.mapToInvert = mapToInvert;
    }

    public Map<String, String> invert() {
        Map<String, String> inverted = new ConcurrentHashMap<>(mapToInvert.size(), 1.75f);

        for (Map.Entry<String, Collection<String>> entry : mapToInvert.entrySet()) {
            for (String value : entry.getValue()) {
                inverted.put(value, entry.getKey());
            }
        }

        return inverted;

    }


}
