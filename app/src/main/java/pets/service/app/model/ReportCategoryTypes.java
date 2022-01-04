package pets.service.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportCategoryTypes implements Serializable {
    private RefCategoryType refCategoryType;
    private BigDecimal totalRefCategoryType;
    private List<ReportCategories> reportCategories;
}
