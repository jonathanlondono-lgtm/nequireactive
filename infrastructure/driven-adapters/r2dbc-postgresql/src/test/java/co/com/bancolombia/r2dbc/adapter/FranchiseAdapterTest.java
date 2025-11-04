package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.franchise.Franchise;
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
class FranchiseAdapterTest {

    @Mock
    private FranchiseReactiveRepository franchiseRepository;

    @Mock
    private BranchReactiveRepository branchRepository;

    @InjectMocks
    private FranchiseAdapter franchiseAdapter;

    private UUID franchiseId;
    private Franchise franchise;
    private FranchiseEntity franchiseEntity;

    @BeforeEach
    void setUp() {
        franchiseId = UUID.randomUUID();
        franchise = Franchise.create("KFC");
        franchiseEntity = FranchiseEntity.builder()
                .id(franchiseId)
                .name("KFC")
                .build();
    }

    @Test
    @DisplayName("Should save franchise successfully")
    void shouldSaveFranchiseSuccessfully() {
        // Arrange
        when(franchiseRepository.insertFranchise(any(UUID.class), anyString()))
                .thenReturn(Mono.empty());

        // Act
        Mono<Franchise> result = franchiseAdapter.saveFranchise(franchise);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(f -> f.getName().equals("KFC"))
                .verifyComplete();

        verify(franchiseRepository).insertFranchise(any(UUID.class), eq("KFC"));
    }

    @Test
    @DisplayName("Should get franchise by id successfully")
    void shouldGetFranchiseByIdSuccessfully() {
        // Arrange
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchiseEntity));

        // Act
        Mono<Franchise> result = franchiseAdapter.getFranchiseById(franchiseId);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(f -> f.getName().equals("KFC"))
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
    }

    @Test
    @DisplayName("Should return empty when franchise not found by id")
    void shouldReturnEmptyWhenFranchiseNotFoundById() {
        // Arrange
        when(franchiseRepository.findById(any(UUID.class)))
                .thenReturn(Mono.empty());

        // Act
        Mono<Franchise> result = franchiseAdapter.getFranchiseById(UUID.randomUUID());

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(franchiseRepository).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Should get franchise by branch id with branches")
    void shouldGetFranchiseByBranchIdWithBranches() {
        // Arrange
        UUID branchId = UUID.randomUUID();
        BranchEntity branchEntity = BranchEntity.builder()
                .id(branchId)
                .franchiseId(franchiseId)
                .name("Main Branch")
                .build();

        when(franchiseRepository.findFranchiseByBranchId(branchId))
                .thenReturn(Mono.just(franchiseEntity));
        when(branchRepository.findByFranchiseId(franchiseId))
                .thenReturn(Flux.just(branchEntity));

        // Act
        Mono<Franchise> result = franchiseAdapter.getFranchiseByBranchId(branchId);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(f ->
                    f.getName().equals("KFC") &&
                    f.getBranches().size() == 1 &&
                    f.getBranches().get(0).getName().equals("Main Branch")
                )
                .verifyComplete();

        verify(franchiseRepository).findFranchiseByBranchId(branchId);
        verify(branchRepository).findByFranchiseId(franchiseId);
    }

    @Test
    @DisplayName("Should throw error when franchise not found by branch id")
    void shouldThrowErrorWhenFranchiseNotFoundByBranchId() {
        // Arrange
        UUID branchId = UUID.randomUUID();
        when(franchiseRepository.findFranchiseByBranchId(branchId))
                .thenReturn(Mono.empty());

        // Act
        Mono<Franchise> result = franchiseAdapter.getFranchiseByBranchId(branchId);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(e ->
                    e instanceof IllegalArgumentException &&
                    e.getMessage().contains("Franchise not found for this branch")
                )
                .verify();

        verify(franchiseRepository).findFranchiseByBranchId(branchId);
        verify(branchRepository, never()).findByFranchiseId(any());
    }

    @Test
    @DisplayName("Should update franchise successfully")
    void shouldUpdateFranchiseSuccessfully() {
        // Arrange
        Franchise updatedFranchise = Franchise.restore(franchiseId, "KFC Updated");
        FranchiseEntity updatedEntity = FranchiseEntity.builder()
                .id(franchiseId)
                .name("KFC Updated")
                .build();

        when(franchiseRepository.save(any(FranchiseEntity.class)))
                .thenReturn(Mono.just(updatedEntity));

        // Act
        Mono<Franchise> result = franchiseAdapter.updateFranchise(updatedFranchise);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(f -> f.getName().equals("KFC Updated"))
                .verifyComplete();

        verify(franchiseRepository).save(any(FranchiseEntity.class));
    }

    @Test
    @DisplayName("Should get franchise by branch id with empty branches")
    void shouldGetFranchiseByBranchIdWithEmptyBranches() {
        // Arrange
        UUID branchId = UUID.randomUUID();

        when(franchiseRepository.findFranchiseByBranchId(branchId))
                .thenReturn(Mono.just(franchiseEntity));
        when(branchRepository.findByFranchiseId(franchiseId))
                .thenReturn(Flux.empty());

        // Act
        Mono<Franchise> result = franchiseAdapter.getFranchiseByBranchId(branchId);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(f ->
                    f.getName().equals("KFC") &&
                    f.getBranches().isEmpty()
                )
                .verifyComplete();

        verify(franchiseRepository).findFranchiseByBranchId(branchId);
        verify(branchRepository).findByFranchiseId(franchiseId);
    }
}

