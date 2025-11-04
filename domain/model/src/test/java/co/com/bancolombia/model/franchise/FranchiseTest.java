package co.com.bancolombia.model.franchise;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.enums.DomainExceptionMessage;
import co.com.bancolombia.model.exception.BranchException;
import co.com.bancolombia.model.exception.FranchiseException;
import co.com.bancolombia.model.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FranchiseTest {

    @Test
    @DisplayName("Should create franchise with valid name")
    void shouldCreateFranchiseWithValidName() {
        // Act
        Franchise franchise = Franchise.create("KFC");

        // Assert
        assertNotNull(franchise);
        assertNotNull(franchise.getId());
        assertEquals("KFC", franchise.getName());
        assertTrue(franchise.getBranches().isEmpty());
    }

    @Test
    @DisplayName("Should throw exception when creating franchise with null name")
    void shouldThrowExceptionWhenNameIsNull() {
        // Act & Assert
        FranchiseException exception = assertThrows(
            FranchiseException.class,
            () -> Franchise.create(null)
        );

        assertEquals(DomainExceptionMessage.FRANCHISE_NAME_REQUIRED.getMessage(),
                     exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when creating franchise with empty name")
    void shouldThrowExceptionWhenNameIsEmpty() {
        // Act & Assert
        FranchiseException exception = assertThrows(
            FranchiseException.class,
            () -> Franchise.create("   ")
        );

        assertEquals(DomainExceptionMessage.FRANCHISE_NAME_REQUIRED.getMessage(),
                     exception.getMessage());
    }

    @Test
    @DisplayName("Should restore franchise with valid data")
    void shouldRestoreFranchiseWithValidData() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        Franchise franchise = Franchise.restore(id, "McDonald's");

        // Assert
        assertEquals(id, franchise.getId());
        assertEquals("McDonald's", franchise.getName());
        assertTrue(franchise.getBranches().isEmpty());
    }

    @Test
    @DisplayName("Should rename franchise successfully")
    void shouldRenameFranchiseSuccessfully() {
        // Arrange
        Franchise franchise = Franchise.create("Old Name");

        // Act
        franchise.rename("New Name");

        // Assert
        assertEquals("New Name", franchise.getName());
    }

    @Test
    @DisplayName("Should throw exception when renaming with null name")
    void shouldThrowExceptionWhenRenamingWithNull() {
        // Arrange
        Franchise franchise = Franchise.create("Franchise");

        // Act & Assert
        FranchiseException exception = assertThrows(
            FranchiseException.class,
            () -> franchise.rename(null)
        );

        assertEquals(DomainExceptionMessage.FRANCHISE_NAME_REQUIRED.getMessage(),
                     exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when renaming with empty name")
    void shouldThrowExceptionWhenRenamingWithEmpty() {
        // Arrange
        Franchise franchise = Franchise.create("Franchise");

        // Act & Assert
        assertThrows(FranchiseException.class, () -> franchise.rename("  "));
    }

    @Test
    @DisplayName("Should add branch successfully")
    void shouldAddBranchSuccessfully() {
        // Arrange
        Franchise franchise = Franchise.create("KFC");
        Branch branch = Branch.create("Main Branch");

        // Act
        franchise.addBranch(branch);

        // Assert
        assertEquals(1, franchise.getBranches().size());
        assertTrue(franchise.getBranches().contains(branch));
    }

    @Test
    @DisplayName("Should throw exception when adding null branch")
    void shouldThrowExceptionWhenAddingNullBranch() {
        // Arrange
        Franchise franchise = Franchise.create("KFC");

        // Act & Assert
        BranchException exception = assertThrows(
            BranchException.class,
            () -> franchise.addBranch(null)
        );

        assertEquals(DomainExceptionMessage.BRANCH_REQUIRED.getMessage(),
                     exception.getMessage());
    }

    @Test
    @DisplayName("Should add multiple branches successfully")
    void shouldAddMultipleBranches() {
        // Arrange
        Franchise franchise = Franchise.create("KFC");
        Branch branch1 = Branch.create("Branch 1");
        Branch branch2 = Branch.create("Branch 2");

        // Act
        franchise.addBranch(branch1);
        franchise.addBranch(branch2);

        // Assert
        assertEquals(2, franchise.getBranches().size());
    }

    @Test
    @DisplayName("Should get highest stock products by branch")
    void shouldGetHighestStockProductsByBranch() {
        // Arrange
        Franchise franchise = Franchise.create("KFC");

        Branch branch1 = Branch.create("Branch 1");
        Product product1 = Product.create("Laptop", 10);
        Product product2 = Product.create("Mouse", 50);
        branch1.addProduct(product1);
        branch1.addProduct(product2);

        Branch branch2 = Branch.create("Branch 2");
        Product product3 = Product.create("Keyboard", 30);
        branch2.addProduct(product3);

        franchise.addBranch(branch1);
        franchise.addBranch(branch2);

        // Act
        List<Product> result = franchise.getHighestStockProductsByBranch();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("Mouse") && p.getStock() == 50));
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("Keyboard") && p.getStock() == 30));
    }

    @Test
    @DisplayName("Should return empty list when no branches have products")
    void shouldReturnEmptyListWhenNoBranchesHaveProducts() {
        // Arrange
        Franchise franchise = Franchise.create("KFC");
        Branch branch = Branch.create("Empty Branch");
        franchise.addBranch(branch);

        // Act
        List<Product> result = franchise.getHighestStockProductsByBranch();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when franchise has no branches")
    void shouldReturnEmptyListWhenNoBranches() {
        // Arrange
        Franchise franchise = Franchise.create("KFC");

        // Act
        List<Product> result = franchise.getHighestStockProductsByBranch();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return immutable list of branches")
    void shouldReturnImmutableListOfBranches() {
        // Arrange
        Franchise franchise = Franchise.create("KFC");
        Branch branch = Branch.create("Branch");
        franchise.addBranch(branch);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                     () -> franchise.getBranches().add(Branch.create("Another Branch")));
    }
}

