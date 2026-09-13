package com.sparta.msa.project.domain.product.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductOptionResponse {

  Long id;

  String name;

  int additionalPrice;

  int stock;
}
