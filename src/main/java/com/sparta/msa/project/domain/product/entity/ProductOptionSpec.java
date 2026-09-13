package com.sparta.msa.project.domain.product.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * 옵션 전체 교체 시 사용하는 옵션 값. id가 null이면 신규 옵션, 있으면 기존 옵션 수정을 의미한다.
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductOptionSpec {

  Long id;

  String name;

  int additionalPrice;

  int stock;
}
