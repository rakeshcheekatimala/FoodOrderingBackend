package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.controller.ext.ResponseBuilder;
import com.upgrad.FoodOrderingApp.api.controller.provider.BearerAuthDecoder;
import com.upgrad.FoodOrderingApp.api.controller.transformer.AddressTransformer;
import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerAuthService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Controller
@CrossOrigin
@RequestMapping("/")
public class AddressController {
    @Autowired
    AddressService addressService;
    @Autowired
    CustomerAuthService customerAuthService;

    @RequestMapping(method = RequestMethod.POST, path = "/address", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public ResponseEntity<SaveAddressResponse> createAddress(SaveAddressRequest saveAddressRequest,@RequestHeader("authorization") final String authorization) throws SaveAddressException, AddressNotFoundException, AuthenticationFailedException, AuthorizationFailedException {
        // conditions to check all the values are not empty
        final BearerAuthDecoder authDecoder = new BearerAuthDecoder(authorization);
        String accessToken = authDecoder.getAccessToken();
        CustomerAuthEntity customerAuthEntity = customerAuthService.getCustomerByToken(accessToken);
        Boolean isAuthorizedUser = customerAuthService.isAuthorizedUser(accessToken,customerAuthEntity);

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

        AddressEntity createdEntity = addressService.saveAddress(addressEntity); // this creates a new address

        final CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();
        customerAddressEntity.setAddress(createdEntity); // save the saved addressEntity
        customerAddressEntity.setCustomer(customerAuthEntity.getCustomer()); // get the logged in customer
        addressService.saveCustomerAddress(customerAddressEntity); // save the customerAddressEntity

        SaveAddressResponse saveAddressResponse = new SaveAddressResponse().id(createdEntity.getUuid())
                .status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/address/{address_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteAddress(@PathVariable String address_id,@RequestHeader("authorization") final String authorization) throws AddressNotFoundException, AuthorizationFailedException, AuthenticationFailedException {
        // Calls the getAllStates from addressService to get the list of stateEntity
        final BearerAuthDecoder authDecoder = new BearerAuthDecoder(authorization);
        String accessToken = authDecoder.getAccessToken();
        CustomerAuthEntity customerAuthEntity = customerAuthService.getCustomerByToken(accessToken);
        Boolean isAuthorizedUser = customerAuthService.isAuthorizedUser(accessToken,customerAuthEntity);

        if(StringUtils.isEmpty(address_id)){
            throw new AddressNotFoundException("ANF-005","Address id can not be empty");
        }

        AddressEntity addressEntity = addressService.getAddressByUUID(address_id);

        if (addressEntity == null) {
            throw new AddressNotFoundException("ANF-003", "No address by this id.");
        }


        CustomerAddressEntity customerAddressEntity = addressService.getCustomerAddress(customerAuthEntity.getCustomer(), addressEntity);

        if (customerAddressEntity == null) {
            throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
        }

        AddressEntity deletedEntity = addressService.deleteAddress(addressEntity);

        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse().id(UUID.fromString(deletedEntity.getUuid()))
                .status("ADDRESS DELETED SUCCESSFULLY");

        return  new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllCustomerAddress(@RequestHeader("authorization") String authorization) throws AuthorizationFailedException, AuthenticationFailedException {
        final BearerAuthDecoder authDecoder = new BearerAuthDecoder(authorization);
        String accessToken = authDecoder.getAccessToken();
        CustomerAuthEntity customerAuthEntity = customerAuthService.getCustomerByToken(accessToken);
        Boolean isAuthorizedUser = customerAuthService.isAuthorizedUser(accessToken,customerAuthEntity);
        // isAuthorizedUser will tell whether the user is valid user or not
        if(isAuthorizedUser) {
            List<CustomerAddressEntity> customerAddressEntities = addressService.getAllAddress(customerAuthEntity.getCustomer().getUuid());
            List<AddressEntity> addressEntityList = new LinkedList<>();
            //iterate over the customerAddressEntities List and add each address entity to the addressEntityList
            customerAddressEntities.forEach(addressEntity -> {
                addressEntityList.add(addressEntity.getAddress());
            });

            // call the transform method to populate the AddressListResponse with the correct data
            AddressListResponse addressListResponse = AddressTransformer.toAddressListResponse(addressEntityList);
            return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
        }
        else {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }

    }

    @RequestMapping(method = RequestMethod.GET, path = "/states", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates() {
        // Calls the getAllStates from addressService to get the list of stateEntity
        List<StateEntity> stateEntityList = addressService.getAllStates();
        StatesListResponse stateListResponse = AddressTransformer.toStateListResponse(stateEntityList);
        return ResponseBuilder.ok().payload(stateListResponse).build();
    }
}
