package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    AddressDao addressDao;

    @Autowired
    CustomerDao customerDao;

    public List<StateEntity> getAllStates() {
        return  addressDao.getAllStates();
    }

    public AddressEntity saveAddress(AddressEntity addressEntity){
       return addressDao.saveAddress(addressEntity);
    }

    public AddressEntity saveAddress(AddressEntity addressEntity,CustomerEntity customerEntity){
        return addressDao.saveAddress(addressEntity);
    }

    public StateEntity getStateByUUID(String stateUUID){
        return addressDao.getStateByUUID(stateUUID);
    }

    public AddressEntity deleteAddress(AddressEntity addressEntity) {
        return  addressDao.deleteAddress(addressEntity);
    }

    public AddressEntity getAddressByUUID(String addressUuid) {
        return addressDao.getAddressByUUID(addressUuid);
    }

    public AddressEntity getAddressByUUID(String addressUuid,CustomerEntity customerEntity)throws AuthorizationFailedException,AddressNotFoundException{

        if(addressUuid == null) {
            throw new AddressNotFoundException("ANF-005","Address id can not be empty");
        }

        AddressEntity addressEntity = addressDao.getAddressByUUID(addressUuid);

        if (addressEntity == null) {
            throw new AddressNotFoundException("ANF-003","No address by this id");
        }

        CustomerAddressEntity customerAddressEntity = customerDao.getCustomerAddressByAddressEntity(addressEntity);

        //Checking if the address belong to the customer requested.If no throws corresponding exception.
        if(customerAddressEntity.getCustomer().getUuid() == customerEntity.getUuid()){
            return addressEntity;
        }else{
            throw new AuthorizationFailedException("ATHR-004","You are not authorized to view/update/delete any one else's address");
        }

    }


    public CustomerAddressEntity saveCustomerAddress(CustomerAddressEntity customerAddressEntity) {
        return addressDao.saveCustomerAddress(customerAddressEntity);
    }

    public List<CustomerAddressEntity> getAllAddress(String uuid) {
        return addressDao.getAllAddress(uuid);
    }

    public List<CustomerAddressEntity> getAllAddress(CustomerEntity customerEntity) {
        return addressDao.getAllAddress(customerEntity.getUuid());
    }

    public CustomerAddressEntity getCustomerAddress(CustomerEntity customerEntity, final AddressEntity addressEntity) {
        return addressDao.getCustomerAddress(customerEntity,addressEntity);
    }
}
