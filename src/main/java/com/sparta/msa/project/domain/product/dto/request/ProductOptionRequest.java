package com.sparta.msa.project.domain.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductOptionRequest {

  Long id;

  @NotBlank(message = "옵션명은 필수입니다.")
  @Size(max = 50, message = "옵션명은 50자 이하여야 합니다.")
  String name;

  @NotNull(message = "옵션 추가 금액은 필수입니다.")
  @PositiveOrZero(message = "옵션 추가 금액은 0 이상이어야 합니다.")
  Integer additionalPrice;

  @NotNull(message = "옵션 재고는 필수입니다.")
  @PositiveOrZero(message = "옵션 재고는 0 이상이어야 합니다.")
  Integer stock;
}
