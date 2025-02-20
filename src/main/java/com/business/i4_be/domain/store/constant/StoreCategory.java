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
    //주어진 카테고리 유효한지 체크하는 메서드
    public static boolean isValidCategory(String category) {
        try {
            StoreCategory.valueOf(category.toUpperCase());  // Enum에 존재하는지 확인
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
