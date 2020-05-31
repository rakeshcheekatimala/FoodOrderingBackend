package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryDao categoryDao;


    public CategoryEntity getCategoryByUuid(String uuid) {
        return  categoryDao.getCategoryByUuid(uuid);
    }

    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        //Calls getAllCategoriesOrderedByName of categoryDao to get list of CategoryEntity
        return categoryDao.getAllCategoriesOrderedByName();
    }

    public CategoryEntity getCategoryById(String id) {
        return  categoryDao.getCategoryByUuid(id);
    }
}
