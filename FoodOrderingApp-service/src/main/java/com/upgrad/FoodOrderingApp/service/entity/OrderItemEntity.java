package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@NamedQueries({
        @NamedQuery(name = "OrderItemEntity.getAllOrdersList", query = "SELECT oi from OrderItemEntity oi where oi.orders.id = :ordersId"),
        @NamedQuery(name = "OrderItemEntity.getAllOrdersListByItemIds", query = "SELECT oi from OrderItemEntity oi where oi.item.id = :itemIds")

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
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name="PRICE")
    private Integer price;

    public long getId() {
        return id;
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

    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
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
