package co.com.bancolombia.usecase.franchiseusecase;

import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUsecaseUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> createFranchise(String name) {
        Franchise franchise = Franchise.create(name);
        return franchiseRepository.saveFranchise(franchise);
    }
}
