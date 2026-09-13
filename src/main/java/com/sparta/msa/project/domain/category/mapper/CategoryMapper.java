package com.sparta.msa.project.domain.category.mapper;

import com.sparta.msa.project.domain.category.dto.response.CategoryResponse;
import com.sparta.msa.project.domain.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  @Mapping(target = "parentId", source = "parent.id")
  CategoryResponse toResponse(Category category);
}
