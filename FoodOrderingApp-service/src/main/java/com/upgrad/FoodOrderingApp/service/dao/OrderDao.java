package com.upgrad.FoodOrderingApp.service.dao;


import com.upgrad.FoodOrderingApp.service.entity.*;
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

    public List<OrderItemEntity> getOrderDetails(ArrayList<Long> ordersid) {
        try {
            return em.createNamedQuery("getOrderDetails", OrderItemEntity.class).setParameter("ordersid", ordersid).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<OrdersEntity> getOrders(String id) {

        try {
            return em.createNamedQuery("getOrders", OrdersEntity.class).setParameter("custid", id).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public AddressEntity getAddressByUUID(String uuid) {

        try {
            return em.createNamedQuery("getAddressByuuid", AddressEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public RestaurantEntity getRestaurantByUUID(String uuid) {
        try {
            return em.createNamedQuery("getRestaurantByuuid", RestaurantEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntities) {
        em.persist(orderItemEntities);
        return orderItemEntities;
    }

    public OrdersEntity saveOrder(OrdersEntity orderEntity) {
        em.persist(orderEntity);
        return orderEntity;
    }


    public ItemEntity getItemByuuid(String uuid) {
        try {
            return em.createNamedQuery("getItemByuuid", ItemEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
