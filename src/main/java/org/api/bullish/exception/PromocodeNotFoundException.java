package org.api.bullish.exception;

public class PromocodeNotFoundException extends RuntimeException {

    public PromocodeNotFoundException(String error) {
        super(error);
    }
}
