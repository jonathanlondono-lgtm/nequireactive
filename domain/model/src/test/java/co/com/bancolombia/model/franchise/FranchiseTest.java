package co.com.bancolombia.model.franchise;

import co.com.bancolombia.model.branch.Branch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FranchiseTest {

    @Test
    @DisplayName("Should create franchise with builder")
    void shouldCreateFranchiseWithBuilder() {
        Franchise franchise = Franchise.builder()
                .name("KFC")
                .branches(new ArrayList<>())
                .build();


        assertNotNull(franchise);
        assertEquals("KFC", franchise.getName());
        assertNotNull(franchise.getBranches());
        assertTrue(franchise.getBranches().isEmpty());
    }

    @Test
    @DisplayName("Should create franchise with id")
    void shouldCreateFranchiseWithId() {

        UUID id = UUID.randomUUID();


        Franchise franchise = Franchise.builder()
                .id(id)
                .name("McDonald's")
                .branches(new ArrayList<>())
                .build();

        assertEquals(id, franchise.getId());
        assertEquals("McDonald's", franchise.getName());
    }

    @Test
    @DisplayName("Should allow modifying franchise name")
    void shouldAllowModifyingFranchiseName() {

        Franchise franchise = Franchise.builder()
                .name("Old Name")
                .branches(new ArrayList<>())
                .build();

        franchise.setName("New Name");

        assertEquals("New Name", franchise.getName());
    }

    @Test
    @DisplayName("Should allow adding branches to list")
    void shouldAllowAddingBranches() {
        Franchise franchise = Franchise.builder()
                .name("KFC")
                .branches(new ArrayList<>())
                .build();

        Branch branch = Branch.builder()
                .name("Main Branch")
                .franchiseId(franchise.getId())
                .products(new ArrayList<>())
                .build();

        franchise.getBranches().add(branch);

        assertEquals(1, franchise.getBranches().size());
        assertTrue(franchise.getBranches().contains(branch));
    }

    @Test
    @DisplayName("Should allow adding multiple branches")
    void shouldAllowAddingMultipleBranches() {
        Franchise franchise = Franchise.builder()
                .name("KFC")
                .branches(new ArrayList<>())
                .build();

        Branch branch1 = Branch.builder()
                .name("Branch 1")
                .franchiseId(franchise.getId())
                .products(new ArrayList<>())
                .build();

        Branch branch2 = Branch.builder()
                .name("Branch 2")
                .franchiseId(franchise.getId())
                .products(new ArrayList<>())
                .build();

        franchise.getBranches().add(branch1);
        franchise.getBranches().add(branch2);

        assertEquals(2, franchise.getBranches().size());
        assertTrue(franchise.getBranches().contains(branch1));
        assertTrue(franchise.getBranches().contains(branch2));
    }
}

