package com.upgrad.FoodOrderingApp.api.controller.provider;


import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;

public class BearerAuthDecoder {

    private final String accessToken;
    public static  String BEARER_AUTH_PREFIX = "Bearer ";

    public BearerAuthDecoder(final String bearerToken) throws AuthenticationFailedException {
        if(!bearerToken.startsWith(BEARER_AUTH_PREFIX)) {
            throw new AuthenticationFailedException("ATH_003", "Only BEARER auth token is supported");
        }

        final String[] bearerTokens = bearerToken.split(BEARER_AUTH_PREFIX);

        if(bearerTokens.length != 2) {
            throw new AuthenticationFailedException("ATH_004","Bearer auth token is missing");
        }

        this.accessToken = bearerTokens[1];
    }

    public String getAccessToken() {
        return accessToken;
    }

}
