package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.controller.ext.ResponseBuilder;
import com.upgrad.FoodOrderingApp.api.controller.transformer.RestaurantTransformer;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/")
@CrossOrigin
public class RestaurantController {
    @Autowired
    RestaurantService restaurantService;

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants() {
        final List<RestaurantEntity>  restaurants= restaurantService.getAllRestaurants();
        RestaurantListResponse restaurantListResponse = RestaurantTransformer.toRestuarantListResponse(restaurants);
        return ResponseBuilder.ok().payload(restaurantListResponse).build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{restaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants(@PathVariable String restaurant_name) throws RestaurantNotFoundException {

        if(restaurant_name == null || restaurant_name.isEmpty()){ //Checking for categoryUuid to be null or empty to throw exception.
            throw new RestaurantNotFoundException("RNF-003","Restaurant name field should not be empty");
        }

        final List<RestaurantEntity>  restaurants= restaurantService.restaurantsByName(restaurant_name);
        RestaurantListResponse restaurantListResponse = RestaurantTransformer.toRestuarantListResponse(restaurants);
        return ResponseBuilder.ok().payload(restaurantListResponse).build();
    }
}