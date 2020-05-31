package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;

@Repository
public class CustomerDao {

    @Autowired
    EntityManager entityManager;

    public CustomerEntity getCustomerByContact(String contactNumber){
        try{
            return entityManager.createNamedQuery("CustomerEntity.customerByContactNumber",CustomerEntity.class).setParameter("contactNumber",contactNumber).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public CustomerAuthEntity createAuthToken( CustomerAuthEntity customerAuthEntity) {
         entityManager.persist(customerAuthEntity);
         return customerAuthEntity;
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
}
