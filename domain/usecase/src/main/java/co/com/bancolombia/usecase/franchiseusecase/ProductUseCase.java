package co.com.bancolombia.usecase.franchiseusecase;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.enums.DomainExceptionMessage;
import co.com.bancolombia.model.exception.BranchException;
import co.com.bancolombia.model.exception.ProductException;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class ProductUseCase {

    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Mono<Void> execute(UUID branchId, String productName, int stock) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Branch not found")))
                .flatMap(branch -> {
                    Product product = Product.create(productName, stock);

                    return productRepository.addProductToBranch(branchId, product);
                });
    }

    public Mono<Void> execute(UUID branchId, UUID productId) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(
                        new BranchException(DomainExceptionMessage.BRANCH_NOT_FOUND, branchId.toString())
                ))
                .flatMap(branch ->
                        Mono.fromRunnable(() -> branch.removeProduct(productId))
                                .then(productRepository.deleteById(productId))
                );
    }

    public Mono<Product> execute(UUID productId, int newStock) {
        return productRepository.findById(productId.toString())
                .switchIfEmpty(Mono.error(
                        new ProductException(DomainExceptionMessage.PRODUCT_NOT_FOUND, productId.toString())
                ))
                .flatMap(product -> {
                    product.updateStock(newStock);
                    return productRepository.updateProduct(productId, newStock);
                });
    }

    public Mono<Product> updateProductName(UUID productId, String newName) {
        return productRepository.findById(productId.toString())
                .switchIfEmpty(Mono.error(new ProductException(DomainExceptionMessage.PRODUCT_NOT_FOUND, productId.toString())))
                .flatMap(product -> {
                    product.rename(newName);
                    return productRepository.updateProduct(product);
                });
    }




}