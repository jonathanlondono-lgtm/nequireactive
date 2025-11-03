package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.r2dbc.entity.ProductEntity;
import co.com.bancolombia.r2dbc.mapper.ProductMapper;
import co.com.bancolombia.r2dbc.repository.ProductReactiveRepository;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductAdapter implements ProductRepository {

    private final ProductReactiveRepository productRepository;

    @Override
    public Mono<Void> addProductToBranch(UUID branchId,Product product) {
        ProductEntity entity = ProductMapper.toEntity(product, branchId);
        return productRepository.insertProduct(
                entity.getId(),
                entity.getBranchId(),
                entity.getName(),
                entity.getStock()
        );    }

    @Override
    public Mono<Product> findById(String productId) {
        return productRepository.findById(productId)
                .map(ProductMapper::toDomain);    }

    @Override
    public Mono<Void> deleteById(UUID productId) {
        return productRepository.deleteById(productId);
    }
}
