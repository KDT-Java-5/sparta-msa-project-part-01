package com.sparta.msa.project.domain.product.repository;

import com.sparta.msa.project.domain.product.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

  boolean existsByCategoryId(Long categoryId);

  // 상세 조회 시 카테고리와 옵션을 한 번의 쿼리로 가져온다.
  @Query("""
      select p from Product p
      join fetch p.category
      left join fetch p.options
      where p.id = :productId
      """)
  Optional<Product> findDetailById(@Param("productId") Long productId);
}
