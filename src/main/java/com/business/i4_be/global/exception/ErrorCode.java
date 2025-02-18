package com.business.i4_be.global.exception;

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
  USER_NOT_FOUND(1000, NOT_FOUND.value(), "유저가 존재하지 않습니다.");

  private final int code; // 도메인 관리 코드
  private final int status;
  private final String message;
}
