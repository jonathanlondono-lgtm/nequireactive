package co.com.bancolombia.usecase.franchiseusecase;

import co.com.bancolombia.model.enums.TechnicalMessage;
import co.com.bancolombia.model.exception.BusinessException;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> createFranchise(Franchise franchise) {
        return franchiseRepository.save(franchise);
    }

    public Mono<Franchise> updateFranchiseName(Franchise franchise) {
        return franchiseRepository.findById(franchise.getId())
                .switchIfEmpty(Mono.error(new BusinessException(
                        TechnicalMessage.FRANCHISE_NOT_FOUND,
                        franchise.getId().toString())))
                .flatMap(existingFranchise -> {
                    existingFranchise.setName(franchise.getName());
                    return franchiseRepository.update(existingFranchise);
                });
    }
}
