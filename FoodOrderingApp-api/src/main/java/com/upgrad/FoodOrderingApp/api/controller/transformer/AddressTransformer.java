package com.upgrad.FoodOrderingApp.api.controller.transformer;

import com.upgrad.FoodOrderingApp.api.model.StatesList;
import com.upgrad.FoodOrderingApp.api.model.StatesListResponse;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

final public class AddressTransformer {
    public static StatesListResponse toStateListResponse(List<StateEntity> stateEntityList){

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
}
