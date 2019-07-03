package com.progsoft.assignment.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Map;

public final class CommonUtils {

    /**
     * Checks if is null.
     *
     * @param obj the obj
     * @return true, if is null
     */
    public static boolean isNull(final Object obj) {
        return null == obj;
    }

    /**
     * Checks if is null or empty.
     *
     * @param <T>   the generic type
     * @param array the array
     * @return true, if is null or empty
     */
    public static <T> boolean isNullOrEmpty(final T[] array) {
        return isNull(array) || array.length == 0;
    }


    /**
     * Concat.
     *
     * @param objects the objects
     * @return the string
     */
    public static String concat(final Object... objects) {
        final StringBuilder builder = new StringBuilder();
        if (!isNullOrEmpty(objects)) {
            for (final Object obj : objects) {
                builder.append(String.valueOf(obj));
            }
        }
        return builder.toString();
    }

}