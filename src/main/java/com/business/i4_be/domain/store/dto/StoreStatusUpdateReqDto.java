package com.business.i4_be.domain.store.dto;

import com.business.i4_be.domain.store.constant.StoreIsOpen;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreStatusUpdateReqDto {

    private UUID storeId;

    @NotNull(message = "오픈 여부는 필수입니다.")
    private StoreIsOpen isOpen;
}
