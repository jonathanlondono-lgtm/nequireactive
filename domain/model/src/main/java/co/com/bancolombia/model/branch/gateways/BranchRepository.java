package co.com.bancolombia.model.branch.gateways;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.franchise.Franchise;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BranchRepository {
    Mono<Void> addBranchToFranchise(UUID franchiseId, String branchName);
    Mono<Franchise> findById(UUID id);

}
