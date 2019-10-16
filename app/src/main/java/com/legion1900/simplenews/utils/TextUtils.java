package com.legion1900.simplenews.utils;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.internal.EverythingIsNonNull;

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
