package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.controller.ext.ResponseBuilder;
import com.upgrad.FoodOrderingApp.api.controller.transformer.RestaurantTransformer;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
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
    @Autowired
    CategoryService categoryService;

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

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantCategoryById(@PathVariable String category_id) throws CategoryNotFoundException {

        if(category_id == null || category_id.isEmpty()){ //Checking for categoryUuid to be null or empty to throw exception.
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }
        // get the categoryEntity by using the category_id received in the request param
        CategoryEntity categoryEntity = categoryService.getCategoryByUuid(category_id);
        if(categoryEntity == null){
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }
        final List<RestaurantCategoryEntity>  categoryEntities= restaurantService.restaurantByCategory(categoryEntity.getId());

        List<RestaurantEntity> restaurantEntityList = new LinkedList<>();
        //iterate over the category entities and add them to the RestaurantEntity List
        categoryEntities.forEach(restaurantCategoryEntity -> {
            restaurantEntityList.add(restaurantCategoryEntity.getRestaurant());
        });
        RestaurantListResponse restaurantListResponse = RestaurantTransformer.toRestuarantListResponse(restaurantEntityList);
        return ResponseBuilder.ok().payload(restaurantListResponse).build();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getRestaurantById(@PathVariable String restaurant_id) throws RestaurantNotFoundException {

        if(restaurant_id == null || restaurant_id.isEmpty()){ //Checking for categoryUuid to be null or empty to throw exception.
            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
        }
        // get the categoryEntity by using the category_id received in the request param

        final RestaurantEntity  restaurantEntity= restaurantService.restaurantByUUID(restaurant_id);

        RestaurantDetailsResponse restaurantDetailsResponse = RestaurantTransformer.toResponseEntityWithCategoryItems(restaurantEntity);
        return ResponseBuilder.ok().payload(restaurantDetailsResponse).build();
    }
}