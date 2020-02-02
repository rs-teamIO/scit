package com.scit.xml.common.util;

import com.scit.xml.exception.InvalidRequestDataException;

/**
 * Contains utility methods for {@link com.scit.xml.exception.BadRequestException}
 */
public final class BadRequestUtils {

    private BadRequestUtils() { }

    public static void throwInvalidRequestDataExceptionIf(boolean condition, String message) {
        if (condition) {
            throwInvalidRequestDataException(message);
        }
    }

    public static void throwInvalidRequestDataException(String message) {
        throw new InvalidRequestDataException(message);
    }
}
