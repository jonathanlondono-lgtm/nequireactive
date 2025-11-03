package co.com.bancolombia.model.franchise.gateways;

import co.com.bancolombia.model.franchise.Franchise;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FranchiseRepository {
    Mono<Franchise> saveFranchise(Franchise franchise);
    Mono<Franchise> getFranchiseByBranchId(UUID branchId);


    Mono<Franchise> getFranchiseByName(String name);
    Mono<Franchise> getFranchiseById(UUID id);
}
