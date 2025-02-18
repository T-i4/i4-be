package com.business.i4_be.domain.store.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StoreListResDto {
    private final int totalCount;
    private final List<StoreResDto> stores;

    public StoreListResDto(int totalCount, List<StoreResDto> stores) {
        this.totalCount = totalCount;
        this.stores = stores;
    }

    public static StoreListResDto fromEntityList(List<StoreResDto> storeDtos) {
        return new StoreListResDto(storeDtos.size(), storeDtos);
    }
}
