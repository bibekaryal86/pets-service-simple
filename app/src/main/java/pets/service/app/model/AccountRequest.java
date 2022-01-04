package pets.service.app.model;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest implements Serializable {
    @NonNull
    private String typeId;
    @NonNull
    private String bankId;
    @NonNull
    private String description;
    @NonNull
    private BigDecimal openingBalance;
    @NonNull
    private String status;
    @NonNull
    private String username;
}
