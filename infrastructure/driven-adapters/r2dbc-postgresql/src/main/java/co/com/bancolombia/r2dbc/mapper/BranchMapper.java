package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.r2dbc.entity.BranchEntity;

import java.util.ArrayList;

public final class BranchMapper {

    private BranchMapper() {}

    public static Branch toDomain(BranchEntity entity) {
        if (entity == null) return null;
        return Branch.builder()
                .id(entity.getId())
                .name(entity.getName())
                .franchiseId(entity.getFranchiseId())
                .products(new ArrayList<>())
                .build();
    }

    public static BranchEntity toEntity(Branch branch) {
        if (branch == null) return null;
        return BranchEntity.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .build();
    }
}

