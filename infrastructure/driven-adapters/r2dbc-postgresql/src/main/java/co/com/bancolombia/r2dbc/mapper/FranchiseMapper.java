package co.com.bancolombia.r2dbc.mapper;
import co.com.bancolombia.model.franchise.Franchise;
import co.com.bancolombia.r2dbc.entity.FranchiseEntity;

public final class FranchiseMapper {

    private FranchiseMapper() {
        // Evita instanciación
    }

    public static Franchise toDomain(FranchiseEntity entity) {
        if (entity == null) return null;
        return Franchise.restore(entity.getId(), entity.getName());
    }

    public static FranchiseEntity toEntity(Franchise franchise) {
        if (franchise == null) return null;
        return FranchiseEntity.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();
    }
}