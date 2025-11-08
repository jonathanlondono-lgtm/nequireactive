package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.r2dbc.mapper.FranchiseMapper;
import co.com.bancolombia.r2dbc.repository.FranchiseReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FranchiseAdapter implements FranchiseRepository {

    private final FranchiseReactiveRepository repository;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return repository.save(FranchiseMapper.toEntity(franchise))
                .map(FranchiseMapper::toDomain);
    }

    @Override
    public Mono<Franchise> findById(UUID id) {
        return repository.findById(id)
                .map(FranchiseMapper::toDomain);
    }

    @Override
    public Mono<Franchise> update(Franchise franchise) {
        return repository.save(FranchiseMapper.toEntity(franchise))
                .map(FranchiseMapper::toDomain);
    }
}
