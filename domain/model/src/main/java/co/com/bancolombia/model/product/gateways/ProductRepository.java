package co.com.bancolombia.model.product.gateways;

import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductRepository {
    Mono<Product> saveProduct(Product product);
    Mono<Product> updateProduct(Product product);
    Mono<Product> getProductByName(String name, UUID branchId);
    Mono<Product> getProductById(UUID id);
    Mono<Void> removeProduct(Product product);
    Flux<Product> getTopProductsForBranchAndFranchise(UUID franchise);
}
