package co.com.bancolombia.model.product.gateways;

import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductRepository {
    Mono<Product> save(Product product);
    Mono<Product> findById(UUID productId);
    Mono<Product> update(Product product);
    Mono<Void> deleteById(UUID productId);
    Flux<Product> findProductsWithHighestStockByFranchiseId(UUID franchiseId);
}

