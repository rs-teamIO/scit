package com.scit.xml.exception;

import com.scit.xml.common.api.ResponseCode;

public class BadRequestException extends RuntimeException {

    private final ResponseCode responseCode;
    private final String message;

    public BadRequestException(ResponseCode responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public String getMessage() {
        return message;
    }
}
