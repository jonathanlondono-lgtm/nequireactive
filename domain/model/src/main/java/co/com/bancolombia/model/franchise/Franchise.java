package co.com.bancolombia.model.franchise;
import co.com.bancolombia.model.branch.Branch;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Franchise {
    private UUID id;
    private String name;
    private List<Branch> branches;
}