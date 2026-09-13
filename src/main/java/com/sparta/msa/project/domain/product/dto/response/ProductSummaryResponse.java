package com.sparta.msa.project.domain.product.dto.response;

import com.sparta.msa.project.domain.product.entity.ProductStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSummaryResponse {

  Long id;

  String name;

  int price;

  int stock;

  ProductStatus status;
}
