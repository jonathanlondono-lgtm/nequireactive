package co.com.bancolombia.model.franchise;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.model.enums.DomainExceptionMessage;
import co.com.bancolombia.model.exception.BranchException;
import co.com.bancolombia.model.exception.FranchiseException;
import co.com.bancolombia.model.product.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Franchise {

    private final UUID id;
    private String name;
    private final List<Branch> branches;

    private Franchise(UUID id, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new FranchiseException(DomainExceptionMessage.FRANCHISE_NAME_REQUIRED);
        }
        this.id = id != null ? id : UUID.randomUUID();
        this.name = name;
        this.branches = new ArrayList<>();
    }

    public static Franchise create(String name) {
        return new Franchise(null, name);
    }

    public static Franchise restore(UUID id, String name) {
        return new Franchise(id, name);
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public List<Branch> getBranches() { return Collections.unmodifiableList(branches); }

    public void rename(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new FranchiseException(DomainExceptionMessage.FRANCHISE_NAME_REQUIRED);
        }
        this.name = newName;
    }

    public void addBranch(Branch branch) {
        if (branch == null) throw new BranchException(DomainExceptionMessage.BRANCH_REQUIRED);
        branches.add(branch);
    }

    public List<Product> getHighestStockProductsByBranch() {
        List<Product> result = new ArrayList<>();
        for (Branch branch : branches) {
            Product topProduct = branch.getProductWithHighestStock();
            if (topProduct != null) result.add(topProduct);
        }
        return result;
    }
}
