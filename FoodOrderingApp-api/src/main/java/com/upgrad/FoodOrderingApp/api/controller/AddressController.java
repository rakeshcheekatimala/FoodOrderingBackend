package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.controller.ext.ResponseBuilder;
import com.upgrad.FoodOrderingApp.api.controller.transformer.AddressTransformer;
import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.apache.commons.lang3.StringUtils;
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

    @RequestMapping(method = RequestMethod.POST, path = "/address", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public ResponseEntity<SaveAddressResponse> createAddress(SaveAddressRequest saveAddressRequest) throws SaveAddressException, AddressNotFoundException {
        // conditions to check all the values are not empty
        if(StringUtils.isEmpty(saveAddressRequest.getCity()) ||
                StringUtils.isEmpty(saveAddressRequest.getFlatBuildingName()) || StringUtils.isEmpty(saveAddressRequest.getPincode()) ||
                StringUtils.isEmpty(saveAddressRequest.getStateUuid()) || StringUtils.isEmpty(saveAddressRequest.getStateUuid())) {
                    throw new SaveAddressException("SAR-001", "No field can be empty.");
        }

        // Calls the getAllStates from addressService to get the list of stateEntity
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setFlatBuildingNumber(saveAddressRequest.getFlatBuildingName());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        StateEntity stateEntity = addressService.getStateByUUID(saveAddressRequest.getStateUuid());

        if(stateEntity==null) {
            throw new AddressNotFoundException("ANF-002", "No state by this id");
        }

        addressEntity.setState(stateEntity);
        addressEntity.setUuid(UUID.randomUUID().toString());
        addressEntity.setActive(1);

        AddressEntity createdEntity = addressService.saveAddress(addressEntity);

        SaveAddressResponse saveAddressResponse = new SaveAddressResponse().id(createdEntity.getUuid())
                .status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/states", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates() {
        // Calls the getAllStates from addressService to get the list of stateEntity
        List<StateEntity> stateEntityList = addressService.getAllStates();
        StatesListResponse stateListResponse = AddressTransformer.toStateListResponse(stateEntityList);
        return ResponseBuilder.ok().payload(stateListResponse).build();
    }
}
