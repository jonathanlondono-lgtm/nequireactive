package co.com.bancolombia.r2dbc.repository;

import co.com.bancolombia.r2dbc.entity.FranchiseEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FranchiseReactiveRepository extends ReactiveCrudRepository<FranchiseEntity, UUID> {

    @Query("SELECT f.* FROM franchise f " +
            "JOIN branch b ON f.id = b.franchise_id " +
            "WHERE b.id = :branchId")
    Mono<FranchiseEntity> findFranchiseByBranchId(UUID branchId);


    @Query("INSERT INTO franchise (id, name) VALUES ($1, $2)")
    Mono<Void> insertFranchise(UUID id, String name);
    Mono<FranchiseEntity> findByName(String name);

}