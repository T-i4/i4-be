package com.business.i4_be.domain.store.constant;

import lombok.Getter;
//
@Getter
public enum StoreCategory {
    한식("Korean"),
    중식("Chinese"),
    분식("Snack"),
    치킨("Chicken"),
    피자("Pizza");

    private final String description;

    StoreCategory(String description) {
        this.description = description;
    }
}
