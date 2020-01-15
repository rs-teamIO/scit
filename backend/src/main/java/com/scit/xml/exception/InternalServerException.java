package com.scit.xml.exception;

public class InternalServerException extends RuntimeException {

    public InternalServerException() { }

    public InternalServerException(Exception e) {
        super(e);
    }
}
