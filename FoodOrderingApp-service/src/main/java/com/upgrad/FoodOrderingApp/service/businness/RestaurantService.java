package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.List;

@Service
public class RestaurantService {
    @Autowired
    RestaurantDao restaurantDao;

    public List<RestaurantEntity> getAllRestaurants(){
        return restaurantDao.getAllRestaurants();
    }

    public List<RestaurantEntity> restaurantsByName(String restaurantName) {
        return restaurantDao.getRestaurantsByName(restaurantName);
    }

    public RestaurantEntity restaurantByUUID(String restaurant_id) {
        return restaurantDao.getRestaurantByUUID(restaurant_id);
    }

    public List<RestaurantCategoryEntity> restaurantByCategory(Long categoryId){
        return restaurantDao.getRestaurantByCategory(categoryId);
    }

    public RestaurantEntity updateRestaurantRating(final RestaurantEntity restaurantEntity) {
        return restaurantDao.updateRestaurantRating(restaurantEntity);
    }
}
