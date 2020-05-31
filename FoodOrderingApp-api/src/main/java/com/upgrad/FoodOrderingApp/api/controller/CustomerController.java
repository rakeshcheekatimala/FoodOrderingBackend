package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.controller.provider.BasicAuthDecoder;
import com.upgrad.FoodOrderingApp.api.controller.transformer.CustomerTransformer;
import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
@CrossOrigin
public class CustomerController {
        @Autowired
        CustomerService customerService;

        @RequestMapping(method = RequestMethod.POST, path = "/customer/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
        public ResponseEntity<LoginResponse> login(@RequestHeader("authorization")  String authorization) throws AuthenticationFailedException {
            final BasicAuthDecoder basicAuthDecoder = new BasicAuthDecoder(authorization);

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
}
