package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.r2dbc.entity.BranchEntity;
import co.com.bancolombia.r2dbc.repository.BranchReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchAdapterTest {

    @Mock
    private BranchReactiveRepository branchRepository;

    @InjectMocks
    private BranchAdapter branchAdapter;

    private UUID franchiseId;
    private UUID branchId;
    private Branch branch;
    private BranchEntity branchEntity;

    @BeforeEach
    void setUp() {
        franchiseId = UUID.randomUUID();
        branchId = UUID.randomUUID();

        branch = Branch.builder()
                .id(branchId)
                .name("Main Branch")
                .franchiseId(franchiseId)
                .products(new ArrayList<>())
                .build();

        branchEntity = BranchEntity.builder()
                .id(branchId)
                .franchiseId(franchiseId)
                .name("Main Branch")
                .build();
    }

    @Test
    @DisplayName("Should save branch successfully")
    void shouldSaveBranchSuccessfully() {
        when(branchRepository.save(any(BranchEntity.class)))
                .thenReturn(Mono.just(branchEntity));

        Mono<Branch> result = branchAdapter.save(branch);

        StepVerifier.create(result)
                .expectNextMatches(b -> b.getName().equals("Main Branch") && b.getId().equals(branchId))
                .verifyComplete();

        verify(branchRepository).save(any(BranchEntity.class));
    }

    @Test
    @DisplayName("Should find branch by id successfully")
    void shouldFindBranchByIdSuccessfully() {
        when(branchRepository.findById(branchId))
                .thenReturn(Mono.just(branchEntity));

        Mono<Branch> result = branchAdapter.findById(branchId);

        StepVerifier.create(result)
                .expectNextMatches(b -> b.getName().equals("Main Branch") && b.getFranchiseId().equals(franchiseId))
                .verifyComplete();

        verify(branchRepository).findById(branchId);
    }

    @Test
    @DisplayName("Should return empty when branch not found by id")
    void shouldReturnEmptyWhenBranchNotFoundById() {
        when(branchRepository.findById(any(UUID.class)))
                .thenReturn(Mono.empty());

        Mono<Branch> result = branchAdapter.findById(UUID.randomUUID());

        StepVerifier.create(result)
                .verifyComplete();

        verify(branchRepository).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Should update branch successfully")
    void shouldUpdateBranchSuccessfully() {
        Branch updatedBranch = Branch.builder()
                .id(branchId)
                .name("Updated Branch")
                .franchiseId(franchiseId)
                .products(new ArrayList<>())
                .build();

        BranchEntity updatedEntity = BranchEntity.builder()
                .id(branchId)
                .franchiseId(franchiseId)
                .name("Updated Branch")
                .build();

        when(branchRepository.save(any(BranchEntity.class)))
                .thenReturn(Mono.just(updatedEntity));

        Mono<Branch> result = branchAdapter.update(updatedBranch);

        StepVerifier.create(result)
                .expectNextMatches(b -> b.getName().equals("Updated Branch"))
                .verifyComplete();

        verify(branchRepository).save(any(BranchEntity.class));
    }
}

