package com.business.i4_be.domain.store.entity;

import com.business.i4_be.domain.review.entity.Review;
import com.business.i4_be.domain.store.constant.StoreCategory;
import com.business.i4_be.domain.store.constant.StoreIsOpen;
import com.business.i4_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "p_stores")
public class Store extends BaseEntity {
//
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID storeId;

    @Column(name = "store_name", nullable = false, length = 255)
    private String storeName;


    @Column(name = "store_address", nullable = false, length = 255)
    private String storeAddress;

    @Column(name = "store_detail", columnDefinition = "TEXT")
    private String storeDetail;


    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;


    @Column(name = "closed_time", nullable = false)
    private LocalTime closedTime;


    @Column(name = "store_number", nullable = false, length = 255)
    private String storeNumber;


    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private StoreCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "is_open", nullable = false)
    private StoreIsOpen isOpen;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();


    public void updateStoreInfo(String storeNumber, String storeAddress, String storeDetail,
                                LocalTime openTime, LocalTime closedTime,
                                StoreCategory category, StoreIsOpen isOpen) {
        this.storeNumber = storeNumber;
        this.storeAddress = storeAddress;
        this.storeDetail = storeDetail;
        this.openTime = openTime;
        this.closedTime = closedTime;
        this.category = category;
        this.isOpen = isOpen;
    }

    // 도메인 메서드
    public void softDelete() {
        this.isDeleted = true;
    }
    public void updateStatus(StoreIsOpen newStatus) {
        this.isOpen = newStatus;
    }

    public void updateCategory(StoreCategory newCategory) {
        this.category = newCategory;
    }
}
