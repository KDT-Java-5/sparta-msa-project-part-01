package com.sparta.msa.project.domain.product.controller;

import com.sparta.msa.project.domain.product.dto.request.ProductCreateRequest;
import com.sparta.msa.project.domain.product.dto.request.ProductOptionsUpdateRequest;
import com.sparta.msa.project.domain.product.dto.request.ProductStatusUpdateRequest;
import com.sparta.msa.project.domain.product.dto.request.ProductUpdateRequest;
import com.sparta.msa.project.domain.product.dto.response.ProductCreateResponse;
import com.sparta.msa.project.domain.product.dto.response.ProductDetailResponse;
import com.sparta.msa.project.domain.product.dto.response.ProductOptionsResponse;
import com.sparta.msa.project.domain.product.service.ProductService;
import com.sparta.msa.project.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Product", description = "관리자 상품 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class AdminProductController {

  private final ProductService productService;

  @Operation(summary = "상품 등록", description = "상품 옵션을 함께 등록할 수 있습니다.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<ProductCreateResponse> createProduct(
      @Valid @RequestBody ProductCreateRequest request) {
    return ApiResponse.ok(productService.createProduct(request));
  }

  @Operation(summary = "상품 수정")
  @PutMapping("/{productId}")
  public ApiResponse<ProductDetailResponse> updateProduct(
      @PathVariable Long productId,
      @Valid @RequestBody ProductUpdateRequest request) {
    return ApiResponse.ok(productService.updateProduct(productId, request));
  }

  @Operation(summary = "상품 옵션 전체 교체",
      description = "id 있음: 수정 / id 없음: 추가 / 요청에 없는 기존 옵션: 삭제")
  @PutMapping("/{productId}/options")
  public ApiResponse<ProductOptionsResponse> updateProductOptions(
      @PathVariable Long productId,
      @Valid @RequestBody ProductOptionsUpdateRequest request) {
    return ApiResponse.ok(productService.updateProductOptions(productId, request));
  }

  @Operation(summary = "상품 판매 상태 변경", description = "FOR_SALE, STOP_SALE, OUT_OF_STOCK")
  @PatchMapping("/{productId}/status")
  public ApiResponse<ProductDetailResponse> changeProductStatus(
      @PathVariable Long productId,
      @Valid @RequestBody ProductStatusUpdateRequest request) {
    return ApiResponse.ok(productService.changeProductStatus(productId, request));
  }

  @Operation(summary = "상품 삭제", description = "상품에 속한 옵션도 함께 삭제됩니다.")
  @DeleteMapping("/{productId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteProduct(@PathVariable Long productId) {
    productService.deleteProduct(productId);
  }
}
