package pets.service.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportsResponse implements Serializable {
    private List<ReportCurrentBalances> reportCurrentBalances;
    private List<ReportCashFlows> reportCashFlows;
    private List<ReportCategoryTypes> reportCategoryTypes;
    private Status status;
}
