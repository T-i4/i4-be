package com.business.i4_be.domain.product.entity;

import com.business.i4_be.domain.product.constants.ProductStatus;
import com.business.i4_be.domain.store.entity.Store;
import com.business.i4_be.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Table(name = "p_products", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_name", "deleted_at"})
})
@Builder
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "uuid", nullable = false, updatable = false)
  private UUID productId;

  @Column(nullable = false)
  private String productName;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private Integer price;

  private String text;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ProductStatus status;

  private String image;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setStatus(ProductStatus status) {
    this.status = status;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
