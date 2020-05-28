package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddressState;
import com.upgrad.FoodOrderingApp.api.model.RestaurantList;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        List<RestaurantList> restaurantLists = new ArrayList<>();
        for (RestaurantEntity restaurantEntity : restaurants) {
            RestaurantDetailsResponseAddress address = new RestaurantDetailsResponseAddress();
            address.setCity(restaurantEntity.getAddress().getCity());
            address.setFlatBuildingName(restaurantEntity.getAddress().getFlatBuildingNumber());
            address.setCity(restaurantEntity.getAddress().getCity());
            address.setLocality(restaurantEntity.getAddress().getLocality());
            address.setPincode(restaurantEntity.getAddress().getPincode());
            address.setId(UUID.fromString(restaurantEntity.getAddress().getUuid()));

            //creating the state object to store state details
            RestaurantDetailsResponseAddressState state =  new RestaurantDetailsResponseAddressState();
            if(restaurantEntity.getAddress()!=null){
                state.setStateName(restaurantEntity.getAddress().getState().getStateName());
                state.setId(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()));
                address.setState(state); // adding the state to address object
            }

            List categoryList = restaurantEntity.getRestaurantCategories();

            //Collections.sort(categoryList);
            ListIterator<RestaurantCategoryEntity> iterator = categoryList.listIterator();
            List<String> categoryResult = new ArrayList<String>();
           while(iterator.hasNext()){
               RestaurantCategoryEntity restaurantCategoryEntity  = new RestaurantCategoryEntity();
               restaurantCategoryEntity = iterator.next();
               String categoryName = restaurantCategoryEntity.getCategory().getCategoryName();
               if(!categoryName.isEmpty() && categoryName!=null){
                   categoryResult.add(restaurantCategoryEntity.getCategory().getCategoryName());
               }
           }

           Collections.sort(categoryResult);
           String categoryResultDelimeter = String.join(",", categoryResult);


            RestaurantList restaurantList = new RestaurantList()
                    .id(UUID.fromString(restaurantEntity.getUuid()))
                    .restaurantName(restaurantEntity.getRestaurantName())
                    .photoURL(restaurantEntity.getPhotoUrl())
                    .numberCustomersRated(restaurantEntity.getNumberOfCustomersRated())
                    .customerRating(restaurantEntity.getCustomerRating())
                    .averagePrice(restaurantEntity.getAveragePriceForTwo())
                    .address(address)
                    .categories(categoryResultDelimeter);


            restaurantLists.add(restaurantList); //adding the each entity to restaurantLists
        }
        restaurantListResponse.restaurants(restaurantLists);

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{restaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantListResponse> getAllRestaurants(@PathVariable String restaurant_name) {
        final List<RestaurantEntity>  restaurants= restaurantService.restaurantsByName(restaurant_name);

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        List<RestaurantList> restaurantLists = new ArrayList<>();
        for (RestaurantEntity restaurantEntity : restaurants) {
            RestaurantDetailsResponseAddress address = new RestaurantDetailsResponseAddress();
            address.setCity(restaurantEntity.getAddress().getCity());
            address.setFlatBuildingName(restaurantEntity.getAddress().getFlatBuildingNumber());
            address.setCity(restaurantEntity.getAddress().getCity());
            address.setLocality(restaurantEntity.getAddress().getLocality());
            address.setPincode(restaurantEntity.getAddress().getPincode());
            address.setId(UUID.fromString(restaurantEntity.getAddress().getUuid()));

            //creating the state object to store state details
            RestaurantDetailsResponseAddressState state =  new RestaurantDetailsResponseAddressState();
            if(restaurantEntity.getAddress()!=null){
                state.setStateName(restaurantEntity.getAddress().getState().getStateName());
                state.setId(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()));
                address.setState(state); // adding the state to address object
            }

            List categoryList = restaurantEntity.getRestaurantCategories();

            //Collections.sort(categoryList);
            ListIterator<RestaurantCategoryEntity> iterator = categoryList.listIterator();
            List<String> categoryResult = new ArrayList<String>();
            while(iterator.hasNext()){
                RestaurantCategoryEntity restaurantCategoryEntity  = new RestaurantCategoryEntity();
                restaurantCategoryEntity = iterator.next();
                String categoryName = restaurantCategoryEntity.getCategory().getCategoryName();
                if(!categoryName.isEmpty() && categoryName!=null){
                    categoryResult.add(restaurantCategoryEntity.getCategory().getCategoryName());
                }
            }

            Collections.sort(categoryResult);
            String categoryResultDelimeter = String.join(",", categoryResult);


            RestaurantList restaurantList = new RestaurantList()
                    .id(UUID.fromString(restaurantEntity.getUuid()))
                    .restaurantName(restaurantEntity.getRestaurantName())
                    .photoURL(restaurantEntity.getPhotoUrl())
                    .numberCustomersRated(restaurantEntity.getNumberOfCustomersRated())
                    .customerRating(restaurantEntity.getCustomerRating())
                    .averagePrice(restaurantEntity.getAveragePriceForTwo())
                    .address(address)
                    .categories(categoryResultDelimeter);


            restaurantLists.add(restaurantList); //adding the each entity to restaurantLists
        }
        restaurantListResponse.restaurants(restaurantLists);

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);

    }
}