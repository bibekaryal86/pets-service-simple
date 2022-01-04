package pets.service.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFilters implements Serializable {
    private String accountId;
    private String accountTypeId;
    private BigDecimal amountFrom;
    private BigDecimal amountTo;
    private String bankId;
    private String categoryId;
    private String categoryTypeId;
    private String dateFrom;
    private String dateTo;
    private String merchantId;
    private Boolean necessary;
    private Boolean regular;
    private String transactionTypeId;
}
