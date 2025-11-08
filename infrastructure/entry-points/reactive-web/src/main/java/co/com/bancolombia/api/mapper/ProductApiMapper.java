package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.request.ProductDTO;
import co.com.bancolombia.api.dto.request.UpdateProductNameRequest;
import co.com.bancolombia.api.dto.request.UpdateProductStockRequest;
import co.com.bancolombia.api.dto.response.ProductResponse;
import co.com.bancolombia.api.dto.response.ProductWithBranchResponse;
import co.com.bancolombia.model.product.Product;

import java.util.UUID;

public final class ProductApiMapper {

    private ProductApiMapper() {
    }

    public static Product toDomain(ProductDTO dto, UUID branchId) {
        if (dto == null) return null;
        return Product.builder()
                .name(dto.getName())
                .stock(dto.getStock())
                .branchId(branchId)
                .build();
    }

    public static Product toDomainForStockUpdate(UpdateProductStockRequest request) {
        if (request == null) return null;
        return Product.builder()
                .id(request.getProductId())
                .stock(request.getNewStock())
                .build();
    }

    public static Product toDomainForNameUpdate(UpdateProductNameRequest request) {
        if (request == null) return null;
        return Product.builder()
                .id(request.getProductId())
                .name(request.getNewName())
                .build();
    }

    public static ProductResponse toResponse(Product product) {
        if (product == null) return null;
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getStock(),
                product.getBranchId()
        );
    }

    public static ProductWithBranchResponse toProductWithBranchResponse(Product product) {
        if (product == null) return null;
        return new ProductWithBranchResponse(
                product.getBranchId(),
                product.getId(),
                product.getName(),
                product.getStock()
        );
    }
}

