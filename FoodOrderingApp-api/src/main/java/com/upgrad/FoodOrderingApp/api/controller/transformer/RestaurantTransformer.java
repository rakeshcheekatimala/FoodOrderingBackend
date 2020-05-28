package com.upgrad.FoodOrderingApp.api.controller.transformer;

import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponseAddressState;
import com.upgrad.FoodOrderingApp.api.model.RestaurantList;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

final public class RestaurantTransformer {
    public static RestaurantListResponse toRestuarantListResponse(List<RestaurantEntity> restaurants){
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

        return restaurantListResponse;
    }
}
