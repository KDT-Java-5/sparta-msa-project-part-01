package com.sparta.msa.project.domain.product.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductOptionsUpdateRequest {

  @NotNull(message = "옵션 목록은 필수입니다. 모든 옵션을 삭제하려면 빈 배열을 보내세요.")
  List<@Valid @NotNull(message = "옵션 항목은 null일 수 없습니다.") ProductOptionRequest> options;
}
