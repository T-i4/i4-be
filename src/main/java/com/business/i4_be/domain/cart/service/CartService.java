package com.business.i4_be.domain.cart.service;

import static com.business.i4_be.global.exception.ErrorCode.ALREADY_DIFFERENT_STORE_PRODUCT;
import static com.business.i4_be.global.exception.ErrorCode.NOT_ENOUGH_QUANTITY;
import static com.business.i4_be.global.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static com.business.i4_be.global.exception.ErrorCode.USER_NOT_FOUND;

import com.business.i4_be.domain.cart.dto.AddCartReqDto;
import com.business.i4_be.domain.cart.dto.CartResDto;
import com.business.i4_be.domain.cart.dto.UpdateCartReqDto;
import com.business.i4_be.domain.cart.dto.UpdateCartReqDto.ProductDto;
import com.business.i4_be.domain.cart.entity.Cart;
import com.business.i4_be.domain.cart.entity.ProductCart;
import com.business.i4_be.domain.cart.repository.CartRepository;
import com.business.i4_be.domain.product.entity.Product;
import com.business.i4_be.domain.product.repository.ProductRepository;
import com.business.i4_be.domain.user.entity.User;
import com.business.i4_be.domain.user.repository.UserRepository;
import com.business.i4_be.global.exception.CustomException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

  private final UserRepository userRepository;
  private final CartRepository cartRepository;
  private final ProductRepository productRepository;

  @Transactional
  public CartResDto addCart(Long userId, AddCartReqDto requestDto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    Product product = productRepository.findByProductIdAndStore_storeId(
            requestDto.getProduct().getProductId(), requestDto.getStoreId())
        .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));

    if (product.getQuantity() < requestDto.getProduct().getQuantity()) {
      throw new CustomException(NOT_ENOUGH_QUANTITY);
    }

    Cart cart = getOrDefaultCart(user);
    if (cart.getStoreId() == null) {
      cart.updateStoreId(requestDto.getStoreId());
    } else {
      if (!requestDto.getStoreId().equals(cart.getStoreId())) {
        throw new CustomException(ALREADY_DIFFERENT_STORE_PRODUCT);
      }
    }

    // 장바구니에 같은 상품이 있나 확인
    boolean exist = false;
    if (!cart.getProductCarts().isEmpty()) {
      for (ProductCart productCart : cart.getProductCarts()) {
        if (product.getProductName().equals(productCart.getProductName())) {
          if (productCart.getQuantity() + requestDto.getProduct().getQuantity()
              > product.getQuantity()) {
            throw new CustomException(NOT_ENOUGH_QUANTITY);
          }
          Integer totalQuantity = productCart.updateQuantity(requestDto.getProduct().getQuantity());

          log.info("특정 상품 총 가격 : {}",totalQuantity * product.getPrice());
          productCart.updatePrice(totalQuantity * product.getPrice());
          cart.updateTotalPrice(requestDto.getProduct().getQuantity() * product.getPrice());
          exist = true;
          break;
        }
      }
    }

    if (!exist) { // 같은 상품이 없다.
      ProductCart productCart = ProductCart.builder()
          .productName(product.getProductName())
          .quantity(requestDto.getProduct().getQuantity())
          .price(product.getPrice() * requestDto.getProduct().getQuantity())
          .image(product.getImage())
          .product(product)
          .cart(cart)
          .build();
      cart.getProductCarts().add(productCart);
      cart.updateTotalPrice(productCart.getPrice());
    }

    cartRepository.save(cart);
    return CartResDto.from(userId, cart);
  }


  @Transactional(readOnly = true)
  public CartResDto getCart(Long userId, UUID cartId) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    Cart cart = getOrDefaultCart(user);
    return CartResDto.from(userId, cart);
  }

  @Transactional
  public CartResDto updateCart(Long userId, UUID cartId, UpdateCartReqDto requestDto) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    Cart cart = cartRepository.findByUser(user);
    if (cart != null) {
      if (!requestDto.getStoreId().equals(cart.getStoreId())) {
        throw new CustomException(ALREADY_DIFFERENT_STORE_PRODUCT);
      }
      cart.getProductCarts().clear();
      cart.updateTotalPrice(-cart.getTotalPrice());
    } else {
      cart = Cart.builder()
          .totalPrice(0)
          .user(user)
          .build();
    }

    for (ProductDto product : requestDto.getProducts()) {
      Product getProduct = productRepository.findByProductId(product.getProductId())
          .orElseThrow(() -> new CustomException(PRODUCT_NOT_FOUND));
      if (getProduct.getQuantity() < product.getQuantity()) {
        throw new CustomException(NOT_ENOUGH_QUANTITY);
      }

      ProductCart productCart = makeProductCart(product, getProduct,
          cart);
      cart.getProductCarts().add(productCart);
      cart.updateTotalPrice(productCart.getPrice());
    }
    cartRepository.save(cart);

    return CartResDto.from(userId, cart);
  }

  @Transactional
  public void deleteCart(Long userId, UUID cartId) {
    Cart cart = cartRepository.findByCartIdAndUserId(cartId, userId);
    if (cart != null) {
      cartRepository.delete(cart);
    }
  }

  private Cart getOrDefaultCart(User user) {
    Cart cart = cartRepository.findByUser(user);
    if (cart == null) {
      cart = Cart.builder()
          .totalPrice(0)
          .user(user)
          .build();
    }
    return cart;
  }

  private ProductCart makeProductCart(ProductDto product, Product getProduct, Cart cart) {
    return ProductCart.builder()
        .productName(getProduct.getProductName())
        .quantity(product.getQuantity())
        .price(product.getQuantity() * getProduct.getPrice())
        .image(getProduct.getImage())
        .cart(cart)
        .product(getProduct)
        .build();
  }
}
