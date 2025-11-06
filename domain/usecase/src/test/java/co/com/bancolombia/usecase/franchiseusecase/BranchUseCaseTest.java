package co.com.bancolombia.usecase.franchiseusecase;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.branch.gateways.BranchRepository;
import co.com.bancolombia.model.exception.BranchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BranchUseCaseTest {

    @Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchUseCase branchUseCase;

    private UUID franchiseId;
    private UUID branchId;
    private Branch branch;

    @BeforeEach
    void setUp() {
        franchiseId = UUID.randomUUID();
        branchId = UUID.randomUUID();
        branch = Branch.create("Main Branch");
    }

    @Test
    @DisplayName("Should add branch to franchise successfully")
    void shouldAddBranchToFranchiseSuccessfully() {
        // Arrange
        Branch newBranch = Branch.create("New Branch");
        when(branchRepository.addBranchToFranchise(franchiseId, "New Branch"))
                .thenReturn(Mono.just(newBranch));

        // Act
        Mono<Branch> result = branchUseCase.execute(franchiseId, "New Branch");

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(b -> b.getName().equals("New Branch"))
                .verifyComplete();

        verify(branchRepository).addBranchToFranchise(franchiseId, "New Branch");
    }

    @Test
    @DisplayName("Should propagate error when repository fails")
    void shouldPropagateErrorWhenRepositoryFails() {
        // Arrange
        when(branchRepository.addBranchToFranchise(any(UUID.class), anyString()))
                .thenReturn(Mono.error(new RuntimeException("Repository error")));

        // Act
        Mono<Branch> result = branchUseCase.execute(franchiseId, "New Branch");

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Repository error"))
                .verify();

        verify(branchRepository).addBranchToFranchise(franchiseId, "New Branch");
    }

    @Test
    @DisplayName("Should update branch name successfully")
    void shouldUpdateBranchNameSuccessfully() {
        // Arrange
        Branch branchWithId = Branch.restore(branchId, "Old Name");
        Branch updatedBranch = Branch.restore(branchId, "New Name");

        when(branchRepository.findById(branchId))
                .thenReturn(Mono.just(branchWithId));
        when(branchRepository.updateBranch(any(Branch.class)))
                .thenReturn(Mono.just(updatedBranch));

        // Act
        Mono<Branch> result = branchUseCase.updateBranchName(branchId, "New Name");

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(b -> b.getName().equals("New Name"))
                .verifyComplete();

        verify(branchRepository).findById(branchId);
        verify(branchRepository).updateBranch(any(Branch.class));
    }

    @Test
    @DisplayName("Should throw exception when branch not found")
    void shouldThrowExceptionWhenBranchNotFound() {
        // Arrange
        when(branchRepository.findById(any(UUID.class)))
                .thenReturn(Mono.empty());

        // Act
        Mono<Branch> result = branchUseCase.updateBranchName(branchId, "New Name");

        // Assert
        StepVerifier.create(result)
                .expectError(BranchException.class)
                .verify();

        verify(branchRepository).findById(branchId);
        verify(branchRepository, never()).updateBranch(any());
    }

    @Test
    @DisplayName("Should throw exception when updating with null name")
    void shouldThrowExceptionWhenUpdatingWithNullName() {
        // Arrange
        Branch branchWithId = Branch.restore(branchId, "Current Name");

        when(branchRepository.findById(branchId))
                .thenReturn(Mono.just(branchWithId));

        // Act
        Mono<Branch> result = branchUseCase.updateBranchName(branchId, null);

        // Assert
        StepVerifier.create(result)
                .expectError(BranchException.class)
                .verify();

        verify(branchRepository).findById(branchId);
        verify(branchRepository, never()).updateBranch(any());
    }

    @Test
    @DisplayName("Should throw exception when updating with empty name")
    void shouldThrowExceptionWhenUpdatingWithEmptyName() {
        // Arrange
        Branch branchWithId = Branch.restore(branchId, "Current Name");

        when(branchRepository.findById(branchId))
                .thenReturn(Mono.just(branchWithId));

        // Act
        Mono<Branch> result = branchUseCase.updateBranchName(branchId, "   ");

        // Assert
        StepVerifier.create(result)
                .expectError(BranchException.class)
                .verify();

        verify(branchRepository).findById(branchId);
        verify(branchRepository, never()).updateBranch(any());
    }
}
