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

    public List<RestaurantEntity> getAllRestaurants(){
        try {
            List<RestaurantEntity> restaurantEntities = entityManager.createNamedQuery("RestaurantEntity.findAll",RestaurantEntity.class).getResultList();
            return restaurantEntities;
        }catch (NoResultException e){
            return null;
        }
    }

    public List<RestaurantEntity> getRestaurantsByName(String restaurantName) {
        try {
            List<RestaurantEntity> restaurantEntities = entityManager.createNamedQuery("RestaurantEntity.findByName", RestaurantEntity.class).setParameter("restaurantName","%" + restaurantName.toLowerCase() + "%").getResultList();
            return restaurantEntities;
        }catch (NoResultException nre){
            return null;
        }

    }
}
