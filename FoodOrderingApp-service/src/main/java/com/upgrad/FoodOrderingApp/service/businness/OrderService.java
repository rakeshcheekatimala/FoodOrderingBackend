package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;
    @Autowired
    CouponDao couponDao;

    @Autowired
    PaymentDao paymentDao;

    public List<OrderItemEntity> getPastOrders(ArrayList<Long> ordersid) {
        return orderDao.getOrderDetails(ordersid);
    }

    public List<OrdersEntity> getOrdersByCustomers(String id) {
        return orderDao.getOrders(id);
    }

    public AddressEntity getAddressByUUID(String uuid) {
        return orderDao.getAddressByUUID(uuid);
    }



    public RestaurantEntity getRestaurantByUUID(String uuid) {
        return orderDao.getRestaurantByUUID(uuid);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OrderItemEntity saveOrderItem(OrderItemEntity orderItemEntity) {
        return orderDao.saveOrderItem(orderItemEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OrdersEntity saveOrder(OrdersEntity orderEntity) {
        return orderDao.saveOrder(orderEntity);
    }


    public ItemEntity getItemByuuid(String uuid) {
        return orderDao.getItemByuuid(uuid);
    }

    public CouponEntity getCouponByCouponId(String uuid) {
        return couponDao.getCouponByUUID(uuid);
    }
    public CouponEntity getCouponByCouponName(String couponName) {
        return couponDao.getCouponByName(couponName);
    }

}
