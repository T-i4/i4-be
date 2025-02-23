package com.business.i4_be.domain.store.dto;

import com.business.i4_be.domain.store.constant.StoreCategory;
import com.business.i4_be.domain.store.constant.StoreIsOpen;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreResDto {

    private UUID storeId;
    private Long userId;

    @Valid
    private StoreDto storeDto;

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class StoreDto {

        @NotBlank(message = "가게 이름은 필수입니다.")
        private String storeName;

        @NotBlank(message = "가게 전화번호는 필수입니다.")
        @Pattern(
                regexp = "^(\\d{2,3})-(\\d{3,4})-(\\d{4})$",
                message = "전화번호 형식이 올바르지 않습니다. (예: 02-1234-5678 또는 010-1234-5678)"
        )
        private String storeNumber;

        @NotBlank(message = "가게 주소는 필수입니다.")
        private String storeAddress;

        private String storeDetail;

        @NotBlank(message = "오픈 시간은 필수입니다.")
        private String openTime;

        @NotBlank(message = "닫는 시간은 필수입니다.")
        private String closedTime;

        @NotNull(message = "카테고리는 필수입니다.")
        private StoreCategory category;

        @NotNull(message = "오픈 여부는 필수입니다.")
        private StoreIsOpen isOpen;
    }

    /**
     * 단일 가게 조회를 위한 DTO 변환 메서드
     */
    public static StoreResDto fromEntity(com.business.i4_be.domain.store.entity.Store store) {
        return StoreResDto.builder()
                .storeId(store.getStoreId())
                .userId(store.getUser() != null ? store.getUser().getId() : null)
                .storeDto(StoreDto.builder()
                        .storeName(store.getStoreName())
                        .storeNumber(store.getStoreNumber())
                        .storeAddress(store.getStoreAddress())
                        .storeDetail(store.getStoreDetail())
                        .openTime(store.getOpenTime().toString())
                        .closedTime(store.getClosedTime().toString())
                        .category(store.getCategory())
                        .isOpen(store.getIsOpen())
                        .build())
                .build();
    }

    /**
     * 여러 개의 가게 리스트를 응답하는 DTO
     */
    @Getter
    public static class StoreListResDto {

        private final int totalCount;
        private final List<StoreResDto> stores;

        public StoreListResDto(int totalCount, List<StoreResDto> stores) {
            this.totalCount = totalCount;
            this.stores = stores;
        }

        /**
         * 엔티티 리스트를 DTO 리스트로 변환
         */
        public static StoreListResDto fromEntityList(List<com.business.i4_be.domain.store.entity.Store> storeEntities) {
            List<StoreResDto> storeDtos = storeEntities.stream()
                    .map(StoreResDto::fromEntity)
                    .collect(Collectors.toList());

            return new StoreListResDto(storeDtos.size(), storeDtos);
        }
    }
}