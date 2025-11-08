package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.request.BranchDTO;
import co.com.bancolombia.api.dto.request.UpdateBranchNameRequest;
import co.com.bancolombia.api.dto.response.CreateBranchResponse;
import co.com.bancolombia.api.dto.response.UpdateBranchNameResponse;
import co.com.bancolombia.model.branch.Branch;

import java.util.ArrayList;
import java.util.UUID;

public final class BranchApiMapper {

    private BranchApiMapper() {
    }

    public static Branch toDomain(BranchDTO dto, UUID franchiseId) {
        if (dto == null) return null;
        return Branch.builder()
                .name(dto.getName())
                .franchiseId(franchiseId)
                .products(new ArrayList<>())
                .build();
    }

    public static Branch toDomain(UpdateBranchNameRequest request) {
        if (request == null) return null;
        return Branch.builder()
                .id(request.getBranchId())
                .name(request.getNewName())
                .products(new ArrayList<>())
                .build();
    }

    public static CreateBranchResponse toCreateResponse(Branch branch) {
        if (branch == null) return null;
        return new CreateBranchResponse(
                branch.getId(),
                branch.getName()
        );
    }

    public static UpdateBranchNameResponse toUpdateNameResponse(Branch branch) {
        if (branch == null) return null;
        return new UpdateBranchNameResponse(
                branch.getId(),
                branch.getName()
        );
    }
}

