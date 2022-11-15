package org.api.bullish.response;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ApiResponse<T> {

    private final boolean success;
    private final List<String> error;
    private final T response;
    private final HttpStatus status;

    public ApiResponse(boolean success, List<String> error, HttpStatus status, T response) {
        this.success = success;
        this.error = error;
        this.status = status;
        this.response = response;
    }

    public boolean isSuccess() {
        return error.isEmpty() && status.is2xxSuccessful();
    }

}
