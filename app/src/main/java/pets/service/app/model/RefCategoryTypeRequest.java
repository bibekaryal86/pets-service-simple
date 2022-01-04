package pets.service.app.model;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefCategoryTypeRequest implements Serializable {
    @NonNull
    private String description;
}
