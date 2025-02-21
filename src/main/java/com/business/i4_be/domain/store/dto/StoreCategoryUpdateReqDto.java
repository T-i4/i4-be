package com.business.i4_be.domain.store.dto;

import com.business.i4_be.domain.store.constant.StoreCategory;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreCategoryUpdateReqDto {

    private UUID storeId;

    @NotNull(message = "카테고리는 필수입니다.")
    private StoreCategory category;
}
