package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.controller.provider.BasicAuthDecoder;
import com.upgrad.FoodOrderingApp.api.controller.provider.BearerAuthDecoder;
import com.upgrad.FoodOrderingApp.api.controller.transformer.CustomerTransformer;
import com.upgrad.FoodOrderingApp.api.model.*;


import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.common.UtilityProvider;

import com.upgrad.FoodOrderingApp.service.businness.CustomerAuthService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/")
@CrossOrigin
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerAuthService customerAuthService;

    @Autowired
    UtilityProvider utilityProvider; // It Provides Data Check methods for various cases

    @RequestMapping(method = RequestMethod.POST, path = "/customer/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader("authorization") String authorization) throws AuthenticationFailedException {
        final BasicAuthDecoder basicAuthDecoder = new BasicAuthDecoder(authorization);

        if(basicAuthDecoder==null) {
            throw  new AuthenticationFailedException("ATH-003","Incorrect format of decoded customer name and password");
        }

        // authenticate service method needs contactNumber & password
        // if both matches they would set the token and send the entity back

        CustomerAuthEntity customerAuthEntity = customerService.authenticate(basicAuthDecoder.getContactNumber(),basicAuthDecoder.getPassword());
        if(customerAuthEntity!=null){
            HttpHeaders headers = new HttpHeaders();
            headers.add("access-token", customerAuthEntity.getAccessToken());
            headers.add("access-control-expose-headers", "access-token");

            if(StringUtils.isEmpty(authorization)) {
                throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
            }
            LoginResponse loginResponse = CustomerTransformer.toLoginResponse(customerAuthEntity.getCustomer());
            return new ResponseEntity<LoginResponse>(loginResponse, headers, HttpStatus.OK);
        }
        else {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }
    }

    @RequestMapping(method= RequestMethod.POST, path="/customer/logout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, AuthenticationFailedException {

        // authDecoder splits the authorization to extract the token
        final BearerAuthDecoder authDecoder = new BearerAuthDecoder(authorization);
        String accessToken = authDecoder.getAccessToken();
        CustomerAuthEntity customerAuthEntity = customerAuthService.getCustomerByToken(accessToken);

        final CustomerAuthEntity logoutEntity = customerService.logout(customerAuthEntity);
        LogoutResponse logoutResponse = CustomerTransformer.toLogoutResponse(logoutEntity.getCustomer());
            // Returns the LogoutResponse with OK http status
        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
        // Calls the logout method by passing the bearer token
    }


    @RequestMapping(method = RequestMethod.POST, path = "/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signUpCustomer(@RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {
        //Creating new Customer entity from the request data
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setPassword(signupCustomerRequest.getPassword());
        customerEntity.setUuid(UUID.randomUUID().toString());

        //Checking if the Request Data is valid and has all the fields required.
        utilityProvider.isValidSignupRequest(customerEntity);

        //Passing the Customer entity to the customerService saveCustomer method to persist in the database.
        CustomerEntity signedUpCustomer = customerService.saveCustomer(customerEntity);

        //Creating Response for the Request
        SignupCustomerResponse signupCustomerResponse = new SignupCustomerResponse().id(signedUpCustomer.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");

        return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.CREATED);
    }

    /* This method handles the customer details update request. Takes the request as UpdateCustomerRequest and produces UpdateCustomerResponse containing the details of the updated Customer.
       If error returns the error code with corresponding Message.
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT, path = "/customer", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdateCustomerResponse> updateCustomerDetails(@RequestHeader("authorization") final String authorization, @RequestBody(required = false) UpdateCustomerRequest updateCustomerRequest) throws AuthorizationFailedException, UpdateCustomerException {

        //Checking if the request is valid
        utilityProvider.isValidUpdateCustomerRequest(updateCustomerRequest.getFirstName());

        //Access the accessToken from the request Header
        String accessToken = authorization.split("Bearer ")[1];

        //Calls customerService getCustomerMethod to check the validity of the customer.this methods returns the customerEntity  to be updated.
        CustomerEntity toBeUpdatedCustomerEntity = customerService.getCustomer(accessToken);

        //updating the customer entity
        toBeUpdatedCustomerEntity.setFirstName(updateCustomerRequest.getFirstName());
        toBeUpdatedCustomerEntity.setLastName(updateCustomerRequest.getLastName());

        //Calls customerService updateCustomer to persist the updated Entity.
        CustomerEntity updatedCustomerEntity = customerService.updateCustomer(toBeUpdatedCustomerEntity);

        //Creating the UpdateCustomerResponse with updated details.
        UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse()
                .firstName(updatedCustomerEntity.getFirstName())
                .lastName(updatedCustomerEntity.getLastName())
                .id(updatedCustomerEntity.getUuid())
                .status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");

        return new ResponseEntity<UpdateCustomerResponse>(updateCustomerResponse, HttpStatus.OK);
    }

    /* This method is used to update password of the customer.It takes updatePasswordRequest and produces updatePasswordResponse containing UUID and the successful message.
       If error returns the error code with corresponding Message.
     */
    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT, path = "/customer/password", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> updateCustomerPassword(@RequestHeader("authorization") final String authorization, @RequestBody(required = false) UpdatePasswordRequest updatePasswordRequest) throws AuthorizationFailedException, UpdateCustomerException {

        //Checking if the data received is valid
        utilityProvider.isValidUpdatePasswordRequest(updatePasswordRequest.getOldPassword(), updatePasswordRequest.getNewPassword());

        //Access the accessToken from the request Header
        String accessToken = authorization.split("Bearer ")[1];

        //Creating variables for passwords
        String oldPassword = updatePasswordRequest.getOldPassword();
        String newPassword = updatePasswordRequest.getNewPassword();

        //Calls customerService getCustomerMethod to check the validity of the customer.this methods returns the customerEntity  to be updated.
        CustomerEntity toBeUpdatedCustomerEntity = customerService.getCustomer(accessToken);

        //updating the customer entity
        CustomerEntity updatedCustomerEntity = customerService.updateCustomerPassword(oldPassword, newPassword, toBeUpdatedCustomerEntity);

        //Creating the UpdatePasswordResponse with updated details.
        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse()
                .id(updatedCustomerEntity.getUuid())
                .status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");

        return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse, HttpStatus.OK);
    }

}
