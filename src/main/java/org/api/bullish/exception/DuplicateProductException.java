package org.api.bullish.exception;

public class DuplicateProductException extends RuntimeException {

    public DuplicateProductException(String error) {
        super(error);
    }
}
