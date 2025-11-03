package co.com.bancolombia.usecase.franchiseusecase;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.enums.DomainExceptionMessage;
import co.com.bancolombia.model.exception.BranchException;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class ProductUseCase {

    private final BranchRepository branchRepository; // solo para verificar que la branch existe
    private final ProductRepository productRepository; // para agregar producto

    public Mono<Void> execute(UUID branchId, String productName, int stock) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Branch not found")))
                .flatMap(branch -> {
                    Product product = Product.create(productName, stock);

                    return productRepository.addProductToBranch(branchId, product);
                });
    }
}