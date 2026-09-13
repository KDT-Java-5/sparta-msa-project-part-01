package com.sparta.msa.project.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

// Query Parameter 바인딩(@ModelAttribute)을 위해 Setter를 둔다.
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSearchRequest {

  @Schema(description = "카테고리 ID")
  Long category;

  @Schema(description = "최소 가격")
  @PositiveOrZero(message = "최소 가격은 0 이상이어야 합니다.")
  Integer minPrice;

  @Schema(description = "최대 가격")
  @PositiveOrZero(message = "최대 가격은 0 이상이어야 합니다.")
  Integer maxPrice;

  @Schema(description = "페이지 번호 (0부터 시작)", defaultValue = "0")
  @PositiveOrZero(message = "페이지 번호는 0 이상이어야 합니다.")
  Integer page;

  @Schema(description = "페이지 크기", defaultValue = "10")
  @Min(value = 1, message = "페이지 크기는 1 이상이어야 합니다.")
  @Max(value = 100, message = "페이지 크기는 100 이하여야 합니다.")
  Integer size;

  @Schema(description = "정렬 기준 (price,asc | createdAt,desc)", defaultValue = "createdAt,desc")
  String sortBy;
}
