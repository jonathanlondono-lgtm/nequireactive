package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.r2dbc.entity.ProductEntity;

public final class ProductMapper {

    private ProductMapper() {
        // Evita instanciación
    }

    public static Product toDomain(ProductEntity entity) {
        if (entity == null) return null;
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .stock(entity.getStock())
                .branchId(entity.getBranchId())
                .build();
    }

    public static ProductEntity toEntity(Product product) {
        if (product == null) return null;
        return ProductEntity.builder()
                .id(product.getId())
                .branchId(product.getBranchId())
                .name(product.getName())
                .stock(product.getStock())
                .build();
    }
}

