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
public class RefTransactionTypeResponse implements Serializable {
    private List<RefTransactionType> refTransactionTypes;
    private Long deleteCount;
    private Status status;
}
