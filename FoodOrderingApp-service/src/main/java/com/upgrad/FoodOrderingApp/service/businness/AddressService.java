package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    AddressDao addressDao;

    public List<StateEntity> getAllStates() {
        return  addressDao.getAllStates();
    }

    public AddressEntity saveAddress(AddressEntity addressEntity){
       return addressDao.saveAddress(addressEntity);
    }

    public StateEntity getStateByUUID(String stateUUID){
        return addressDao.getStateByUUID(stateUUID);
    }
}
