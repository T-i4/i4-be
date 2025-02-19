package com.business.i4_be.domain.review.entity;

import com.business.i4_be.domain.order.entity.Order;
import com.business.i4_be.domain.store.entity.Store;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@SQLRestriction("deleted_at IS NULL")
@Table(name = "p_reviews")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name ="order_id",nullable = false)
    private Order order;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "text", length = 255)
    private String text;



    public static Review createReview(Store store, Long userId, String text, int rating) {
        return Review.builder()
                .store(store)
                .text(text)
                .rating(rating)
                .build();
    }

    public void updateReview(String text, int rating) {
        this.text = text;
        this.rating = rating;
    }
}
