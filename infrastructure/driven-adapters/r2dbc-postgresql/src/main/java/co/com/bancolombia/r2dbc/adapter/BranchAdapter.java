package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.r2dbc.mapper.BranchMapper;
import co.com.bancolombia.r2dbc.repository.BranchReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BranchAdapter implements BranchRepository {

    private final BranchReactiveRepository branchRepository;

    @Override
    public Mono<Branch> save(Branch branch) {
        return branchRepository.save(BranchMapper.toEntity(branch))
                .map(BranchMapper::toDomain);
    }

    @Override
    public Mono<Branch> findById(UUID id) {
        return branchRepository.findById(id)
                .map(BranchMapper::toDomain);
    }

    @Override
    public Mono<Branch> update(Branch branch) {
        return branchRepository.save(BranchMapper.toEntity(branch))
                .map(BranchMapper::toDomain);
    }

}
