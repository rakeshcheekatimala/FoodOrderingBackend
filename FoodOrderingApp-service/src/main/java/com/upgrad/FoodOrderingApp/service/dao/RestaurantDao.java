package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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

    public RestaurantEntity getRestaurantByUUID(String restaurant_id){
        try {
            RestaurantEntity restaurantEntity = entityManager.createNamedQuery("RestaurantEntity.byUUid", RestaurantEntity.class).setParameter("uuid",restaurant_id).getSingleResult();
            return restaurantEntity;
        }catch (NoResultException nre){
            return null;
        }
    }

    public List<RestaurantCategoryEntity> getRestaurantByCategory(Long categoryId){

        try {
            return entityManager.createNamedQuery("findRestaurantsByCategoryId", RestaurantCategoryEntity.class).setParameter("categoryId",categoryId).getResultList();
        }catch (NoResultException nre){
            return null;
        }
    }

    @Transactional
    public RestaurantEntity updateRestaurantRating(final RestaurantEntity restaurantEntity){
        entityManager.merge(restaurantEntity);
        return restaurantEntity;
    }
}
