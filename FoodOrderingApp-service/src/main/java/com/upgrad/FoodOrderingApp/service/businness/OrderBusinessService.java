package com.upgrad.FoodOrderingApp.service.businness;

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
public class OrderBusinessService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    PaymentDao paymentDao;

public List<OrderItemEntity> getPastOrders(ArrayList<Long> ordersid){
     return orderDao.getOrderDetails(ordersid);
}

public List<OrdersEntity> getOrders(int id){
    return orderDao.getOrders(id);
}

public AddressEntity getAddressByUUID(String uuid){
    return orderDao.getAddressByUUID(uuid);
}

public PaymentEntity getPaymentByUUID(String uuid){
    return  paymentDao.getPaymentByUUID(uuid);
}

public RestaurantEntity getRestaurantByUUID(String uuid){
    return orderDao.getRestaurantByUUID(uuid);
}

    @Transactional(propagation = Propagation.REQUIRED)
public OrderItemEntity saveOrder(OrderItemEntity orderItemEntity)
{
    return orderDao.saveOrder(orderItemEntity);
}
}
