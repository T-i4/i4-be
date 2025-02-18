package com.business.i4_be.domain.product.entity;

import com.business.i4_be.domain.product.constants.ProductStatus;
import com.business.i4_be.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_products")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "uuid", nullable = false, updatable = false)
  private UUID productId;

  @Column(nullable = false)
  private String productName;

  private Integer quantity;

  @Column(nullable = false)
  private Integer price;

  private String text;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ProductStatus status;

  private String image;
}
