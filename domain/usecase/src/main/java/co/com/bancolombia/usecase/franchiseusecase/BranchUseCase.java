package co.com.bancolombia.usecase.franchiseusecase;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.enums.DomainExceptionMessage;
import co.com.bancolombia.model.exception.BranchException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class BranchUseCase {
    private final BranchRepository branchRepository;

    public Mono<Branch> execute(UUID franchiseId, String branchName) {
        return branchRepository.addBranchToFranchise(franchiseId, branchName);
    }

    public Mono<Branch> updateBranchName(UUID branchId, String newName) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new BranchException(DomainExceptionMessage.BRANCH_NOT_FOUND, branchId.toString())))
                .flatMap(branch -> {
                    branch.rename(newName);
                    return branchRepository.updateBranch(branch);
                });
    }

}
