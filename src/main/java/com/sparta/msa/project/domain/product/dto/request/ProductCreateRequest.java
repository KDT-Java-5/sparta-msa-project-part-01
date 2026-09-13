package com.sparta.msa.project.domain.product.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreateRequest {

  @NotBlank(message = "상품명은 필수입니다.")
  @Size(max = 100, message = "상품명은 100자 이하여야 합니다.")
  String name;

  String description;

  @NotNull(message = "가격은 필수입니다.")
  @PositiveOrZero(message = "가격은 0 이상이어야 합니다.")
  Integer price;

  @NotNull(message = "재고는 필수입니다.")
  @PositiveOrZero(message = "재고는 0 이상이어야 합니다.")
  Integer stock;

  @NotNull(message = "카테고리 ID는 필수입니다.")
  Long categoryId;

  List<@Valid @NotNull(message = "옵션 항목은 null일 수 없습니다.") ProductOptionRequest> options;
}
