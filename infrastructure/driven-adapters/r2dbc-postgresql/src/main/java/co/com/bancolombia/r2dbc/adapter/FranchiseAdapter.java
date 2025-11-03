package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.r2dbc.entity.FranchiseEntity;
import co.com.bancolombia.r2dbc.mapper.BranchMapper;
import co.com.bancolombia.r2dbc.mapper.FranchiseMapper;
import co.com.bancolombia.r2dbc.repository.BranchReactiveRepository;
import co.com.bancolombia.r2dbc.repository.FranchiseReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FranchiseAdapter implements FranchiseRepository {

    private final FranchiseReactiveRepository repository;
    private final BranchReactiveRepository branchRepository;


    @Override
    public Mono<Franchise> saveFranchise(Franchise franchise) {
        FranchiseEntity entity = FranchiseMapper.toEntity(franchise);
        return repository.insertFranchise(entity.getId(), entity.getName())
                .then(Mono.just(franchise)); }

    @Override
    public Mono<Franchise> getFranchiseByBranchId(UUID branchId) {
        return repository.findFranchiseByBranchId(branchId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found for this branch")))
                .flatMap(franchiseEntity ->
                        branchRepository.findByFranchiseId(franchiseEntity.getId())
                                .collectList()
                                .map(branchEntities -> {
                                    Franchise franchise = FranchiseMapper.toDomain(franchiseEntity);
                                    branchEntities.forEach(b -> franchise.addBranch(
                                            BranchMapper.toDomain(b)
                                    ));
                                    return franchise;
                                })
                );
    }


    @Override
    public Mono<Franchise> getFranchiseByName(String name) {
        return null;
    }

    @Override
    public Mono<Franchise> getFranchiseById(UUID id) {
        return null;
    }
}
