package co.com.bancolombia.model.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("Should create product with builder")
    void shouldCreateProductWithBuilder() {
        Product product = Product.builder()
                .name("Laptop")
                .stock(10)
                .branchId(UUID.randomUUID())
                .build();

        assertNotNull(product);
        assertEquals("Laptop", product.getName());
        assertEquals(10, product.getStock());
        assertNotNull(product.getBranchId());
    }

    @Test
    @DisplayName("Should create product with id")
    void shouldCreateProductWithId() {
        UUID id = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();

        Product product = Product.builder()
                .id(id)
                .name("Mouse")
                .stock(20)
                .branchId(branchId)
                .build();

        assertEquals(id, product.getId());
        assertEquals("Mouse", product.getName());
        assertEquals(20, product.getStock());
        assertEquals(branchId, product.getBranchId());
    }

    @Test
    @DisplayName("Should allow modifying product name")
    void shouldAllowModifyingProductName() {
        Product product = Product.builder()
                .name("Old Name")
                .stock(5)
                .branchId(UUID.randomUUID())
                .build();

        product.setName("New Name");

        assertEquals("New Name", product.getName());
    }

    @Test
    @DisplayName("Should allow modifying product stock")
    void shouldAllowModifyingProductStock() {
        Product product = Product.builder()
                .name("Product")
                .stock(10)
                .branchId(UUID.randomUUID())
                .build();

        product.setStock(25);

        assertEquals(25, product.getStock());
    }

    @Test
    @DisplayName("Should allow zero stock")
    void shouldAllowZeroStock() {
        Product product = Product.builder()
                .name("Product")
                .stock(0)
                .branchId(UUID.randomUUID())
                .build();

        assertEquals(0, product.getStock());
    }
}

