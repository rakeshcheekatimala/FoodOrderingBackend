package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.controller.ext.ResponseBuilder;
import com.upgrad.FoodOrderingApp.api.controller.transformer.AddressTransformer;
import com.upgrad.FoodOrderingApp.api.model.StatesList;
import com.upgrad.FoodOrderingApp.api.model.StatesListResponse;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

@Controller
@CrossOrigin
@RequestMapping("/")
public class AddressController {
    @Autowired
    AddressService addressService;

    @RequestMapping(method = RequestMethod.GET, path = "/states", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates() {
        // Calls the getAllStates from addressService to get the list of stateEntity
        List<StateEntity> stateEntityList = addressService.getAllStates();
        StatesListResponse stateListResponse = AddressTransformer.toStateListResponse(stateEntityList);
        return ResponseBuilder.ok().payload(stateListResponse).build();
    }
}
