package com.example.demo1.entity;

public enum ProductEnum {
    S("Size S"),
    M("Size M"),
    L("Size L"),
    XL("Size XL"),
    XXL("Size XXL");

    private final String displayName;

    ProductEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
