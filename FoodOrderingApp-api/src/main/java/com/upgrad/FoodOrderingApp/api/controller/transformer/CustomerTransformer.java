package com.upgrad.FoodOrderingApp.api.controller.transformer;

import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.api.model.LogoutResponse;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

public class CustomerTransformer {

    public static  LoginResponse toLoginResponse(CustomerEntity customerEntity){
        LoginResponse loginResponse = new LoginResponse().id(customerEntity.getUuid()).firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName()).emailAddress(customerEntity.getEmail()).contactNumber(customerEntity.getContactNumber())
                .message("LOGGED IN SUCCESSFULLY");
        return loginResponse;
    }

    public static LogoutResponse toLogoutResponse(CustomerEntity customerEntity){
        LogoutResponse logoutResponse = new LogoutResponse().id(customerEntity.getUuid()).message("LOGGED OUT SUCCESSFULLY");
        return  logoutResponse;
    }
}
