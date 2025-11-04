package co.com.bancolombia.usecase.franchiseusecase;

import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.model.product.gateways.ProductRepository;
import co.com.bancolombia.usecase.dto.MaxStockByBranchResponse;
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

    public Flux<MaxStockByBranchResponse> getProductsWithHighestStockByFranchise(UUID franchiseId) {
        return franchiseRepository.getFranchiseById(franchiseId)
                .switchIfEmpty(Mono.error(new RuntimeException("Franchise not found")))
                .flatMapMany(franchise -> branchRepository.findAllByFranchiseId(franchise.getId()))
                .flatMap(branch ->
                        productRepository.findProductWithHighestStockByBranchId(branch.getId())
                                .map(product -> new MaxStockByBranchResponse(
                                        branch.getId(),
                                        branch.getName(),
                                        product.getId(),
                                        product.getName(),
                                        product.getStock()
                                ))
                );
    }
}
