package com.scit.xml.exception;

import com.scit.xml.common.api.ResponseCode;

public class InvalidRequestDataException extends BadRequestException {

    public InvalidRequestDataException(String message) {
        super(ResponseCode.INVALID_REQUEST_DATA, message);
    }

}
