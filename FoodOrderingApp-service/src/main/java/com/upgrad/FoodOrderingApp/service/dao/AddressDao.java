package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class AddressDao {
    @PersistenceContext
    EntityManager entityManager;

    public List<StateEntity> getAllStates() {
        try {
            return entityManager.createNamedQuery("StateEntity.getAllStates", StateEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Transactional
    public AddressEntity saveAddress(AddressEntity addressEntity){
         entityManager.persist(addressEntity);
        return addressEntity;
    }

    public StateEntity getStateByUUID(String stateUUID){
        try {
            return entityManager.createNamedQuery("StateEntity.findStateByUUID", StateEntity.class).setParameter("uuid",stateUUID).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public AddressEntity getAddressByUUID(String addressUuid){
        try {
            return entityManager.createNamedQuery("AddressEntity.findAddressByUuid", AddressEntity.class).setParameter("uuid",addressUuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @Transactional
    public AddressEntity deleteAddress(AddressEntity addressEntity) {
        entityManager.remove(addressEntity);
        return addressEntity;
    }

    @Transactional
    public CustomerAddressEntity saveCustomerAddress(CustomerAddressEntity customerAddressEntity) {
        entityManager.persist(customerAddressEntity);
        return customerAddressEntity;
    }

    public List<CustomerAddressEntity> getAllAddress(String uuid){
        try {
             return entityManager.createNamedQuery("CustomerAddressEntity.findAddressByCustomerId", CustomerAddressEntity.class).setParameter("uuid",uuid).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CustomerAddressEntity getCustomerAddress( CustomerEntity customerEntity, final AddressEntity addressEntity) {
        try {
            return entityManager.createNamedQuery("CustomerAddressEntity.getCustomerAddressByAddressEntity", CustomerAddressEntity.class)
                    .setParameter("customer", customerEntity).setParameter( "address", addressEntity)
                    .getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }
    }
}
