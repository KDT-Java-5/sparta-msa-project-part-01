package com.sparta.msa.project.domain.product.entity;

import com.sparta.msa.project.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@Entity
@Table(name = "product_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductOption extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // 연관관계의 주인 (FK 보유)
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "product_id", nullable = false)
  Product product;

  @Column(nullable = false, length = 50)
  String name;

  @Column(name = "additional_price", nullable = false)
  int additionalPrice;

  @Column(nullable = false)
  int stock;

  @Builder
  private ProductOption(String name, int additionalPrice, int stock) {
    this.name = name;
    this.additionalPrice = additionalPrice;
    this.stock = stock;
  }

  public void update(String name, int additionalPrice, int stock) {
    this.name = name;
    this.additionalPrice = additionalPrice;
    this.stock = stock;
  }

  // Product.addOption()을 통해서만 호출된다.
  void assignProduct(Product product) {
    this.product = product;
  }
}
