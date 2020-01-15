package com.scit.xml.exception.handlers;

import com.scit.xml.common.api.ResponseCode;
import com.scit.xml.exception.BadRequestException;
import com.scit.xml.exception.InsufficientPrivilegesException;
import com.scit.xml.exception.NotFoundException;
import com.scit.xml.exception.RestApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpecificExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpecificExceptionHandler.class);

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestApiError handleBadRequestException(BadRequestException e) {
        LOGGER.error("{}", e.getMessage());

        return RestApiError.fromBadRequestException(e);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestApiError handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        LOGGER.error("{}", e.getMessage());

        return RestApiError.builder()
                .responseCode(ResponseCode.METHOD_NOT_SUPPORTED.getCode())
                .message(ResponseCode.METHOD_NOT_SUPPORTED.getMessage())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestApiError handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        LOGGER.error("{}", e.getMessage());

        return RestApiError.builder()
                .responseCode(ResponseCode.INVALID_DATA.getCode())
                .message(ResponseCode.INVALID_DATA.getMessage())
                .build();
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestApiError handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e) {
        LOGGER.error("{}", e.getMessage());

        return RestApiError.builder()
                .responseCode(ResponseCode.INVALID_MEDIA_TYPE.getCode())
                .message(ResponseCode.INVALID_MEDIA_TYPE.getMessage())
                .build();
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestApiError handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        LOGGER.error("{}", e.getMessage());

        return RestApiError.builder()
                .responseCode(ResponseCode.INVALID_MEDIA_TYPE.getCode())
                .message(ResponseCode.INVALID_MEDIA_TYPE.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestApiError handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        LOGGER.error("{}", e.getMessage());

        return RestApiError.builder()
                .responseCode(ResponseCode.INVALID_METHOD_ARGUMENT.getCode())
                .message(ResponseCode.INVALID_METHOD_ARGUMENT.getMessage())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public RestApiError handleNotFoundException(NotFoundException e) {
        LOGGER.error("{}", e.getMessage());

        return RestApiError.fromNotFoundException(e);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public RestApiError handleAccessDeniedException(AccessDeniedException e) {
        LOGGER.error("Request unauthorized.");
        LOGGER.debug("{}", e.getMessage());

        return RestApiError.fromInsufficientPrivilegesException();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestApiError handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        LOGGER.error("{}", e.getMessage());

        return RestApiError.builder()
                .responseCode(ResponseCode.REQUEST_PARAM_MISSING.getCode())
                .message(ResponseCode.REQUEST_PARAM_MISSING.getMessage())
                .build();
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestApiError handleMultipartException(MultipartException e) {
        LOGGER.error("{}", e.getMessage());

        return RestApiError.builder()
                .responseCode(ResponseCode.MULTIPART_REQUEST_REQUIRED.getCode())
                .message(ResponseCode.MULTIPART_REQUEST_REQUIRED.getMessage())
                .build();
    }

    @ExceptionHandler(InsufficientPrivilegesException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public RestApiError handleInsufficientPrivilegesException(InsufficientPrivilegesException e) {
        LOGGER.error("{}", e.getMessage());

        return RestApiError.builder()
                .responseCode(ResponseCode.INSUFFICIENT_PRIVILEGES.getCode())
                .message(ResponseCode.INSUFFICIENT_PRIVILEGES.getMessage())
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestApiError handleBadCredentialsException(BadCredentialsException e) {
        return RestApiError.builder()
                .responseCode(ResponseCode.INVALID_DATA.getCode())
                .message(e.getMessage())
                .build();
    }
}
