package com.sparta.msa.project.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum DomainExceptionCode {

  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
  EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
  MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 누락되었습니다."),
  UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다."),
  JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Json 데이터 처리 중 에러가 발생하였습니다."),

  // Category
  CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."),
  PARENT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "부모 카테고리를 찾을 수 없습니다."),
  CATEGORY_CIRCULAR_REFERENCE(HttpStatus.BAD_REQUEST, "자기 자신 또는 하위 카테고리를 부모로 지정할 수 없습니다."),
  CATEGORY_HAS_CHILDREN(HttpStatus.CONFLICT, "하위 카테고리가 존재하여 삭제할 수 없습니다."),
  CATEGORY_HAS_PRODUCTS(HttpStatus.CONFLICT, "카테고리에 속한 상품이 존재하여 삭제할 수 없습니다."),

  // Product
  PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다."),
  PRODUCT_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품에 속한 옵션을 찾을 수 없습니다."),
  DUPLICATE_PRODUCT_OPTION_ID(HttpStatus.BAD_REQUEST, "동일한 옵션 ID가 중복으로 요청되었습니다."),
  INVALID_PRICE_RANGE(HttpStatus.BAD_REQUEST, "최소 가격은 최대 가격보다 클 수 없습니다."),
  INVALID_SORT_PARAMETER(HttpStatus.BAD_REQUEST, "정렬 조건이 올바르지 않습니다. (예: price,asc 또는 createdAt,desc)");

  final HttpStatus status;
  final String message;
}
