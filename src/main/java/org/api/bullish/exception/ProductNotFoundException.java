package org.api.bullish.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String error) {
        super(error);
    }
}
