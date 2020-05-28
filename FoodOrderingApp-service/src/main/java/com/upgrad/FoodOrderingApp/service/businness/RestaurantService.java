package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
