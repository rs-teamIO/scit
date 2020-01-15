package com.scit.xml.exception.handlers;

import com.scit.xml.common.api.ResponseCode;
import com.scit.xml.exception.RestApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class CommonExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestApiError handleGeneralException(Exception e) {
        LOGGER.error("{}", e);

        return RestApiError.builder()
                .responseCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }
}
