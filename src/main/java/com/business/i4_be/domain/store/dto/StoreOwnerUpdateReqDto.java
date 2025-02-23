package com.business.i4_be.domain.store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreOwnerUpdateReqDto {

    @NotNull(message = "OWNER ID를 지정해주세요.")
    private Long newUserId;
}
