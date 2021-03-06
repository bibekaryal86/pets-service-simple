package pets.service.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefCategoryRequest implements Serializable {
    @NonNull
    private String description;
    @NonNull
    private String categoryTypeId;
}
