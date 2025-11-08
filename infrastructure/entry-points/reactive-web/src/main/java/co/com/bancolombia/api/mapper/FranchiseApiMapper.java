package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.request.FranchiseDTO;
import co.com.bancolombia.api.dto.request.UpdateFranchiseNameRequest;
import co.com.bancolombia.api.dto.response.FranchiseResponse;
import co.com.bancolombia.api.dto.response.UpdateFranchiseNameResponse;
import co.com.bancolombia.model.franchise.Franchise;

import java.util.ArrayList;

public final class FranchiseApiMapper {

    private FranchiseApiMapper() {
    }

    public static Franchise toDomain(FranchiseDTO dto) {
        if (dto == null) return null;
        return Franchise.builder()
                .name(dto.getName())
                .branches(new ArrayList<>())
                .build();
    }

    public static Franchise toDomain(UpdateFranchiseNameRequest request) {
        if (request == null) return null;
        return Franchise.builder()
                .id(request.getFranchiseId())
                .name(request.getNewName())
                .branches(new ArrayList<>())
                .build();
    }

    public static FranchiseResponse toResponse(Franchise franchise) {
        if (franchise == null) return null;
        return new FranchiseResponse(
                franchise.getId(),
                franchise.getName()
        );
    }

    public static UpdateFranchiseNameResponse toUpdateNameResponse(Franchise franchise) {
        if (franchise == null) return null;
        return new UpdateFranchiseNameResponse(
                franchise.getId(),
                franchise.getName()
        );
    }
}

