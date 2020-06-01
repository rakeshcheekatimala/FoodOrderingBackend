package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.ItemDao;
import com.upgrad.FoodOrderingApp.service.dao.OrdersDao;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrdersEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.*;

@Service
public class ItemService {
    @Autowired
    private ItemDao itemDao;

    @Autowired
    OrdersDao ordersDao;

    @Autowired
    EntityManager entityManager;

    public  List<ItemEntity> getItemsByPopularity(RestaurantEntity restaurantEntity) {
        //itemDao.getItemsByPopularity(restaurantEntity);
        // get all the orders using UUID of restaurant
        List<OrdersEntity> ordersEntityList = ordersDao.getAllOrders(restaurantEntity.getUuid());

        List OrderIds = new ArrayList();

        for(OrdersEntity oe:ordersEntityList) {
            OrderIds.add(oe.getId()); // add each order Id to the List
        }
        if(OrderIds.size()==0){
            List<ItemEntity> resultItemList = new ArrayList<ItemEntity>();
            return  resultItemList;
        }
        List<OrderItemEntity> orderItemEntities = ordersDao.getAllOrdersUsingIdList(OrderIds);


        if(orderItemEntities.size()==0){
            List<ItemEntity> resultItemList = new ArrayList<ItemEntity>();
            return  resultItemList;
        }

        List<Long> itemIds = new ArrayList<Long>();

        for(OrderItemEntity oe:orderItemEntities) {
            itemIds.add(oe.getItem().getId());// add the item id ;
        }

        Set<Long> uniqueSet = new HashSet<Long>(itemIds); //  this sorts and has the count
        List<Long> topFiveIds = new ArrayList<>();
        Integer count = 0;

        for (Long itemId : uniqueSet) {
            topFiveIds.add(itemId);
            ++count;
            if(count==5) {
                break;
            }
        }

        List<OrderItemEntity> topFiveItems = ordersDao.getAllOrdersUsingIdList(topFiveIds);
        Map<Long,ItemEntity> topItemEntities = new HashMap<Long,ItemEntity>() ;

        for(OrderItemEntity oe:topFiveItems){
            topItemEntities.put(oe.getItem().getId(),oe.getItem()) ; // get the top item and add to topItemEntities
        }
        List<ItemEntity> resultItemList = new ArrayList<ItemEntity>();
        for (Map.Entry<Long, ItemEntity> entry : topItemEntities.entrySet()) {
            resultItemList.add(entry.getValue());
        }
        return resultItemList;
    }
}
