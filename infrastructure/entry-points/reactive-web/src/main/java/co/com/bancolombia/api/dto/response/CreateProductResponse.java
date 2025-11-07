package co.com.bancolombia.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CreateProductResponse {
    private UUID id;
    private String name;
    private int stock;
    private UUID branchId;
}

