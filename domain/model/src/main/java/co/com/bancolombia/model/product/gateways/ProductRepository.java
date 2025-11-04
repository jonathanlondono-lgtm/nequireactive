package co.com.bancolombia.model.product.gateways;


import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductRepository {
    Mono<Void> addProductToBranch(UUID branchId, Product product);
    Mono<Product> findById(String productId);
    Mono<Void> deleteById(UUID productId);
    Mono<Product> updateProduct(UUID productId, int newStock);

    Mono<Product> findProductWithHighestStockByBranchId(UUID branchId);





}
