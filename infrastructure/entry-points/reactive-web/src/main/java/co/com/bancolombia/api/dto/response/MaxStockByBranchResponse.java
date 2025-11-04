package co.com.bancolombia.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaxStockByBranchResponse {
    private UUID branchId;
    private String branchName;
    private UUID productId;
    private String productName;
    private int stock;
}
