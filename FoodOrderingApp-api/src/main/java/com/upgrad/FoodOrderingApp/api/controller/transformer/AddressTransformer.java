package com.upgrad.FoodOrderingApp.api.controller.transformer;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import java.util.List;
import java.util.UUID;

final public class AddressTransformer {

    public static StatesListResponse toStateListResponse(List<StateEntity> stateEntityList) {
        StatesListResponse stateListResponse = new StatesListResponse();
       //iterate stateEntityList parameter and add to StatesListResponse
        stateEntityList.forEach(se -> {
            StatesList statesItem = new StatesList(); // create new entity add to the List
            statesItem.setStateName(se.getStateName());
            statesItem.setId(UUID.fromString(se.getUuid()));
            stateListResponse.addStatesItem(statesItem);
        });
        return stateListResponse;
    }

    public static AddressListResponse toAddressListResponse(List<AddressEntity> addressEntityList) {
        AddressListResponse addressListResponse = new AddressListResponse();

        // iterate addressEntityList and add each address Item to the addressListResponse

        for (AddressEntity ae : addressEntityList) {
            AddressListState addressListState = new AddressListState();
            addressListState.setStateName(ae.getState().getStateName()); // get StateName from state obj
            // create AddressList Object and add it to the addressEntityList which is the final response
            AddressList addressList = new AddressList().id(UUID.fromString(ae.getUuid())).city(ae.getCity())
                    .flatBuildingName(ae.getFlatBuildingNumber()).locality(ae.getLocality())
                    .pincode(ae.getPincode()).state(addressListState);
            addressListResponse.addAddressesItem(addressList);
        }

        return addressListResponse; // return the final response
    }


}
