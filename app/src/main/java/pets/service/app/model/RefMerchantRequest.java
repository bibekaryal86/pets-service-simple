package pets.service.app.model;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefMerchantRequest implements Serializable {
    @NonNull
    private String description;
    @NonNull
    private String username;
}
