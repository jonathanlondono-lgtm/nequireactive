package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.r2dbc.entity.BranchEntity;
import co.com.bancolombia.r2dbc.entity.FranchiseEntity;
import co.com.bancolombia.r2dbc.repository.BranchReactiveRepository;
import co.com.bancolombia.r2dbc.repository.FranchiseReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchAdapterTest {

    @Mock
    private FranchiseReactiveRepository franchiseRepository;

    @Mock
    private BranchReactiveRepository branchRepository;

    @InjectMocks
    private BranchAdapter branchAdapter;

    private UUID franchiseId;
    private UUID branchId;
    private FranchiseEntity franchiseEntity;
    private BranchEntity branchEntity;

    @BeforeEach
    void setUp() {
        franchiseId = UUID.randomUUID();
        branchId = UUID.randomUUID();

        franchiseEntity = FranchiseEntity.builder()
                .id(franchiseId)
                .name("KFC")
                .build();

        branchEntity = BranchEntity.builder()
                .id(branchId)
                .franchiseId(franchiseId)
                .name("Main Branch")
                .build();
    }

    @Test
    @DisplayName("Should add branch to franchise successfully")
    void shouldAddBranchToFranchiseSuccessfully() {
        // Arrange
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchiseEntity));
        when(branchRepository.insertBranch(any(UUID.class), eq(franchiseId), eq("New Branch")))
                .thenReturn(Mono.empty());

        // Act
        Mono<Branch> result = branchAdapter.addBranchToFranchise(franchiseId, "New Branch");

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(branch -> branch.getName().equals("New Branch") && branch.getId() != null)
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
        verify(branchRepository).insertBranch(any(UUID.class), eq(franchiseId), eq("New Branch"));
    }

    @Test
    @DisplayName("Should return error when franchise not found")
    void shouldReturnErrorWhenFranchiseNotFound() {
        // Arrange
        when(franchiseRepository.findById(any(UUID.class)))
                .thenReturn(Mono.empty());

        // Act
        Mono<Branch> result = branchAdapter.addBranchToFranchise(franchiseId, "New Branch");

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
        verify(branchRepository, never()).insertBranch(any(), any(), anyString());
    }

    @Test
    @DisplayName("Should find branch by id successfully")
    void shouldFindBranchByIdSuccessfully() {
        // Arrange
        when(branchRepository.findById(branchId))
                .thenReturn(Mono.just(branchEntity));

        // Act
        Mono<Branch> result = branchAdapter.findById(branchId);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(branch -> branch.getName().equals("Main Branch"))
                .verifyComplete();

        verify(branchRepository).findById(branchId);
    }

    @Test
    @DisplayName("Should return empty when branch not found by id")
    void shouldReturnEmptyWhenBranchNotFoundById() {
        // Arrange
        when(branchRepository.findById(any(UUID.class)))
                .thenReturn(Mono.empty());

        // Act
        Mono<Branch> result = branchAdapter.findById(branchId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(branchRepository).findById(branchId);
    }

    @Test
    @DisplayName("Should find all branches by franchise id")
    void shouldFindAllBranchesByFranchiseId() {
        // Arrange
        BranchEntity branch2 = BranchEntity.builder()
                .id(UUID.randomUUID())
                .franchiseId(franchiseId)
                .name("Second Branch")
                .build();

        when(branchRepository.findAllByFranchiseId(franchiseId))
                .thenReturn(Flux.just(branchEntity, branch2));

        // Act
        Flux<Branch> result = branchAdapter.findAllByFranchiseId(franchiseId);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(branch -> branch.getName().equals("Main Branch"))
                .expectNextMatches(branch -> branch.getName().equals("Second Branch"))
                .verifyComplete();

        verify(branchRepository).findAllByFranchiseId(franchiseId);
    }

    @Test
    @DisplayName("Should return empty flux when no branches found")
    void shouldReturnEmptyFluxWhenNoBranchesFound() {
        // Arrange
        when(branchRepository.findAllByFranchiseId(any(UUID.class)))
                .thenReturn(Flux.empty());

        // Act
        Flux<Branch> result = branchAdapter.findAllByFranchiseId(franchiseId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(branchRepository).findAllByFranchiseId(franchiseId);
    }

    @Test
    @DisplayName("Should update branch name successfully")
    void shouldUpdateBranchNameSuccessfully() {
        // Arrange
        Branch branchToUpdate = Branch.restore(branchId, "Updated Branch");

        when(branchRepository.updateBranchName(eq("Updated Branch"), eq(branchId)))
                .thenReturn(Mono.just(1));

        // Act
        Mono<Branch> result = branchAdapter.updateBranch(branchToUpdate);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(branch -> branch.getName().equals("Updated Branch"))
                .verifyComplete();

        verify(branchRepository).updateBranchName("Updated Branch", branchId);
    }

    @Test
    @DisplayName("Should handle update branch when no rows affected")
    void shouldHandleUpdateBranchWhenNoRowsAffected() {
        // Arrange
        Branch branchToUpdate = Branch.restore(branchId, "Updated Branch");

        when(branchRepository.updateBranchName(anyString(), any(UUID.class)))
                .thenReturn(Mono.just(0));

        // Act
        Mono<Branch> result = branchAdapter.updateBranch(branchToUpdate);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(branch -> branch.getName().equals("Updated Branch"))
                .verifyComplete();

        verify(branchRepository).updateBranchName("Updated Branch", branchId);
    }
}
