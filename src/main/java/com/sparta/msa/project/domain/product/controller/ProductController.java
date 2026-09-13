package com.sparta.msa.project.domain.product.controller;

import com.sparta.msa.project.domain.product.dto.request.ProductSearchRequest;
import com.sparta.msa.project.domain.product.dto.response.ProductDetailResponse;
import com.sparta.msa.project.domain.product.dto.response.ProductSummaryResponse;
import com.sparta.msa.project.domain.product.service.ProductService;
import com.sparta.msa.project.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product", description = "사용자 상품 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  @Operation(summary = "상품 목록 검색",
      description = "카테고리/가격대 필터, 정렬(sortBy=price,asc | createdAt,desc), 페이징을 지원합니다.")
  @GetMapping
  public ApiResponse<Page<ProductSummaryResponse>> searchProducts(
      @ParameterObject @Valid ProductSearchRequest request) {
    return ApiResponse.ok(productService.searchProducts(request));
  }

  @Operation(summary = "상품 상세 조회")
  @GetMapping("/{productId}")
  public ApiResponse<ProductDetailResponse> getProduct(@PathVariable Long productId) {
    return ApiResponse.ok(productService.getProduct(productId));
  }
}
