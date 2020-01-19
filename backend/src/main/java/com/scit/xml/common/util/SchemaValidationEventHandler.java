package com.scit.xml.common.util;

import com.scit.xml.exception.handlers.CommonExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

public class SchemaValidationEventHandler implements ValidationEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchemaValidationEventHandler.class);

    /**
     * Receive notification of a validation warning or error.
     * <p>
     * The ValidationEvent will have a
     * {@link ValidationEventLocator ValidationEventLocator} embedded in it that
     * indicates where the error or warning occurred.
     *
     * <p>
     * If an unchecked runtime exception is thrown from this method, the JAXB
     * provider will treat it as if the method returned false and interrupt
     * the current unmarshal, validate, or marshal operation.
     *
     * @param event the encapsulated validation event information.  It is a
     *              provider error if this parameter is null.
     * @return true if the JAXB Provider should attempt to continue the current
     * unmarshal, validate, or marshal operation after handling this
     * warning/error, false if the provider should terminate the current
     * operation with the appropriate <tt>UnmarshalException</tt>,
     * <tt>ValidationException</tt>, or <tt>MarshalException</tt>.
     * @throws IllegalArgumentException if the event object is null.
     */
    @Override
    public boolean handleEvent(ValidationEvent event) {

        ValidationEventLocator validationEventLocator = event.getLocator();
        String warningMessage = String.format("Line: %d Column: %d, %s", validationEventLocator.getLineNumber(),
                validationEventLocator.getColumnNumber(), event.getMessage());

        if(event.getSeverity() == ValidationEvent.WARNING) {
            LOGGER.warn(warningMessage);
            return true;
        } else {
            LOGGER.error(warningMessage);
            return false;
        }
    }
}
