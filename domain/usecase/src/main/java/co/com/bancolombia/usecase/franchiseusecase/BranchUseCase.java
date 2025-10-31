package co.com.bancolombia.usecase.franchiseusecase;

import co.com.bancolombia.model.branch.gateways.BranchRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class BranchUseCase {
    private final BranchRepository branchRepository;

    public Mono<Void> execute(UUID franchiseId, String branchName) {
        return branchRepository.addBranchToFranchise(franchiseId, branchName);
    }

}
