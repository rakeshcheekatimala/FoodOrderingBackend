package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.model.CustomerAuthTokenStatus;
import java.time.ZonedDateTime;

public final class CustomerAuthTokenVerifier {

    private final CustomerAuthTokenStatus status;

    public CustomerAuthTokenVerifier(final CustomerAuthEntity userAuthToken) {

        if (userAuthToken == null) {
            status = CustomerAuthTokenStatus.NOT_FOUND;
        } else if (isLoggedOut(userAuthToken)) {
            status = CustomerAuthTokenStatus.LOGGED_OUT;
        } else if (isExpired(userAuthToken)) {
            status = CustomerAuthTokenStatus.EXPIRED;
        } else {
            status = CustomerAuthTokenStatus.ACTIVE;
        }
    }

    public boolean isActive() {
        return CustomerAuthTokenStatus.ACTIVE == status;
    }

    public boolean hasExpired() {
        return CustomerAuthTokenStatus.EXPIRED == status;
    }

    public boolean hasLoggedOut() {
        return CustomerAuthTokenStatus.LOGGED_OUT == status;
    }

    public boolean isNotFound() {
        return CustomerAuthTokenStatus.NOT_FOUND == status;
    }

    public CustomerAuthTokenStatus getStatus() {
        return status;
    }

    private boolean isExpired(final CustomerAuthEntity userAuthToken) {
        final ZonedDateTime now = ZonedDateTime.now();
        return userAuthToken != null && (userAuthToken.getExpiresAt().isBefore(now) || userAuthToken.getExpiresAt().isEqual(now));
    }

    private boolean isLoggedOut(final CustomerAuthEntity userAuthToken) {
        return userAuthToken != null && userAuthToken.getLogoutAt() != null;
    }

}
