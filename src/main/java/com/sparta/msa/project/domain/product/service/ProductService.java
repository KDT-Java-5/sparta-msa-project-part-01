package com.sparta.msa.project.domain.product.service;

import com.sparta.msa.project.domain.category.entity.Category;
import com.sparta.msa.project.domain.category.repository.CategoryRepository;
import com.sparta.msa.project.domain.product.dto.request.ProductCreateRequest;
import com.sparta.msa.project.domain.product.dto.request.ProductOptionRequest;
import com.sparta.msa.project.domain.product.dto.request.ProductOptionsUpdateRequest;
import com.sparta.msa.project.domain.product.dto.request.ProductSearchRequest;
import com.sparta.msa.project.domain.product.dto.request.ProductStatusUpdateRequest;
import com.sparta.msa.project.domain.product.dto.request.ProductUpdateRequest;
import com.sparta.msa.project.domain.product.dto.response.ProductCreateResponse;
import com.sparta.msa.project.domain.product.dto.response.ProductDetailResponse;
import com.sparta.msa.project.domain.product.dto.response.ProductOptionsResponse;
import com.sparta.msa.project.domain.product.dto.response.ProductSummaryResponse;
import com.sparta.msa.project.domain.product.entity.Product;
import com.sparta.msa.project.domain.product.entity.ProductOption;
import com.sparta.msa.project.domain.product.mapper.ProductMapper;
import com.sparta.msa.project.domain.product.repository.ProductQueryRepository;
import com.sparta.msa.project.domain.product.repository.ProductRepository;
import com.sparta.msa.project.global.exception.DomainException;
import com.sparta.msa.project.global.exception.DomainExceptionCode;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private static final int DEFAULT_PAGE = 0;
  private static final int DEFAULT_SIZE = 10;
  private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "createdAt");
  private static final Set<String> SORTABLE_FIELDS = Set.of("createdAt", "price");

  private final ProductRepository productRepository;
  private final ProductQueryRepository productQueryRepository;
  private final CategoryRepository categoryRepository;
  private final ProductMapper productMapper;

  @Transactional
  public ProductCreateResponse createProduct(ProductCreateRequest request) {
    Category category = findCategory(request.getCategoryId());

    Product product = Product.builder()
        .name(request.getName())
        .description(request.getDescription())
        .price(request.getPrice())
        .stock(request.getStock())
        .category(category)
        .build();

    if (request.getOptions() != null) {
      for (ProductOptionRequest option : request.getOptions()) {
        product.addOption(ProductOption.builder()
            .name(option.getName())
            .additionalPrice(option.getAdditionalPrice())
            .stock(option.getStock())
            .build());
      }
    }

    return new ProductCreateResponse(productRepository.save(product).getId());
  }

  @Transactional
  public ProductDetailResponse updateProduct(Long productId, ProductUpdateRequest request) {
    Product product = findProductDetail(productId);
    Category category = findCategory(request.getCategoryId());

    product.updateInfo(request.getName(), request.getDescription(), request.getPrice(),
        request.getStock());
    product.changeCategory(category);
    productRepository.flush();

    return productMapper.toDetailResponse(product);
  }

  @Transactional
  public ProductOptionsResponse updateProductOptions(Long productId,
      ProductOptionsUpdateRequest request) {
    Product product = findProductDetail(productId);

    product.replaceOptions(productMapper.toOptionSpecs(request.getOptions()));
    // 신규 옵션의 ID를 응답에 담기 위해 flush
    productRepository.flush();

    return productMapper.toOptionsResponse(product);
  }

  @Transactional
  public ProductDetailResponse changeProductStatus(Long productId,
      ProductStatusUpdateRequest request) {
    Product product = findProductDetail(productId);

    product.changeStatus(request.getStatus());
    productRepository.flush();

    return productMapper.toDetailResponse(product);
  }

  @Transactional
  public void deleteProduct(Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new DomainException(DomainExceptionCode.PRODUCT_NOT_FOUND));

    // cascade + orphanRemoval로 옵션도 함께 삭제된다.
    productRepository.delete(product);
  }

  @Transactional(readOnly = true)
  public Page<ProductSummaryResponse> searchProducts(ProductSearchRequest request) {
    validatePriceRange(request.getMinPrice(), request.getMaxPrice());

    return productQueryRepository
        .search(request.getCategory(), request.getMinPrice(), request.getMaxPrice(),
            toPageable(request))
        .map(productMapper::toSummaryResponse);
  }

  @Transactional(readOnly = true)
  public ProductDetailResponse getProduct(Long productId) {
    return productMapper.toDetailResponse(findProductDetail(productId));
  }

  private Product findProductDetail(Long productId) {
    return productRepository.findDetailById(productId)
        .orElseThrow(() -> new DomainException(DomainExceptionCode.PRODUCT_NOT_FOUND));
  }

  private Category findCategory(Long categoryId) {
    return categoryRepository.findById(categoryId)
        .orElseThrow(() -> new DomainException(DomainExceptionCode.CATEGORY_NOT_FOUND));
  }

  private void validatePriceRange(Integer minPrice, Integer maxPrice) {
    if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
      throw new DomainException(DomainExceptionCode.INVALID_PRICE_RANGE);
    }
  }

  private Pageable toPageable(ProductSearchRequest request) {
    int page = Objects.requireNonNullElse(request.getPage(), DEFAULT_PAGE);
    int size = Objects.requireNonNullElse(request.getSize(), DEFAULT_SIZE);
    return PageRequest.of(page, size, parseSort(request.getSortBy()));
  }

  // "price,asc" / "createdAt,desc" 형식. 정렬 방식이 없으면 asc로 처리한다.
  private Sort parseSort(String sortBy) {
    if (sortBy == null || sortBy.isBlank()) {
      return DEFAULT_SORT;
    }

    String[] tokens = sortBy.split(",");
    String field = tokens[0].trim();
    if (tokens.length > 2 || !SORTABLE_FIELDS.contains(field)) {
      throw new DomainException(DomainExceptionCode.INVALID_SORT_PARAMETER);
    }

    Sort.Direction direction = tokens.length == 2
        ? Sort.Direction.fromOptionalString(tokens[1].trim())
            .orElseThrow(() -> new DomainException(DomainExceptionCode.INVALID_SORT_PARAMETER))
        : Sort.Direction.ASC;

    return Sort.by(direction, field);
  }
}
