package com.lightmock.util;

import java.util.List;
import java.util.Random;

/**
 * @author Mesut Yakut
 */
public final class ResponseSelector {
    private static final Random RANDOM = new Random();

    private ResponseSelector() {}

    public static String select(Object body) {
        if (body == null) return "";
        if (body instanceof List<?> list) {
            if (list.isEmpty()) return "";
            return String.valueOf(list.get(RANDOM.nextInt(list.size())));
        }
        return String.valueOf(body);
    }
}