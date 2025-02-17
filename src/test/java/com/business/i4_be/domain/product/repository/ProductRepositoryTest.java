package com.business.i4_be.domain.product.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.business.i4_be.domain.product.constants.ProductStatus;
import com.business.i4_be.domain.product.entity.Product;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductRepositoryTest {

  @Autowired
  ProductRepository productRepository;

  Product product;

  @Test
  @DisplayName("Product Save 성공")
  @Rollback(false)
  void product_save() {
    //when
    product = makeProduct();

    Product saveProduct = productRepository.save(product);
    Optional<Product> getProduct = productRepository.findByProductId(product.getProductId());

    //then
    assertThat(getProduct).isPresent();
    assertEquals(saveProduct.getProductId(), getProduct.get().getProductId());
    assertEquals(saveProduct.getProductName(), getProduct.get().getProductName());
    assertEquals(saveProduct.getQuantity(), getProduct.get().getQuantity());
    assertEquals(saveProduct.getPrice(), getProduct.get().getPrice());
    assertEquals(saveProduct.getText(), getProduct.get().getText());
    assertEquals(saveProduct.getStatus(), getProduct.get().getStatus());
  }

  private Product makeProduct() {
    return Product.builder()
        .productName("Test Product")
        .quantity(100)
        .price(1000)
        .text("테스트 상품입니다.")
        .status(ProductStatus.SALE)
        .build();
  }
}