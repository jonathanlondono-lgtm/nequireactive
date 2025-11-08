package co.com.bancolombia.usecase.franchiseusecase;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.exception.BranchException;
import co.com.bancolombia.model.exception.FranchiseException;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.model.franchise.gateways.FranchiseRepository;
import co.com.bancolombia.usecase.branch.BranchUseCase;
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
class BranchUseCaseTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    @InjectMocks
    private BranchUseCase branchUseCase;

    private UUID franchiseId;
    private UUID branchId;
    private Branch branch;
    private Franchise franchise;

    @BeforeEach
    void setUp() {
        franchiseId = UUID.randomUUID();
        branchId = UUID.randomUUID();

        franchise = Franchise.builder()
                .id(franchiseId)
                .name("KFC")
                .branches(new ArrayList<>())
                .build();

        branch = Branch.builder()
                .id(branchId)
                .name("Main Branch")
                .franchiseId(franchiseId)
                .products(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Should add branch to franchise successfully")
    void shouldAddBranchToFranchiseSuccessfully() {
        // Arrange
        Branch newBranch = Branch.builder()
                .name("New Branch")
                .franchiseId(franchiseId)
                .products(new ArrayList<>())
                .build();

        Branch savedBranch = Branch.builder()
                .id(UUID.randomUUID())
                .name("New Branch")
                .franchiseId(franchiseId)
                .products(new ArrayList<>())
                .build();

        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchise));
        when(branchRepository.save(any(Branch.class)))
                .thenReturn(Mono.just(savedBranch));

        // Act
        Mono<Branch> result = branchUseCase.addBranchToFranchise(newBranch);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(b -> b.getName().equals("New Branch") && b.getId() != null)
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
        verify(branchRepository).save(any(Branch.class));
    }

    @Test
    @DisplayName("Should throw exception when franchise not found")
    void shouldThrowExceptionWhenFranchiseNotFound() {
        // Arrange
        Branch newBranch = Branch.builder()
                .name("New Branch")
                .franchiseId(franchiseId)
                .products(new ArrayList<>())
                .build();

        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.empty());

        // Act
        Mono<Branch> result = branchUseCase.addBranchToFranchise(newBranch);

        // Assert
        StepVerifier.create(result)
                .expectError(FranchiseException.class)
                .verify();

        verify(franchiseRepository).findById(franchiseId);
        verify(branchRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update branch name successfully")
    void shouldUpdateBranchNameSuccessfully() {
        // Arrange
        Branch updatedBranch = Branch.builder()
                .id(branchId)
                .name("Updated Branch Name")
                .franchiseId(franchiseId)
                .products(new ArrayList<>())
                .build();

        when(branchRepository.findById(branchId))
                .thenReturn(Mono.just(branch));
        when(branchRepository.update(any(Branch.class)))
                .thenReturn(Mono.just(updatedBranch));

        // Act
        Mono<Branch> result = branchUseCase.updateBranchName(updatedBranch);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(b -> b.getName().equals("Updated Branch Name"))
                .verifyComplete();

        verify(branchRepository).findById(branchId);
        verify(branchRepository).update(any(Branch.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent branch")
    void shouldThrowExceptionWhenUpdatingNonExistentBranch() {
        // Arrange
        Branch updatedBranch = Branch.builder()
                .id(branchId)
                .name("New Name")
                .products(new ArrayList<>())
                .build();

        when(branchRepository.findById(branchId))
                .thenReturn(Mono.empty());

        // Act
        Mono<Branch> result = branchUseCase.updateBranchName(updatedBranch);

        // Assert
        StepVerifier.create(result)
                .expectError(BranchException.class)
                .verify();

        verify(branchRepository).findById(branchId);
        verify(branchRepository, never()).update(any());
    }
}

