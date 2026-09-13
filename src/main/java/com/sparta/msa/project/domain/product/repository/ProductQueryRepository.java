package com.sparta.msa.project.domain.product.repository;

import static com.sparta.msa.project.domain.product.entity.QProduct.product;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa.project.domain.product.entity.Product;
import com.sparta.msa.project.global.exception.DomainException;
import com.sparta.msa.project.global.exception.DomainExceptionCode;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

  private final JPAQueryFactory queryFactory;

  public Page<Product> search(Long categoryId, Integer minPrice, Integer maxPrice,
      Pageable pageable) {
    List<Product> content = queryFactory
        .selectFrom(product)
        .where(
            categoryIdEq(categoryId),
            priceGoe(minPrice),
            priceLoe(maxPrice)
        )
        .orderBy(toOrderSpecifiers(pageable.getSort()))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    JPAQuery<Long> countQuery = queryFactory
        .select(product.count())
        .from(product)
        .where(
            categoryIdEq(categoryId),
            priceGoe(minPrice),
            priceLoe(maxPrice)
        );

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  private BooleanExpression categoryIdEq(Long categoryId) {
    return categoryId != null ? product.category.id.eq(categoryId) : null;
  }

  private BooleanExpression priceGoe(Integer minPrice) {
    return minPrice != null ? product.price.goe(minPrice) : null;
  }

  private BooleanExpression priceLoe(Integer maxPrice) {
    return maxPrice != null ? product.price.loe(maxPrice) : null;
  }

  private OrderSpecifier<?>[] toOrderSpecifiers(Sort sort) {
    List<OrderSpecifier<?>> orders = new ArrayList<>();

    for (Sort.Order order : sort) {
      Order direction = order.isAscending() ? Order.ASC : Order.DESC;
      switch (order.getProperty()) {
        case "price" -> orders.add(new OrderSpecifier<>(direction, product.price));
        case "createdAt" -> orders.add(new OrderSpecifier<>(direction, product.createdAt));
        default -> throw new DomainException(DomainExceptionCode.INVALID_SORT_PARAMETER);
      }
    }
    // 동일 값일 때 페이지 간 순서가 흔들리지 않도록 id로 보조 정렬
    orders.add(product.id.desc());

    return orders.toArray(OrderSpecifier[]::new);
  }
}
