package com.business.i4_be.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  /**
   * domain 별로 코드 관리
   * Bean Validation 같은 공통 예외는 0
   */
  USER_NOT_FOUND(1000, NOT_FOUND.value(), "유저가 존재하지 않습니다."),
  STORE_NOT_FOUND(2000, NOT_FOUND.value(), "가게가 존재하지 않습니다."),
  INVALID_STORE_STATUS(2000, BAD_REQUEST.value(), "올바르지 않은 가게 상태입니다."),
  INVALID_STORE_CATEGORY(2000, BAD_REQUEST.value(), "올바르지 않은 카테고리입니다." ),
  INVALID_CATEGORY(2000, BAD_REQUEST.value(), "유효하지 않은 카테고리입니다." ),
  DUPLICATE_STORE_NAME(2000, BAD_REQUEST.value(), "이미 존재하는 가게 이름입니다." );


  private final int code; // 도메인 관리 코드
  private final int status;
  private final String message;
}
