package com.sparta.msa.project.domain.category.controller;

import com.sparta.msa.project.domain.category.dto.request.CategoryRequest;
import com.sparta.msa.project.domain.category.dto.response.CategoryCreateResponse;
import com.sparta.msa.project.domain.category.dto.response.CategoryResponse;
import com.sparta.msa.project.domain.category.service.CategoryService;
import com.sparta.msa.project.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Category", description = "관리자 카테고리 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

  private final CategoryService categoryService;

  @Operation(summary = "카테고리 등록")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<CategoryCreateResponse> createCategory(
      @Valid @RequestBody CategoryRequest request) {
    return ApiResponse.ok(categoryService.createCategory(request));
  }

  @Operation(summary = "카테고리 수정")
  @PutMapping("/{categoryId}")
  public ApiResponse<CategoryResponse> updateCategory(
      @PathVariable Long categoryId,
      @Valid @RequestBody CategoryRequest request) {
    return ApiResponse.ok(categoryService.updateCategory(categoryId, request));
  }

  @Operation(summary = "카테고리 삭제", description = "하위 카테고리와 소속 상품이 모두 없을 때만 삭제됩니다.")
  @DeleteMapping("/{categoryId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCategory(@PathVariable Long categoryId) {
    categoryService.deleteCategory(categoryId);
  }
}
