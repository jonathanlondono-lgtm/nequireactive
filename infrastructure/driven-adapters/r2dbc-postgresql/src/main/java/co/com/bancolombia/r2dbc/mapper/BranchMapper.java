package co.com.bancolombia.r2dbc.mapper;


import co.com.bancolombia.model.branch.Branch;
import co.com.bancolombia.r2dbc.entity.BranchEntity;

import java.util.UUID;

public final class BranchMapper {

    private BranchMapper() {}

    public static Branch toDomain(BranchEntity e) {
        if (e == null) return null;
        return Branch.restore(e.getId().toString(), e.getName());
    }

    public static BranchEntity toEntity(Branch b, UUID franchiseId) {
        if (b == null) return null;
        UUID id = b.getId() != null ? UUID.fromString(b.getId()) : UUID.randomUUID();
        return BranchEntity.builder()
                .id(id)
                .franchiseId(franchiseId)
                .name(b.getName())
                .build();
    }

    // helper to create entity from just name (for insert)
    public static BranchEntity toNewEntity(String name, UUID franchiseId) {
        return BranchEntity.builder()
                .id(UUID.randomUUID())
                .franchiseId(franchiseId)
                .name(name)
                .build();
    }
}
