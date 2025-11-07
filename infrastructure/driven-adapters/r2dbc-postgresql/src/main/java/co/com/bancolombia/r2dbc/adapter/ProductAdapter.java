package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.r2dbc.entity.ProductEntity;
import co.com.bancolombia.r2dbc.mapper.ProductMapper;
import co.com.bancolombia.r2dbc.repository.ProductReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductAdapter implements ProductRepository {

    private final ProductReactiveRepository productRepository;

    @Override
    public Mono<Product> addProductToBranch(UUID branchId, Product product) {
        ProductEntity entity = ProductMapper.toEntity(product, branchId);
        return productRepository.insertProduct(
                entity.getId(),
                entity.getBranchId(),
                entity.getName(),
                entity.getStock()
        ).thenReturn(product);
    }

    @Override
    public Mono<Product> findById(String productId) {
        return productRepository.findById(productId)
                .map(ProductMapper::toDomain);    }

    @Override
    public Mono<Void> deleteById(UUID productId) {
        return productRepository.deleteById(productId);
    }

    @Override
    public Mono<Product> updateProduct(UUID productId, int newStock) {
        return productRepository.findById(productId.toString())
                .flatMap(entity -> {
                    entity.setStock(newStock);
                    return productRepository.save(entity);
                })
                .map(ProductMapper::toDomain);    }

    @Override
    public Mono<Product> updateProduct(Product product) {
        return productRepository.updateProductName(product.getName(), product.getId())
                .thenReturn(product);
    }

    @Override
    public Mono<Product> findProductWithHighestStockByBranchId(UUID branchId) {
        return productRepository.findTopByBranchIdOrderByStockDesc(branchId)
                .map(ProductMapper::toDomain);    }
}
