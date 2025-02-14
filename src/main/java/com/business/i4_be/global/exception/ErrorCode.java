package com.business.i4_be.global.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  /**
   * domain 별로
   */
  USER_NOT_FOUND(NOT_FOUND.value(), "유저가 존재하지 않습니다.");

  private final int status;
  private final String message;
}
