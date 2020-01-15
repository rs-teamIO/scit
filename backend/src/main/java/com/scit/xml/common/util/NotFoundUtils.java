package com.scit.xml.common.util;

import com.scit.xml.exception.NotFoundException;

public final class NotFoundUtils {

    private NotFoundUtils() { }

    public static void throwNotFoundExceptionIf(boolean condition, String message) {
        if (condition) {
            throwNotFoundException(message);
        }
    }

    private static void throwNotFoundException(String message) {
        throw new NotFoundException(message);
    }
}
