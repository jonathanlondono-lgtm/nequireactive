package co.com.bancolombia.model.product.gateways;


import co.com.bancolombia.model.product.Product;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductRepository {
    Mono<Void> addProductToBranch(UUID branchId, String productName, int stock);
    Mono<Product> findById(String productId);


}
