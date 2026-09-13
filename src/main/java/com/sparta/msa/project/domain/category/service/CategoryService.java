package com.sparta.msa.project.domain.category.service;

import com.sparta.msa.project.domain.category.dto.request.CategoryRequest;
import com.sparta.msa.project.domain.category.dto.response.CategoryCreateResponse;
import com.sparta.msa.project.domain.category.dto.response.CategoryResponse;
import com.sparta.msa.project.domain.category.entity.Category;
import com.sparta.msa.project.domain.category.mapper.CategoryMapper;
import com.sparta.msa.project.domain.category.repository.CategoryRepository;
import com.sparta.msa.project.domain.product.repository.ProductRepository;
import com.sparta.msa.project.global.exception.DomainException;
import com.sparta.msa.project.global.exception.DomainExceptionCode;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  private final CategoryMapper categoryMapper;

  @Transactional
  public CategoryCreateResponse createCategory(CategoryRequest request) {
    Category parent = findParent(request.getParentId());

    Category category = Category.builder()
        .name(request.getName())
        .description(request.getDescription())
        .parent(parent)
        .build();

    return new CategoryCreateResponse(categoryRepository.save(category).getId());
  }

  @Transactional
  public CategoryResponse updateCategory(Long categoryId, CategoryRequest request) {
    Category category = findCategory(categoryId);

    if (Objects.equals(categoryId, request.getParentId())) {
      throw new DomainException(DomainExceptionCode.CATEGORY_CIRCULAR_REFERENCE);
    }
    Category parent = findParent(request.getParentId());

    category.updateInfo(request.getName(), request.getDescription());
    category.changeParent(parent);
    categoryRepository.flush();

    return categoryMapper.toResponse(category);
  }

  @Transactional
  public void deleteCategory(Long categoryId) {
    Category category = findCategory(categoryId);

    if (categoryRepository.existsByParentId(categoryId)) {
      throw new DomainException(DomainExceptionCode.CATEGORY_HAS_CHILDREN);
    }
    if (productRepository.existsByCategoryId(categoryId)) {
      throw new DomainException(DomainExceptionCode.CATEGORY_HAS_PRODUCTS);
    }

    categoryRepository.delete(category);
  }

  private Category findCategory(Long categoryId) {
    return categoryRepository.findById(categoryId)
        .orElseThrow(() -> new DomainException(DomainExceptionCode.CATEGORY_NOT_FOUND));
  }

  private Category findParent(Long parentId) {
    if (parentId == null) {
      return null;
    }
    return categoryRepository.findById(parentId)
        .orElseThrow(() -> new DomainException(DomainExceptionCode.PARENT_CATEGORY_NOT_FOUND));
  }
}
