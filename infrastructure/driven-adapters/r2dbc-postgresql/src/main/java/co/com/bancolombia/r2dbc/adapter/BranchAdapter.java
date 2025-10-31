package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.r2dbc.entity.BranchEntity;
import co.com.bancolombia.r2dbc.mapper.FranchiseMapper;
import co.com.bancolombia.r2dbc.repository.BranchReactiveRepository;
import co.com.bancolombia.r2dbc.repository.FranchiseReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class BranchAdapter implements BranchRepository {

    private final FranchiseReactiveRepository franchiseRepository;
    private final BranchReactiveRepository branchRepository;

    @Override
    public Mono<Void> addBranchToFranchise(UUID franchiseId, String branchName) {
        return franchiseRepository.findById(franchiseId)
                .map(FranchiseMapper::toDomain)
                .flatMap(franchise -> {
                    Branch newBranch = Branch.create(branchName);
                    franchise.addBranch(newBranch);

                    UUID branchId = UUID.fromString(newBranch.getId());

                    return branchRepository.insertBranch(branchId, franchiseId, branchName)
                            .then();
                });
    }

    @Override
    public Mono<Franchise> findById(UUID id) {
        return franchiseRepository.findById(id)
                .map(FranchiseMapper::toDomain);
    }
}
