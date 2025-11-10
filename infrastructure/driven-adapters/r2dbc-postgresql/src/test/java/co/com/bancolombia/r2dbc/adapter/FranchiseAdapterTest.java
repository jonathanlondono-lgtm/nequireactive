package co.com.bancolombia.r2dbc.adapter;

import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.r2dbc.entity.FranchiseEntity;
import co.com.bancolombia.r2dbc.repository.FranchiseReactiveRepository;
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
class FranchiseAdapterTest {

    @Mock
    private FranchiseReactiveRepository franchiseRepository;

    @InjectMocks
    private FranchiseAdapter franchiseAdapter;

    private UUID franchiseId;
    private Franchise franchise;
    private FranchiseEntity franchiseEntity;

    @BeforeEach
    void setUp() {
        franchiseId = UUID.randomUUID();

        franchise = Franchise.builder()
                .id(franchiseId)
                .name("KFC")
                .branches(new ArrayList<>())
                .build();

        franchiseEntity = FranchiseEntity.builder()
                .id(franchiseId)
                .name("KFC")
                .build();
    }

    @Test
    @DisplayName("Should save franchise successfully")
    void shouldSaveFranchiseSuccessfully() {
        when(franchiseRepository.save(any(FranchiseEntity.class)))
                .thenReturn(Mono.just(franchiseEntity));

        Mono<Franchise> result = franchiseAdapter.save(franchise);

        StepVerifier.create(result)
                .expectNextMatches(f -> f.getName().equals("KFC") && f.getId().equals(franchiseId))
                .verifyComplete();

        verify(franchiseRepository).save(any(FranchiseEntity.class));
    }

    @Test
    @DisplayName("Should find franchise by id successfully")
    void shouldFindFranchiseByIdSuccessfully() {
        when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchiseEntity));

        Mono<Franchise> result = franchiseAdapter.findById(franchiseId);

        StepVerifier.create(result)
                .expectNextMatches(f -> f.getName().equals("KFC") && f.getId().equals(franchiseId))
                .verifyComplete();

        verify(franchiseRepository).findById(franchiseId);
    }

    @Test
    @DisplayName("Should return empty when franchise not found by id")
    void shouldReturnEmptyWhenFranchiseNotFoundById() {
        when(franchiseRepository.findById(any(UUID.class)))
                .thenReturn(Mono.empty());

        Mono<Franchise> result = franchiseAdapter.findById(UUID.randomUUID());

        StepVerifier.create(result)
                .verifyComplete();

        verify(franchiseRepository).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Should update franchise successfully")
    void shouldUpdateFranchiseSuccessfully() {
        Franchise updatedFranchise = Franchise.builder()
                .id(franchiseId)
                .name("KFC Premium")
                .branches(new ArrayList<>())
                .build();

        FranchiseEntity updatedEntity = FranchiseEntity.builder()
                .id(franchiseId)
                .name("KFC Premium")
                .build();

        when(franchiseRepository.save(any(FranchiseEntity.class)))
                .thenReturn(Mono.just(updatedEntity));

        Mono<Franchise> result = franchiseAdapter.update(updatedFranchise);

        StepVerifier.create(result)
                .expectNextMatches(f -> f.getName().equals("KFC Premium"))
                .verifyComplete();

        verify(franchiseRepository).save(any(FranchiseEntity.class));
    }
}

