package com.sparta.msa.project.domain.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryRequest {

  @NotBlank(message = "카테고리명은 필수입니다.")
  @Size(max = 50, message = "카테고리명은 50자 이하여야 합니다.")
  String name;

  @Size(max = 255, message = "카테고리 설명은 255자 이하여야 합니다.")
  String description;

  Long parentId;
}
