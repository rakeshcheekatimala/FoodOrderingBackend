package com.upgrad.FoodOrderingApp.api.controller.provider;

import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.Base64;

/**
 * Provider to decode basic auth credentials.
 */
public final class BasicAuthDecoder {

    private final String contactNumber;
    private final String password;

    public BasicAuthDecoder(final String base64EncodedCredentials) throws  AuthenticationFailedException{
        try {
            final String[] base64Decoded = new String(Base64.getDecoder().decode(base64EncodedCredentials.split("Basic ")[1])).split(":");
            this.contactNumber = base64Decoded[0];
            this.password = base64Decoded[1];
        }
        catch (Exception e) {
            throw new AuthenticationFailedException("ATH-003","Incorrect format of decoded customer name and password");
        }

    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getPassword() {
        return password;
    }
}
