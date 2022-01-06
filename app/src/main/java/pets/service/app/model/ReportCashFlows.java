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
public class ReportCashFlows implements Serializable {
    private String month;
    private int monthToSort;
    private String monthBeginDate;
    private String monthEndDate;
    private BigDecimal totalIncomes;
    private BigDecimal totalExpenses;
    private BigDecimal netSavings;
}
