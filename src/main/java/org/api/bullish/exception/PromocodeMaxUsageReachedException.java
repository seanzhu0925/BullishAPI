package org.api.bullish.exception;

public class PromocodeMaxUsageReachedException extends RuntimeException {

    public PromocodeMaxUsageReachedException(String error) {
        super(error);
    }
}
