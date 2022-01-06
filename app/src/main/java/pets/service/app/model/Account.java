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
public class Account implements Serializable {
    private String id;
    private RefAccountType refAccountType;
    private RefBank refBank;
    private String description;
    private User user;
    private BigDecimal openingBalance;
    private BigDecimal currentBalance;
    private String status;
    private String creationDate;
    private String lastModified;
}
