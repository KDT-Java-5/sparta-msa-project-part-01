package com.sparta.msa.project.domain.product.entity;

import com.sparta.msa.project.domain.category.entity.Category;
import com.sparta.msa.project.global.entity.BaseEntity;
import com.sparta.msa.project.global.exception.DomainException;
import com.sparta.msa.project.global.exception.DomainExceptionCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false, length = 100)
  String name;

  @Column(columnDefinition = "TEXT")
  String description;

  @Column(nullable = false)
  int price;

  @Column(nullable = false)
  int stock;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  ProductStatus status;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "category_id", nullable = false)
  Category category;

  // 상품(Aggregate Root)이 옵션의 생명주기를 관리한다.
  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id ASC")
  List<ProductOption> options = new ArrayList<>();

  @Builder
  private Product(String name, String description, int price, int stock, Category category) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.stock = stock;
    this.category = category;
    this.status = ProductStatus.FOR_SALE;
  }

  public List<ProductOption> getOptions() {
    return Collections.unmodifiableList(options);
  }

  public void updateInfo(String name, String description, int price, int stock) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.stock = stock;
  }

  public void changeCategory(Category category) {
    this.category = category;
  }

  public void changeStatus(ProductStatus status) {
    this.status = status;
  }

  public void addOption(ProductOption option) {
    options.add(option);
    option.assignProduct(this);
  }

  /**
   * 옵션 목록을 요청된 상태로 전체 교체한다.
   * <ul>
   *   <li>id 있음 → 기존 옵션 수정 (이 상품 소속이어야 함)</li>
   *   <li>id 없음 → 신규 옵션 추가</li>
   *   <li>요청에 없는 기존 옵션 → 삭제 (orphanRemoval)</li>
   * </ul>
   */
  public void replaceOptions(List<ProductOptionSpec> specs) {
    Map<Long, ProductOption> existing = options.stream()
        .collect(Collectors.toMap(ProductOption::getId, Function.identity()));

    // 변경 전에 먼저 전체를 검증해 부분 반영을 막는다.
    Set<Long> requestedIds = new HashSet<>();
    for (ProductOptionSpec spec : specs) {
      if (spec.getId() == null) {
        continue;
      }
      if (!requestedIds.add(spec.getId())) {
        throw new DomainException(DomainExceptionCode.DUPLICATE_PRODUCT_OPTION_ID);
      }
      if (!existing.containsKey(spec.getId())) {
        throw new DomainException(DomainExceptionCode.PRODUCT_OPTION_NOT_FOUND);
      }
    }

    options.removeIf(option -> !requestedIds.contains(option.getId()));

    for (ProductOptionSpec spec : specs) {
      if (spec.getId() != null) {
        existing.get(spec.getId()).update(spec.getName(), spec.getAdditionalPrice(), spec.getStock());
      } else {
        addOption(ProductOption.builder()
            .name(spec.getName())
            .additionalPrice(spec.getAdditionalPrice())
            .stock(spec.getStock())
            .build());
      }
    }
  }
}
