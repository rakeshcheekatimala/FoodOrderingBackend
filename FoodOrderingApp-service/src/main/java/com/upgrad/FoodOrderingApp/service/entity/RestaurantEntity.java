package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="restaurant")
@NamedQueries({
        @NamedQuery(name = "RestaurantEntity.findAll", query = "select r from RestaurantEntity r order by r.customerRating desc"),
        @NamedQuery(name = "RestaurantEntity.findByName", query = "select r from RestaurantEntity  r where  lower(r.restaurantName) like CONCAT(:restaurantName,'%')"),
        @NamedQuery(name = "RestaurantEntity.byUUid", query = "SELECT r FROM RestaurantEntity r where r.uuid=:uuid"),
        @NamedQuery(name="getRestaurantByuuid",query="select u from RestaurantEntity  u where u.uuid=:uuid")
})

public class RestaurantEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private long id;

    @Column(name="AVERAGE_PRICE_FOR_TWO")
    private Integer averagePriceForTwo;

    @Column(name = "CUSTOMER_RATING",scale = 2)
    @NotNull
    private BigDecimal customerRating;

    @Column(name = "NUMBER_OF_CUSTOMERS_RATED")
    @NotNull
    private int numberOfCustomersRated;

    @Column(name = "PHOTO_URL")
    @NotNull
    @Size(max = 255)
    private String photoUrl;

    @Column(name = "RESTAURANT_NAME")
    @NotNull
    @Size(max = 50)
    private String restaurantName;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    //bi-directional many-to-one association to Order
    @OneToMany(mappedBy="restaurant")
    private List<OrdersEntity> orders;

    //bi-directional many-to-one association to Address
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="address_id")
    private AddressEntity address;

    //bi-directional many-to-one association to RestaurantCategory
    @OneToMany(mappedBy="restaurant",fetch = FetchType.EAGER)
    private List<RestaurantCategoryEntity> restaurantCategories;

    //bi-directional many-to-one association to RestaurantItem
    @OneToMany(mappedBy="restaurant")
    private List<RestaurantItemEntity> restaurantItems;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getAveragePriceForTwo() {
        return averagePriceForTwo;
    }

    public void setAveragePriceForTwo(Integer averagePriceForTwo) {
        this.averagePriceForTwo = averagePriceForTwo;
    }

    public BigDecimal getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(BigDecimal customerRating) {
        this.customerRating = customerRating;
    }

    public int getNumberOfCustomersRated() {
        return numberOfCustomersRated;
    }

    public void setNumberOfCustomersRated(int numberOfCustomersRated) {
        this.numberOfCustomersRated = numberOfCustomersRated;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<OrdersEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersEntity> orders) {
        this.orders = orders;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public List<RestaurantCategoryEntity> getRestaurantCategories() {
        return restaurantCategories;
    }

    public void setRestaurantCategories(List<RestaurantCategoryEntity> restaurantCategories) {
        this.restaurantCategories = restaurantCategories;
    }

    public List<RestaurantItemEntity> getRestaurantItems() {
        return restaurantItems;
    }

    public void setRestaurantItems(List<RestaurantItemEntity> restaurantItems) {
        this.restaurantItems = restaurantItems;
    }
}