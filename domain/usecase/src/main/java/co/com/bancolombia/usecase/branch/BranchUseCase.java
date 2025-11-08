package co.com.bancolombia.usecase.branch;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.enums.DomainExceptionMessage;
import co.com.bancolombia.model.exception.BranchException;
import co.com.bancolombia.model.exception.FranchiseException;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class BranchUseCase {
    private final BranchRepository branchRepository;
    private final FranchiseRepository franchiseRepository;


    public Mono<Branch> addBranchToFranchise(Branch branch) {
        return franchiseRepository.findById(branch.getFranchiseId())
                .switchIfEmpty(Mono.error(new FranchiseException(
                        DomainExceptionMessage.FRANCHISE_NOT_FOUND,
                        branch.getFranchiseId().toString())))
                .flatMap(franchise -> branchRepository.save(branch));
    }


    public Mono<Branch> updateBranchName(Branch branch) {
        return branchRepository.findById(branch.getId())
                .switchIfEmpty(Mono.error(new BranchException(
                        DomainExceptionMessage.BRANCH_NOT_FOUND,
                        branch.getId().toString())))
                .flatMap(existingBranch -> {
                    existingBranch.setName(branch.getName());
                    return branchRepository.update(existingBranch);
                });
    }
}
