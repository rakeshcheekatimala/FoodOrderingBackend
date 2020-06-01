package com.upgrad.FoodOrderingApp.service.common;

public enum ItemType {
    VEG("VEG"),
    NON_VEG("NON_VEG");

    private final String value;

    private ItemType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}


