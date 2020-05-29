package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class AddressDao {
    @Autowired
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
            return entityManager.createNamedQuery("StateEntity.findStateByUUID", StateEntity.class).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
