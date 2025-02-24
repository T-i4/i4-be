package com.business.i4_be.domain.product.service;

import static com.business.i4_be.global.exception.ErrorCode.ALREADY_EXIST_PRODUCT;
import static com.business.i4_be.global.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static com.business.i4_be.global.exception.ErrorCode.STORE_NOT_FOUND;

import com.business.i4_be.domain.product.dto.AddProductReqDto;
import com.business.i4_be.domain.product.dto.AddProductResDto;
import com.business.i4_be.domain.product.dto.PageProductsResDto;
import com.business.i4_be.domain.product.dto.ProductResDto;
import com.business.i4_be.domain.product.dto.ProductsResDto;
import com.business.i4_be.domain.product.dto.UpdateProductReqDto;
import com.business.i4_be.domain.product.dto.UpdateProductResDto;
import com.business.i4_be.domain.product.entity.Product;
import com.business.i4_be.domain.product.repository.ProductRepository;
import com.business.i4_be.domain.store.entity.Store;
import com.business.i4_be.domain.store.repository.StoreRepository;
import com.business.i4_be.global.exception.CustomException;
import com.business.i4_be.global.exception.ErrorCode;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final StoreRepository storeRepository;

  @Transactional
  public AddProductResDto addProduct(Long userId, AddProductReqDto requestDto)
      throws CustomException {
    Store store = storeRepository.findById(requestDto.getStoreId())
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    boolean exist = productRepository.existsByProductName(
        requestDto.getProductDto().getProductName());
    if (exist) {
      throw new CustomException(ALREADY_EXIST_PRODUCT);
    }

    return AddProductResDto.from(
        productRepository.save(AddProductReqDto.toEntity(store, requestDto.getProductDto())));
  }

  @Transactional
  public UpdateProductResDto updateProduct(Long userId, UUID productId, UpdateProductReqDto requestDto) {
    Product product = productRepository.findByProductIdAndStore_storeId(productId, requestDto.getStoreId())
        .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

    boolean exist = productRepository.existsByProductName(requestDto.getProductDto().getProductName());
    if(exist && !product.getProductName().equals(requestDto.getProductDto().getProductName())) {
      throw new CustomException(ALREADY_EXIST_PRODUCT);
    }

    updateProductEntity(requestDto, product);
    return UpdateProductResDto.from(product);
  }

  @Transactional
  public void deleteProduct(Long userId, UUID storeId, UUID productId) {
    Product product = productRepository.findByProductIdAndStore_storeId(productId, storeId)
        .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

    product.delete(String.valueOf(userId));
    log.info("상품 삭제 요청 : {}", userId);
  }

  @Transactional(readOnly = true)
  public ProductResDto getProduct(UUID storeId, UUID productId) {
    Product product = productRepository.findByProductIdAndStore_storeId(productId, storeId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    return ProductResDto.from(storeId, product);
  }

  @Transactional(readOnly = true)
  public PageProductsResDto searchProducts(UUID storeId, String keyword, Pageable pageable) {
    Page<Product> products = productRepository.findByStore_storeIdAndProductNameContaining(storeId,
        keyword, pageable);
    return PageProductsResDto.from(storeId, products);
  }

  @Transactional(readOnly = true)
  public ProductsResDto getProducts(Long userId, UUID storeId) {
    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomException(STORE_NOT_FOUND));

    List<Product> products = productRepository.findByStore(store);
    return ProductsResDto.from(storeId, products);
  }

  private void updateProductEntity(UpdateProductReqDto requestDto, Product product) {
    product.setProductName(requestDto.getProductDto().getProductName());
    product.setQuantity(requestDto.getProductDto().getQuantity());
    product.setPrice(requestDto.getProductDto().getPrice());
    product.setText(requestDto.getProductDto().getText());
    product.setStatus(requestDto.getProductDto().getStatus());
    product.setStatus(requestDto.getProductDto().getStatus());
  }
}
