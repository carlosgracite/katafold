package com.carlosgracite.redroid.processor;

public class StringUtils {

    private StringUtils() {}

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
