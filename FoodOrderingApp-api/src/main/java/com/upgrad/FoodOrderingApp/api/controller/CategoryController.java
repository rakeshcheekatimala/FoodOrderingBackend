package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse ;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
@CrossOrigin
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, path = "/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAllCategories() {

        List<CategoryEntity> allCategories = categoryService.getAllCategoriesOrderedByName();

        List<CategoryListResponse> categoriesListResponse = new ArrayList<CategoryListResponse>();

        for (CategoryEntity n: allCategories) {
            CategoryListResponse category = new CategoryListResponse();
            category.setCategoryName(n.getCategoryName());
            category.setId(UUID.fromString(n.getUuid()));
            categoriesListResponse.add(category); // add each category to the List
        }

        // return response entity with categoriesListResponse
        return new ResponseEntity<>(categoriesListResponse, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public ResponseEntity getCategoryById(@PathVariable String category_id) throws CategoryNotFoundException{
        if(category_id == null || category_id.isEmpty()){ //Checking for categoryUuid to be null or empty to throw exception.
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }

        CategoryEntity category = categoryService.getCategoryByUuid(category_id);

        if(category == null){
            throw new CategoryNotFoundException("CNF-002", "No category by this id");
        }

        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse();
        categoryDetailsResponse.setCategoryName(category.getCategoryName());
        categoryDetailsResponse.setId(UUID.fromString(category.getUuid()));
        List<ItemList> itemLists =  new ArrayList<>();

        for(ItemEntity item :category.getItems()){
            ItemList itemList = new ItemList();
            itemList.setId(UUID.fromString(item.getUuid()));
            itemList.setItemName(item.getItemName());
            itemList.setPrice(item.getPrice());
            itemLists.add(itemList);
        }
        categoryDetailsResponse.setItemList(itemLists);
        // return response entity with categoryDetailsResponse
        return new ResponseEntity<>(categoryDetailsResponse, HttpStatus.OK);
    }
}
