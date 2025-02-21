package com.business.i4_be.domain.cart.entity;

import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.global.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_carts")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Cart extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(columnDefinition = "uuid", nullable = false, updatable = false)
  private UUID cartId;

  @Column(nullable = false)
  private Integer totalPrice;

  private UUID storeId;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "cart")
  @Builder.Default private List<ProductCart> productCarts = new ArrayList<>();

  public Integer updateTotalPrice(Integer price) {
    this.totalPrice += price;
    return totalPrice;
  }

  public void updateStoreId(UUID storeId) {
    this.storeId = storeId;
  }
}
