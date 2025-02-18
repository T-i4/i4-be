package com.business.i4_be.domain.store.dto;

import com.business.i4_be.domain.store.constant.StoreCategory;
import com.business.i4_be.domain.store.constant.StoreIsOpen;
import com.business.i4_be.domain.store.entity.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class StoreResDto {

    private UUID storeId;
    private String storeName;
    private String storeNumber;
    private String storeAddress;
    private String storeDetail;
    private String openTime;
    private String closedTime;
    private StoreCategory category;
    private StoreIsOpen isOpen;

    @Builder
    public StoreResDto(UUID storeId, String storeName, String storeNumber, String storeAddress,
                       String storeDetail, String openTime, String closedTime,
                       StoreCategory category, StoreIsOpen isOpen) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeNumber = storeNumber;
        this.storeAddress = storeAddress;
        this.storeDetail = storeDetail;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.category = category;
        this.isOpen = isOpen;
    }

    /**
     *  Store 엔티티를 StoreResDto로 변환하는 정적 메서드
     */
    public static StoreResDto fromEntity(Store store) {
        return StoreResDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .storeNumber(store.getStoreNumber())
                .storeAddress(store.getStoreAddress())
                .storeDetail(store.getStoreDetail())
                .openTime(store.getOpenTime().toString())
                .closedTime(store.getClosedTime().toString())
                .category(store.getCategory())
                .isOpen(store.getIsOpen())
                .build();
    }

    /**
     * Store 엔티티 리스트를 StoreResDto 리스트로 변환하는 정적 메서드
     */
    public static List<StoreResDto> fromEntityList(List<Store> stores) {
        return stores.stream()
                .map(StoreResDto::fromEntity)
                .collect(Collectors.toList());
    }
}
