package com.sparta.msa.project.domain.product.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.msa.project.domain.product.entity.ProductStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponse {

  Long id;

  String name;

  String description;

  int price;

  int stock;

  ProductStatus status;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  LocalDateTime createdAt;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  LocalDateTime updatedAt;

  CategoryInfo category;

  List<ProductOptionResponse> options;

  @Getter
  @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class CategoryInfo {

    Long id;

    String name;
  }
}
