package com.sparta.msa.project.domain.product.dto.request;

import com.sparta.msa.project.domain.product.entity.ProductStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductStatusUpdateRequest {

  @NotNull(message = "판매 상태는 필수입니다. (FOR_SALE, STOP_SALE, OUT_OF_STOCK)")
  ProductStatus status;
}
