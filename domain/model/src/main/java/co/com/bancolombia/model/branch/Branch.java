package co.com.bancolombia.model.branch;

import co.com.bancolombia.model.enums.DomainExceptionMessage;
import co.com.bancolombia.model.exception.BranchException;
import co.com.bancolombia.model.exception.ProductException;
import co.com.bancolombia.model.product.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Branch {

    private final UUID id;
    private String name;
    private final List<Product> products;

    private Branch(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new BranchException(DomainExceptionMessage.BRANCH_NAME_REQUIRED);
        }
        this.id = UUID.randomUUID();
        this.name = name;
        this.products = new ArrayList<>();
    }

    public static Branch create(String name) {
        return new Branch(name);
    }
    public static Branch restore(UUID id, String name) {
        return new Branch(id, name);
    }
    private Branch(UUID id, String name) {
        if (id == null ) {
            throw new BranchException(DomainExceptionMessage.BRANCH_ID_REQUIRED);
        }
        if (name == null || name.trim().isEmpty()) {
            throw new BranchException(DomainExceptionMessage.BRANCH_NAME_REQUIRED);
        }
        this.id = id;
        this.name = name;
        this.products = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public void rename(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new BranchException(DomainExceptionMessage.BRANCH_NAME_REQUIRED);
        }
        this.name = newName;
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new ProductException(DomainExceptionMessage.PRODUCT_REQUIRED);
        }
        products.add(product);
    }

    public void removeProduct(String productId) {
        products.removeIf(p -> p.getId().equals(productId));
    }

    public void updateProductStock(String productId, int newStock) {
        Product product = products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductException(DomainExceptionMessage.PRODUCT_NOT_FOUND, productId));
        product.updateStock(newStock);
    }

    public Product getProductWithHighestStock() {
        return products.stream()
                .max((p1, p2) -> Integer.compare(p1.getStock(), p2.getStock()))
                .orElse(null);
    }
}
