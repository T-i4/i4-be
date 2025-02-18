package com.business.i4_be.domain.store.constant;

import lombok.Getter;

@Getter
public enum StoreIsOpen {
    OPEN("영업 중"),
    CLOSE("영업 종료");

    private final String description;

    StoreIsOpen(String description) {
        this.description = description;
    }
}
