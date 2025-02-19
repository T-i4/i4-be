package com.business.i4_be.domain.user.repository;

import com.business.i4_be.domain.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
