package pets.service.app.model;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefBankRequest implements Serializable {
    @NonNull
    private String description;
}
