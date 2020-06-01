package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDao {

    @PersistenceContext
    EntityManager em;

    public List<OrderItemEntity> getOrderDetails(ArrayList<Long> ordersid){
        try{
            return em.createNamedQuery("getOrderDetails",OrderItemEntity.class).setParameter("ordersid",ordersid).getResultList();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public List<OrdersEntity> getOrders(int id){

        try{
            return em.createNamedQuery("getOrders",OrdersEntity.class).setParameter("custid",id).getResultList();
        }
        catch(NoResultException nre){
            return null;
        }
    }

    public AddressEntity getAddressByUUID(String uuid){

        try{
            return em.createNamedQuery("getAddressByuuid", AddressEntity.class).setParameter("uuid",uuid).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }
    public RestaurantEntity getRestaurantByUUID(String uuid){
        try{
            return em.createNamedQuery("getRestaurantByuuid", RestaurantEntity.class).setParameter("uuid",uuid).getSingleResult();
        }
        catch(NoResultException nre){
            return null;
        }
    }
    public OrderItemEntity saveOrder(OrderItemEntity orderItemEntity)
    {
        em.persist(orderItemEntity);
        return  orderItemEntity;
    }
}
