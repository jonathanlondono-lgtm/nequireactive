package co.com.bancolombia.model.branch;

import co.com.bancolombia.model.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BranchTest {

    @Test
    @DisplayName("Should create branch with builder")
    void shouldCreateBranchWithBuilder() {
        Branch branch = Branch.builder()
                .name("Main Branch")
                .franchiseId(UUID.randomUUID())
                .products(new ArrayList<>())
                .build();

        assertNotNull(branch);
        assertEquals("Main Branch", branch.getName());
        assertNotNull(branch.getFranchiseId());
        assertNotNull(branch.getProducts());
        assertTrue(branch.getProducts().isEmpty());
    }

    @Test
    @DisplayName("Should create branch with id")
    void shouldCreateBranchWithId() {
        UUID id = UUID.randomUUID();
        UUID franchiseId = UUID.randomUUID();

        Branch branch = Branch.builder()
                .id(id)
                .name("Restored Branch")
                .franchiseId(franchiseId)
                .products(new ArrayList<>())
                .build();

        assertEquals(id, branch.getId());
        assertEquals("Restored Branch", branch.getName());
        assertEquals(franchiseId, branch.getFranchiseId());
    }

    @Test
    @DisplayName("Should allow modifying branch name")
    void shouldAllowModifyingBranchName() {
        Branch branch = Branch.builder()
                .name("Old Name")
                .franchiseId(UUID.randomUUID())
                .products(new ArrayList<>())
                .build();

        branch.setName("New Name");

        assertEquals("New Name", branch.getName());
    }

    @Test
    @DisplayName("Should allow adding products to list")
    void shouldAllowAddingProducts() {
        Branch branch = Branch.builder()
                .name("Branch")
                .franchiseId(UUID.randomUUID())
                .products(new ArrayList<>())
                .build();

        Product product = Product.builder()
                .name("Laptop")
                .stock(10)
                .branchId(branch.getFranchiseId())
                .build();

        branch.getProducts().add(product);

        assertEquals(1, branch.getProducts().size());
        assertTrue(branch.getProducts().contains(product));
    }
}

