package com.business.i4_be.domain.user.dto.request;

import com.business.i4_be.domain.user.entity.Address;
import com.business.i4_be.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    private String address;

    // ✅ Address 엔티티로 변환하는 메서드 (주소 추가 시 사용)
    public Address toEntity(User user) {
        return Address.builder()
                .user(user) // 요청한 유저를 엔티티에 저장
                .address(this.address)
                .build();
    }
}

