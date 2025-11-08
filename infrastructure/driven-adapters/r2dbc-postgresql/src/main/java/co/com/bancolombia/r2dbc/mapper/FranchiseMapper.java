package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.r2dbc.entity.FranchiseEntity;

import java.util.ArrayList;

public final class FranchiseMapper {

    private FranchiseMapper() {
    }

    public static Franchise toDomain(FranchiseEntity entity) {
        if (entity == null) return null;
        return Franchise.builder()
                .id(entity.getId())
                .name(entity.getName())
                .branches(new ArrayList<>())
                .build();
    }

    public static FranchiseEntity toEntity(Franchise franchise) {
        if (franchise == null) return null;
        return FranchiseEntity.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();
    }
}

