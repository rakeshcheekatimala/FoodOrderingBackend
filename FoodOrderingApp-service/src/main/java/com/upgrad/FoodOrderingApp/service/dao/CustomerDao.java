package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;


@Repository
public class CustomerDao {

    @Autowired
    EntityManager entityManager;

    public CustomerEntity getCustomerByContact(String contactNumber) {
        try {
            return entityManager.createNamedQuery("CustomerEntity.customerByContactNumber", CustomerEntity.class).setParameter("contactNumber", contactNumber).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CustomerAuthEntity createAuthToken(CustomerAuthEntity customerAuthEntity) {
        entityManager.persist(customerAuthEntity);
        return customerAuthEntity;
    }

    public CustomerEntity createCustomer(CustomerEntity customerEntity) {
        entityManager.persist(customerEntity);
        return customerEntity;
    }

    //To update customer
    public CustomerEntity updateCustomer(CustomerEntity customerToBeUpdated) {
        entityManager.merge(customerToBeUpdated);
        return customerToBeUpdated;
    }

    //To get Customer By Uuid if no results return null
    public CustomerEntity getCustomerByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("CustomerEntity.customerByUuid", CustomerEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CustomerAuthEntity updateAuthToken( CustomerAuthEntity customerAuthEntity) {
        entityManager.merge(customerAuthEntity);
        return customerAuthEntity;
    }

    @Transactional
    public CustomerAuthEntity logout(CustomerAuthEntity customerAuthEntity) {
        final ZonedDateTime now = ZonedDateTime.now();
        customerAuthEntity.setLogoutAt(now);
        entityManager.merge(customerAuthEntity);
        return  customerAuthEntity;
    }

    public CustomerAddressEntity getCustomerAddressByAddressEntity(AddressEntity addressEntity){
        try {
            CustomerAddressEntity customerAddressEntity = entityManager.createNamedQuery("CustomerAddressEntity.getCustomerAddressByAddressEntity",CustomerAddressEntity.class).setParameter("address_entity",addressEntity).getSingleResult();
            return customerAddressEntity;
        }catch (NoResultException nre){
            return null;
        }
    }
}
