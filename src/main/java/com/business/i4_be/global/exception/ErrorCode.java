package com.business.i4_be.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  /**
   * domain 별로 코드 관리
   * Bean Validation 같은 공통 예외는 0
   */
  USER_NOT_FOUND(1000, NOT_FOUND.value(), "유저가 존재하지 않습니다."),
  FORBIDDEN(1000, BAD_REQUEST.value(),"접근 금지" ),
  ROLE_UPDATE_NOT_ALLOWED(1000, BAD_REQUEST.value(), "권한은 변경할 수 없습니다."),
  INVALID_REQUEST(1000, BAD_REQUEST.value(), "잘못된 요청입니다."),
  INVALID_ROLE(1000, BAD_REQUEST.value(), "회원가입은 USER 와 OWNER 만 가능합니다."),
  DUPLICATE_USER_NAME(1000, BAD_REQUEST.value(), "이미 존재하는 이름입니다." ),
  INVALID_CREDENTIALS(1000, HttpStatus.BAD_REQUEST.value(), "아이디 또는 비밀번호가 올바르지 않습니다."),
  UNAUTHORIZED_ACTION(1000, HttpStatus.BAD_REQUEST.value(), "권한이 없습니다."),

  /** Store : 2000
   * */
  STORE_NOT_FOUND(2000, NOT_FOUND.value(), "가게가 존재하지 않습니다."),
  INVALID_STORE_STATUS(2000, BAD_REQUEST.value(), "올바르지 않은 가게 상태입니다."),
  INVALID_STORE_CATEGORY(2000, BAD_REQUEST.value(), "올바르지 않은 카테고리입니다." ),
  INVALID_CATEGORY(2000, BAD_REQUEST.value(), "유효하지 않은 카테고리입니다." ),
  DUPLICATE_STORE_NAME(2000, BAD_REQUEST.value(), "이미 존재하는 가게 이름입니다." ),
  STORE_ALREADY_DELETED(2000, NOT_FOUND.value(),"이미 삭제된 가게입니다." ),
  STORE_NOT_FOUND_OR_NO_PERMISSION(2000,BAD_REQUEST.value() , "본인이 소유한 가게가 아닙니다." ),
  NO_STORE_REGISTERED(2000, NOT_FOUND.value(), "등록된 가게가 없습니다." ),

  /** 
   * Order : 4000
   */
  ORDER_NOT_FOUND(4000, NOT_FOUND.value(), "주문을 찾을 수 없습니다."),
  ORDER_NOT_BELONG_TO_USER(4000, BAD_REQUEST.value(), "해당 사용자의 주문이 아닙니다."),
  ADDRESS_NOT_FOUND(4000, NOT_FOUND.value(), "주소를 찾을 수 없습니다."),
  ORDER_NOT_COMPLETED(4000, BAD_REQUEST.value(),"완료되지 않은 주문입니다." ),

  /**
   * Review : 5000번
   * */
  REVIEW_NOT_FOUND(5000, NOT_FOUND.value(), "존재하지 않는 리뷰입니다." ),

  /**
   * Product 3000번
   */
  ALREADY_EXIST_PRODUCT(3000, BAD_REQUEST.value(), "이미 존재하는 상품명입니다."),
  PRODUCT_NOT_FOUND(3000, NOT_FOUND.value(), "상품이 존재하지 않습니다."),

  /**
   * Payment 6000번
   */
  PAYMENT_NOT_FOUND(6000, NOT_FOUND.value(), "결제 정보가 없습니다."),
  ORDER_NOT_FOUND_FOR_PAYMENT(6000, NOT_FOUND.value(), "주문 정보가 없습니다."),

  /**
   * Cart 7000번
   */
  NOT_ENOUGH_QUANTITY(7000, BAD_REQUEST.value(), "상품 수량이 부족합니다."),
  ALREADY_DIFFERENT_STORE_PRODUCT(7000, BAD_REQUEST.value(), "다른 가게 상품이 존재합니다."),

  /**
   * Security 8000번
   */
  ACCESS_DENIED(8000, HttpStatus.FORBIDDEN.value(), "권한이 없습니다.");


  private final int code; // 도메인 관리 코드
  private final int status;
  private final String message;
}
