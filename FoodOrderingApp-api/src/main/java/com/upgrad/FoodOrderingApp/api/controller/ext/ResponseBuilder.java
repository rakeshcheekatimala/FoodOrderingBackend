package com.upgrad.FoodOrderingApp.api.controller.ext;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Response builder.
 */

public class ResponseBuilder<T> {

    private final HttpStatus status;
    private final HttpHeaders headers = new HttpHeaders();
    private T payload;

    private ResponseBuilder(final HttpStatus status) {
        this.status = status;
    }

    public static <T> ResponseBuilder ok() {
        return new ResponseBuilder<T>(HttpStatus.OK);
    }

    public static <T> ResponseBuilder created() {
        return new ResponseBuilder<T>(HttpStatus.CREATED);
    }

    public ResponseBuilder<T> payload(T payload) {
        this.payload = payload;
        return this;
    }

    public ResponseEntity<T> build() {
        return new ResponseEntity<T>(payload, headers, status);
    }

}

