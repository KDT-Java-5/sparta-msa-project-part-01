package com.sparta.msa.project.domain.product.mapper;

import com.sparta.msa.project.domain.category.entity.Category;
import com.sparta.msa.project.domain.product.dto.request.ProductOptionRequest;
import com.sparta.msa.project.domain.product.dto.response.ProductDetailResponse;
import com.sparta.msa.project.domain.product.dto.response.ProductOptionResponse;
import com.sparta.msa.project.domain.product.dto.response.ProductOptionsResponse;
import com.sparta.msa.project.domain.product.dto.response.ProductSummaryResponse;
import com.sparta.msa.project.domain.product.entity.Product;
import com.sparta.msa.project.domain.product.entity.ProductOption;
import com.sparta.msa.project.domain.product.entity.ProductOptionSpec;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  ProductDetailResponse toDetailResponse(Product product);

  ProductSummaryResponse toSummaryResponse(Product product);

  ProductOptionsResponse toOptionsResponse(Product product);

  ProductOptionResponse toOptionResponse(ProductOption option);

  ProductDetailResponse.CategoryInfo toCategoryInfo(Category category);

  ProductOptionSpec toOptionSpec(ProductOptionRequest request);

  List<ProductOptionSpec> toOptionSpecs(List<ProductOptionRequest> requests);
}
