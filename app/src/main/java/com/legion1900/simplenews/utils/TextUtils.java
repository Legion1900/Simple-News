package com.legion1900.simplenews.utils;

public class TextUtils {
    private static final StringBuilder builder = new StringBuilder();

    private TextUtils() {}

    public static String buildFieldValue(String fieldKey, String value) {
        builder.setLength(0);
        builder.append(fieldKey);
        builder.append(value);
        return builder.toString();
    }
}
