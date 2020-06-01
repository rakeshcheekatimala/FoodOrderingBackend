package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class OrdersDao {
    @Autowired
    EntityManager entityManager;

    public List<OrdersEntity> getAllOrders(String restaurantUUID){
        try{
            return  entityManager.createNamedQuery("OrdersEntity.getAllOrdersByRestaurantId",OrdersEntity.class).setParameter("restaurantUUID",restaurantUUID).getResultList();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<OrderItemEntity> getAllOrdersUsingIdList(List orderIds) {
        try{
            return  entityManager.createNamedQuery("OrderItemEntity.getAllOrdersList",OrderItemEntity.class).setParameter("ordersId",orderIds).getResultList();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public List<OrderItemEntity> getAllItemsUsingItemIdsList(List itemIds) {
        try{
            return  entityManager.createNamedQuery("OrderItemEntity.getAllOrdersListByItemIds",OrderItemEntity.class).setParameter("itemIds",itemIds).getResultList();
        }
        catch (NoResultException nre){
            return null;
        }
    }
}
