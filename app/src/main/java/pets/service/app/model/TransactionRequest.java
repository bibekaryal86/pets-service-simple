package pets.service.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest implements Serializable {
    private String description;
    @NonNull
    private String accountId;
    private String trfAccountId;
    @NonNull
    private String typeId;
    @NonNull
    private String categoryId;
    private String merchantId;
    private String newMerchant;
    @NonNull
    private String username;
    @NonNull
    private String date;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private Boolean regular;
    @NonNull
    private Boolean necessary;
}
