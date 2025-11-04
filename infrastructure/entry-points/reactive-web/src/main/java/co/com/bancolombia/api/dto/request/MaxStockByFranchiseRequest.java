package co.com.bancolombia.api.dto.request;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaxStockByFranchiseRequest {
    private UUID franchiseId;
}