package pets.service.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefMerchantResponse implements Serializable {
    private List<RefMerchant> refMerchants;
    private Long deleteCount;
    private Set<String> refMerchantsFilterList;
    private List<RefMerchant> refMerchantsNotUsedInTransactions;
    private Status status;
}
