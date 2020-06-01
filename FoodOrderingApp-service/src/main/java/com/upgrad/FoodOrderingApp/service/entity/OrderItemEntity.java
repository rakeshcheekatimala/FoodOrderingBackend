package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "order_item")
@NamedQueries({
        @NamedQuery(name = "OrderItemEntity.getAllOrdersList", query = "SELECT oi from OrderItemEntity oi where oi.orders.id = :ordersId"),
        @NamedQuery(name = "OrderItemEntity.getAllOrdersListByItemIds", query = "SELECT oi from OrderItemEntity oi where oi.item.id = :itemIds"),
        @NamedQuery(name="getOrderDetails",query="select u from OrderItemEntity u where u.orders.id in :ordersid ")

})
public class OrderItemEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrdersEntity orders;

    @ManyToOne
    @JoinColumn(name = "item_id" , referencedColumnName = "id")
    private ItemEntity item;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name="PRICE")
    private Integer price;
    public OrderItemEntity(){}
    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrdersEntity getOrders() {
        return orders;
    }

    public void setOrders(OrdersEntity orders) {
        this.orders = orders;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
