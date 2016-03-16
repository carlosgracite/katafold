package com.carlosgracite.katafold.processor;

public class StringUtils {

    private StringUtils() {}

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }
}
