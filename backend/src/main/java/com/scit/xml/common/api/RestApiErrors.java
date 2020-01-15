package com.scit.xml.common.api;

public final class RestApiErrors {

    private RestApiErrors() {}

    public static String fieldShouldNotBeEmptyString(String fieldName) {
        return String.format("Field '%s' should not be empty string.", fieldName);
    }

    public static String entityWithGivenFieldNotFound(String entityName, String fieldName) {
        return String.format("'%s' with given '%s' does not exist.", entityName, fieldName);
    }

    public static String entityWithGivenFieldAlreadyExists(String entityName, String fieldName) {
        return String.format("'%s' with given '%s' already exists.", entityName, fieldName);
    }
}
