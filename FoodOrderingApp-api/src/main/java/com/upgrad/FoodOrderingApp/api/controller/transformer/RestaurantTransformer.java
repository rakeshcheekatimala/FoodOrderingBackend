package com.upgrad.FoodOrderingApp.api.controller.transformer;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import java.util.*;

final public class RestaurantTransformer {

    public static RestaurantListResponse toRestuarantListResponse(List<RestaurantEntity> restaurants){
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        List<RestaurantList> restaurantLists = new ArrayList<>();
        for (RestaurantEntity restaurantEntity : restaurants) {
            RestaurantDetailsResponseAddress address = setDetailsAddress(restaurantEntity);
            List categoryList = restaurantEntity.getRestaurantCategories();
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
            String categoryResultDelimiter = String.join(",", categoryResult);

            RestaurantList restaurantList = new RestaurantList()
                    .id(UUID.fromString(restaurantEntity.getUuid()))
                    .restaurantName(restaurantEntity.getRestaurantName())
                    .photoURL(restaurantEntity.getPhotoUrl())
                    .numberCustomersRated(restaurantEntity.getNumberOfCustomersRated())
                    .customerRating(restaurantEntity.getCustomerRating())
                    .averagePrice(restaurantEntity.getAveragePriceForTwo())
                    .address(address)
                    .categories(categoryResultDelimiter);


            restaurantLists.add(restaurantList); //adding the each entity to restaurantLists
        }
        restaurantListResponse.restaurants(restaurantLists);

        return restaurantListResponse;
    }

    public  static RestaurantDetailsResponse toResponseEntityWithCategoryItems(RestaurantEntity restaurantEntity){
        RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse();
        RestaurantDetailsResponseAddress address = setDetailsAddress(restaurantEntity);
        restaurantDetailsResponse.address(address);
        List<CategoryList> categoryLists = new ArrayList();
        for (RestaurantCategoryEntity restaurantCategoryEntity :restaurantEntity.getRestaurantCategories()) {
            CategoryList categoryListDetail = new CategoryList();
            categoryListDetail.setId(UUID.fromString(restaurantCategoryEntity.getCategory().getUuid()));
            categoryListDetail.setCategoryName(restaurantCategoryEntity.getCategory().getCategoryName());
            List<ItemList> itemLists = new ArrayList();
            for (ItemEntity itemEntity :restaurantCategoryEntity.getCategory().getItems()) {
                ItemList itemDetail = new ItemList();
                itemDetail.setId(UUID.fromString(itemEntity.getUuid()));
                itemDetail.setItemName(itemEntity.getItemName());
                itemDetail.setPrice(itemEntity.getPrice());
                itemDetail.setItemType(ItemList.ItemTypeEnum.valueOf(itemEntity.getType().getValue()));
                itemLists.add(itemDetail);
            }
            categoryListDetail.setItemList(itemLists);
            categoryLists.add(categoryListDetail);
        }
        restaurantDetailsResponse.setId(UUID.fromString(restaurantEntity.getUuid()));
        restaurantDetailsResponse.setRestaurantName(restaurantEntity.getRestaurantName());
        restaurantDetailsResponse.setPhotoURL(restaurantEntity.getPhotoUrl());
        restaurantDetailsResponse.setCustomerRating(restaurantEntity.getCustomerRating());
        restaurantDetailsResponse.setAveragePrice(restaurantEntity.getAveragePriceForTwo());
        restaurantDetailsResponse.setNumberCustomersRated(restaurantEntity.getNumberOfCustomersRated());
        restaurantDetailsResponse.address(address);
        restaurantDetailsResponse.setCategories(categoryLists);
        return restaurantDetailsResponse;
    }

    public  static RestaurantDetailsResponseAddress setDetailsAddress(RestaurantEntity restaurantEntity){
        RestaurantDetailsResponseAddress addressDetails = new RestaurantDetailsResponseAddress();
        addressDetails.setCity(restaurantEntity.getAddress().getCity());
        addressDetails.setFlatBuildingName(restaurantEntity.getAddress().getFlatBuildingNumber());
        addressDetails.setCity(restaurantEntity.getAddress().getCity());
        addressDetails.setLocality(restaurantEntity.getAddress().getLocality());
        addressDetails.setPincode(restaurantEntity.getAddress().getPincode());
        addressDetails.setId(UUID.fromString(restaurantEntity.getAddress().getUuid()));

        //creating the state object to store state details
        RestaurantDetailsResponseAddressState state =  new RestaurantDetailsResponseAddressState();
        if(restaurantEntity.getAddress()!=null){
            state.setStateName(restaurantEntity.getAddress().getState().getStateName());
            state.setId(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()));
            addressDetails.setState(state); // adding the state to address object
        }
        return addressDetails;
    }
}
