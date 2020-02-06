package com.scit.xml.common.util;

import com.scit.xml.exception.InsufficientPrivilegesException;

/**
 * Contains utility methods for {@link com.scit.xml.exception.InsufficientPrivilegesException}
 */
public final class ForbiddenUtils {

    private ForbiddenUtils() { }

    public static void throwInsufficientPrivilegesExceptionIf(boolean condition) {
        if (condition) {
            throwInsufficientPrivilegesException();
        }
    }

    public static void throwInsufficientPrivilegesException() {
        throw new InsufficientPrivilegesException();
    }
}
