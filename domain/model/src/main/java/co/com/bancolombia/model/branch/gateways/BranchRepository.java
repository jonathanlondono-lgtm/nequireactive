package co.com.bancolombia.model.branch.gateways;

import co.com.bancolombia.model.branch.Branch;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BranchRepository {
    Mono<Branch> save(Branch branch);
    Mono<Branch> findById(UUID id);
    Mono<Branch> update(Branch branch);
}
