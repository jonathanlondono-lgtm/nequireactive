package co.com.bancolombia.usecase.product;

import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.enums.TechnicalMessage;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.Product;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class ProductUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;

    public Mono<Product> addProductToBranch(Product product) {
        return branchRepository.findById(product.getBranchId())
                .switchIfEmpty(Mono.error(new BusinessException(
                        TechnicalMessage.BRANCH_NOT_FOUND,
                        product.getBranchId().toString())))
                .flatMap(branch -> productRepository.save(product));
    }

    public Mono<Void> deleteProduct(UUID productId) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new BusinessException(
                        TechnicalMessage.PRODUCT_NOT_FOUND,
                        productId.toString())))
                .flatMap(product -> productRepository.deleteById(productId));
    }

    public Mono<Product> updateProductStock(Product product) {
        return productRepository.findById(product.getId())
                .switchIfEmpty(Mono.error(new BusinessException(
                        TechnicalMessage.PRODUCT_NOT_FOUND,
                        product.getId().toString())))
                .flatMap(existingProduct -> {
                    existingProduct.setStock(product.getStock());
                    return productRepository.update(existingProduct);
                });
    }

    public Mono<Product> updateProductName(Product product) {
        return productRepository.findById(product.getId())
                .switchIfEmpty(Mono.error(new BusinessException(
                        TechnicalMessage.PRODUCT_NOT_FOUND,
                        product.getId().toString())))
                .flatMap(existingProduct -> {
                    existingProduct.setName(product.getName());
                    return productRepository.update(existingProduct);
                });
    }

    public Flux<Product> getProductsWithHighestStockByFranchise(UUID franchiseId) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new BusinessException(
                        TechnicalMessage.FRANCHISE_NOT_FOUND,
                        franchiseId.toString())))
                .flatMapMany(franchise ->
                    productRepository.findProductsWithHighestStockByFranchiseId(franchiseId)
                );
    }
}
