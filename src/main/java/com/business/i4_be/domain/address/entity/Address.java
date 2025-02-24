package com.business.i4_be.domain.address.entity;

import com.business.i4_be.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p_address")
public class Address {

    @Id
    @GeneratedValue
    private UUID addressId;

    @Column(nullable = false)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public void updateAddress(String newAddress) {
        this.address = newAddress;
    }
}
