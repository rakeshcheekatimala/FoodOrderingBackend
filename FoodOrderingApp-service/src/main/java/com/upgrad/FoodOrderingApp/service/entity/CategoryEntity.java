package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@NamedQueries({
        @NamedQuery(name = "CategoryEntity.byAll", query = "SELECT g FROM CategoryEntity g order by g.categoryName"),
        @NamedQuery(name = "CategoryEntity.byUUid", query = "SELECT g FROM CategoryEntity g where g.uuid=:uuid"),
        @NamedQuery(name = "CategoryEntity.getAllCategoriesOrderedByName", query = "SELECT g FROM CategoryEntity g order by g.categoryName")
})
public class CategoryEntity implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @OneToMany(mappedBy="category")
    private List<CategoryItemEntity> categoryItems;

    //bi-directional many-to-one association to RestaurantCategory
    @OneToMany(mappedBy="category")
    private List<RestaurantCategoryEntity> restaurantCategories;

    // relation to send all the entities related to category
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<ItemEntity> items =new ArrayList<>();

    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<CategoryItemEntity> getCategoryItems() {
        return categoryItems;
    }

    public void setCategoryItems(List<CategoryItemEntity> categoryItems) {
        this.categoryItems = categoryItems;
    }

    public List<RestaurantCategoryEntity> getRestaurantCategories() {
        return restaurantCategories;
    }

    public void setRestaurantCategories(List<RestaurantCategoryEntity> restaurantCategories) {
        this.restaurantCategories = restaurantCategories;
    }
}
