package co.com.bancolombia.model.branch;

import co.com.bancolombia.model.enums.DomainExceptionMessage;
import co.com.bancolombia.model.exception.BranchException;
import co.com.bancolombia.model.exception.ProductException;
import co.com.bancolombia.model.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BranchTest {

    @Test
    @DisplayName("Should create branch with valid name")
    void shouldCreateBranchWithValidName() {
        // Act
        Branch branch = Branch.create("Main Branch");

        // Assert
        assertNotNull(branch);
        assertNotNull(branch.getId());
        assertEquals("Main Branch", branch.getName());
        assertTrue(branch.getProducts().isEmpty());
    }

    @Test
    @DisplayName("Should throw exception when creating branch with null name")
    void shouldThrowExceptionWhenNameIsNull() {
        // Act & Assert
        BranchException exception = assertThrows(
            BranchException.class,
            () -> Branch.create(null)
        );

        assertEquals(DomainExceptionMessage.BRANCH_NAME_REQUIRED.getMessage(),
                     exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when creating branch with empty name")
    void shouldThrowExceptionWhenNameIsEmpty() {
        // Act & Assert
        BranchException exception = assertThrows(
            BranchException.class,
            () -> Branch.create("   ")
        );

        assertEquals(DomainExceptionMessage.BRANCH_NAME_REQUIRED.getMessage(),
                     exception.getMessage());
    }

    @Test
    @DisplayName("Should restore branch with valid data")
    void shouldRestoreBranchWithValidData() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        Branch branch = Branch.restore(id, "Restored Branch");

        // Assert
        assertEquals(id, branch.getId());
        assertEquals("Restored Branch", branch.getName());
        assertTrue(branch.getProducts().isEmpty());
    }

    @Test
    @DisplayName("Should throw exception when restoring branch with null id")
    void shouldThrowExceptionWhenRestoringWithNullId() {
        // Act & Assert
        assertThrows(BranchException.class, () -> Branch.restore(null, "Branch"));
    }

    @Test
    @DisplayName("Should rename branch successfully")
    void shouldRenameBranchSuccessfully() {
        // Arrange
        Branch branch = Branch.create("Old Name");

        // Act
        branch.rename("New Name");

        // Assert
        assertEquals("New Name", branch.getName());
    }

    @Test
    @DisplayName("Should throw exception when renaming with null name")
    void shouldThrowExceptionWhenRenamingWithNull() {
        // Arrange
        Branch branch = Branch.create("Branch");

        // Act & Assert
        BranchException exception = assertThrows(
            BranchException.class,
            () -> branch.rename(null)
        );

        assertEquals(DomainExceptionMessage.BRANCH_NAME_REQUIRED.getMessage(),
                     exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when renaming with empty name")
    void shouldThrowExceptionWhenRenamingWithEmpty() {
        // Arrange
        Branch branch = Branch.create("Branch");

        // Act & Assert
        assertThrows(BranchException.class, () -> branch.rename("  "));
    }

    @Test
    @DisplayName("Should add product successfully")
    void shouldAddProductSuccessfully() {
        // Arrange
        Branch branch = Branch.create("Branch");
        Product product = Product.create("Laptop", 10);

        // Act
        branch.addProduct(product);

        // Assert
        assertEquals(1, branch.getProducts().size());
        assertTrue(branch.getProducts().contains(product));
    }

    @Test
    @DisplayName("Should throw exception when adding null product")
    void shouldThrowExceptionWhenAddingNullProduct() {
        // Arrange
        Branch branch = Branch.create("Branch");

        // Act & Assert
        ProductException exception = assertThrows(
            ProductException.class,
            () -> branch.addProduct(null)
        );

        assertEquals(DomainExceptionMessage.PRODUCT_REQUIRED.getMessage(),
                     exception.getMessage());
    }

    @Test
    @DisplayName("Should remove product successfully")
    void shouldRemoveProductSuccessfully() {
        // Arrange
        Branch branch = Branch.create("Branch");
        Product product = Product.create("Laptop", 10);
        branch.addProduct(product);

        // Act
        branch.removeProduct(product.getId());

        // Assert
        assertEquals(0, branch.getProducts().size());
    }

    @Test
    @DisplayName("Should get product with highest stock")
    void shouldGetProductWithHighestStock() {
        // Arrange
        Branch branch = Branch.create("Branch");
        Product product1 = Product.create("Laptop", 10);
        Product product2 = Product.create("Mouse", 50);
        Product product3 = Product.create("Keyboard", 25);
        branch.addProduct(product1);
        branch.addProduct(product2);
        branch.addProduct(product3);

        // Act
        Product result = branch.getProductWithHighestStock();

        // Assert
        assertNotNull(result);
        assertEquals("Mouse", result.getName());
        assertEquals(50, result.getStock());
    }

    @Test
    @DisplayName("Should return null when getting highest stock from empty products")
    void shouldReturnNullWhenNoProducts() {
        // Arrange
        Branch branch = Branch.create("Branch");

        // Act
        Product result = branch.getProductWithHighestStock();

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should return immutable list of products")
    void shouldReturnImmutableListOfProducts() {
        // Arrange
        Branch branch = Branch.create("Branch");
        Product product = Product.create("Laptop", 10);
        branch.addProduct(product);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class,
                     () -> branch.getProducts().add(Product.create("Mouse", 5)));
    }
}
