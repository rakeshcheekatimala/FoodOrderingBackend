package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
}
