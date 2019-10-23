package com.ryanair.flight.exception;

public class ExternalApiException extends RuntimeException {
    public ExternalApiException(String url, int statusCode) {
        super(String.format("Response for URL: %s was %s", url, statusCode));
    }
}
