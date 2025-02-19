package com.business.i4_be.domain.cart.dto;

import com.business.i4_be.domain.cart.entity.ProductCart;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartResDto {

  private Long userId;
  private UUID cartId;
  private List<Product> products;

  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  private static class Product {
    private UUID productId;
    private String productName;
    private Integer quantity;
    private Integer price;
    private String image;

    public static Product from(ProductCart productCart) {
      return Product.builder()
          .productId(productCart.getProduct().getProductId())
          .productName(productCart.getProductName())
          .quantity(productCart.getQuantity())
          .price(productCart.getPrice())
          .image(productCart.getImage())
          .build();
    }
  }

  public static CartResDto from(Long userId, UUID cartId, List<ProductCart> productCarts) {
    List<Product> addProducts = productCarts.stream()
        .map(Product::from).toList();

    return CartResDto.builder()
        .userId(userId)
        .cartId(cartId)
        .products(addProducts)
        .build();
  }
}
