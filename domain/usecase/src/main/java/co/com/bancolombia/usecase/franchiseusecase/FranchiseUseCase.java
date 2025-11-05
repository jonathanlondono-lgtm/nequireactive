package co.com.bancolombia.usecase.franchiseusecase;

import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.enums.DomainExceptionMessage;
import co.com.bancolombia.model.exception.FranchiseException;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.usecase.dto.MaxStockByBranch;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;

    public Mono<Franchise> createFranchise(String name) {
        Franchise franchise = Franchise.create(name);
        return franchiseRepository.saveFranchise(franchise);
    }

    public Flux<MaxStockByBranch> getProductsWithHighestStockByFranchise(UUID franchiseId) {
        return franchiseRepository.getFranchiseById(franchiseId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
                .flatMapMany(franchise -> branchRepository.findAllByFranchiseId(franchise.getId()))
                .flatMap(branch ->
                        productRepository.findProductWithHighestStockByBranchId(branch.getId())
                                .map(product -> new MaxStockByBranch(
                                        branch.getId(),
                                        branch.getName(),
                                        product.getId(),
                                        product.getName(),
                                        product.getStock()
                                ))
                );
    }

    public Mono<Franchise> updateFranchiseName(UUID franchiseId, String newName) {
        return franchiseRepository.getFranchiseById(franchiseId)
                .switchIfEmpty(Mono.error(
                        new FranchiseException(DomainExceptionMessage.FRANCHISE_NOT_FOUND, franchiseId.toString())
                ))
                .flatMap(franchise -> {
                    franchise.rename(newName);
                    return franchiseRepository.updateFranchise(franchise);
                });
    }
}
