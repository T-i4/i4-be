package com.business.i4_be.domain.review.entity;

import com.business.i4_be.domain.store.entity.Store;
import com.business.i4_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "p_reviews")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;


    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "text", length = 255)
    private String text;
}
