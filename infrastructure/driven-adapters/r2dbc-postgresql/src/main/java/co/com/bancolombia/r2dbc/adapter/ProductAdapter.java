package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.r2dbc.mapper.ProductMapper;
import co.com.bancolombia.r2dbc.repository.ProductReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductAdapter implements ProductRepository {

    private final ProductReactiveRepository productRepository;

    @Override
    public Mono<Product> save(Product product) {
        return productRepository.save(ProductMapper.toEntity(product))
                .map(ProductMapper::toDomain);
    }

    @Override
    public Mono<Product> findById(UUID productId) {
        return productRepository.findById(productId)
                .map(ProductMapper::toDomain);
    }

    @Override
    public Mono<Product> update(Product product) {
        return productRepository.save(ProductMapper.toEntity(product))
                .map(ProductMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(UUID productId) {
        return productRepository.deleteById(productId);
    }

    @Override
    public Flux<Product> findProductsWithHighestStockByFranchiseId(UUID franchiseId) {
        return productRepository.findProductsWithHighestStockByFranchiseId(franchiseId)
                .map(ProductMapper::toDomain);
    }
}

