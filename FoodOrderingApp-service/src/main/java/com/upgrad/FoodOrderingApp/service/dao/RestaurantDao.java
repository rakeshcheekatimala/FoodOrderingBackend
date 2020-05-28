package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantEntity> getAllResturants(){
        try {
            List<RestaurantEntity> restaurantEntities = entityManager.createNamedQuery("RestaurantEntity.findAll",RestaurantEntity.class).getResultList();
            return restaurantEntities;
        }catch (NoResultException e){
            return null;
        }
    }
}
