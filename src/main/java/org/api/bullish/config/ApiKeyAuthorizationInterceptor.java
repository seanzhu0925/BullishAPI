package org.api.bullish.config;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class ApiKeyAuthorizationInterceptor implements ClientHttpRequestInterceptor {
    private final String apiKey;

    public ApiKeyAuthorizationInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        httpRequest.getHeaders().add("API-KEY", apiKey);
        return clientHttpRequestExecution.execute(httpRequest, body);
    }
}
