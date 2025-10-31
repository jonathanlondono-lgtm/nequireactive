package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.r2dbc.entity.ProductEntity;

import java.util.UUID;

public final class ProductMapper {

    private ProductMapper() {
        // Evita instanciación
    }

    // De Entity a Dominio
    public static Product toDomain(ProductEntity entity) {
        if (entity == null) return null;
        Product product = Product.create(entity.getName(), entity.getStock());
        // Como el dominio genera un UUID nuevo al crear, necesitamos sobrescribirlo
        return Product.restore(entity.getId(), entity.getName(), entity.getStock());
    }

    // De Dominio a Entity
    public static ProductEntity toEntity(Product product, UUID branchId) {
        if (product == null) return null;
        return ProductEntity.builder()
                .id(product.getId())      // ya es UUID
                .branchId(branchId)       // UUID directo
                .name(product.getName())
                .stock(product.getStock())
                .build();
    }
}
