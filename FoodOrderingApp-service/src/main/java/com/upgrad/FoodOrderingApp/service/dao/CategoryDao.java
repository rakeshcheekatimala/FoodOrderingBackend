package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryDao {
    @PersistenceContext
    private EntityManager entityManager;


    public CategoryEntity getCategoryByUuid(String uuid) {
        try {
            return entityManager.createNamedQuery("CategoryEntity.byUUid", CategoryEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CategoryEntity> getAllCategoriesOrderedByName() throws NoResultException {
        try {
            return entityManager.createNamedQuery("CategoryEntity.getAllCategoriesOrderedByName", CategoryEntity.class).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
