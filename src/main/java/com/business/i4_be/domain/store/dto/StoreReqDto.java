package com.business.i4_be.domain.store.dto;

import com.business.i4_be.domain.store.constant.StoreCategory;
import com.business.i4_be.domain.store.constant.StoreIsOpen;
import com.business.i4_be.domain.store.entity.Store;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class StoreReqDto {

    /**
     * 가게 생성과 수정 구분을 위한 Validation Group 인터페이스
     */
    public interface Create {}
    public interface Update {}

    private UUID storeId;

    @Valid
    private StoreDto storeDto;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StoreDto {

        @NotBlank(message = "가게 이름은 필수입니다.", groups = Create.class)
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

    public LocalTime getOpenTimeAsLocalTime() {
        return LocalTime.parse(this.storeDto.openTime);
    }

    public LocalTime getClosedTimeAsLocalTime() {
        return LocalTime.parse(this.storeDto.closedTime);
    }

    /**
     * 엔티티로 변환 (가게 생성 시)
     */
    public Store toEntity() {
        return Store.builder()
                .storeName(this.storeDto.storeName)
                .storeNumber(this.storeDto.storeNumber)
                .storeAddress(this.storeDto.storeAddress)
                .storeDetail(this.storeDto.storeDetail)
                .openTime(this.getOpenTimeAsLocalTime())
                .closedTime(this.getClosedTimeAsLocalTime())
                .category(this.storeDto.category)
                .isOpen(this.storeDto.isOpen)
                .build();
    }

    /**
     * 엔티티에 적용 (가게 수정 시)
     */
    public void applyTo(Store store) {
        store.updateStoreInfo(
                this.storeDto.storeNumber,
                this.storeDto.storeAddress,
                this.storeDto.storeDetail,
                this.getOpenTimeAsLocalTime(),
                this.getClosedTimeAsLocalTime(),
                this.storeDto.category,
                this.storeDto.isOpen
        );
    }
}


