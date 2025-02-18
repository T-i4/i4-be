package com.business.i4_be.domain.store.dto;

import com.business.i4_be.domain.store.constant.StoreCategory;
import com.business.i4_be.domain.store.constant.StoreIsOpen;
import com.business.i4_be.domain.store.entity.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class StoreReqDto {

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

    @NotNull(message = "오픈여부는 필수입니다.")
    private StoreIsOpen isOpen;

    @Builder
    public StoreReqDto(String storeName, String storeNumber, String storeAddress,
                           String storeDetail, String openTime, String closedTime, StoreCategory category, StoreIsOpen isOpen) {
        this.storeName = storeName;
        this.storeNumber = storeNumber;
        this.storeAddress = storeAddress;
        this.storeDetail = storeDetail;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.category = category;
        this.isOpen= isOpen;
    }
    public LocalTime getOpenTimeAsLocalTime() {
        return LocalTime.parse(this.openTime);
    }

    public LocalTime getClosedTimeAsLocalTime() {
        return LocalTime.parse(this.closedTime);
    }
    //Entity 변환 메소드
    public Store toEntity() {
        return Store.builder()
                .storeName(this.storeName)
                .storeNumber(this.storeNumber)
                .storeAddress(this.storeAddress)
                .storeDetail(this.storeDetail)
                .openTime(this.getOpenTimeAsLocalTime())
                .closedTime(this.getClosedTimeAsLocalTime())
                .category(this.category)
                .isOpen(this.isOpen)
                .build();
    }

}
