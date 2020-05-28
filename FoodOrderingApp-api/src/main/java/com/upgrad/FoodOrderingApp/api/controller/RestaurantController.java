package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.RestaurantList;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
@CrossOrigin
public class RestaurantController {
    @Autowired
    RestaurantService restaurantService;

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants() {
        final List<RestaurantEntity>  restaurants= restaurantService.getAllResturants();

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        List<RestaurantList> restaurantLists = new ArrayList<>();
        for (RestaurantEntity restaurantEntity : restaurants) {
            RestaurantList restaurantList = new RestaurantList()
                    .id(UUID.fromString(restaurantEntity.getUuid()))
                    .restaurantName(restaurantEntity.getRestaurantName())
                    .photoURL(restaurantEntity.getPhotoUrl());
            restaurantLists.add(restaurantList); //adding the each entity to restaurantLists
        }
        restaurantListResponse.restaurants(restaurantLists);

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);

    }
}