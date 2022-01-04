package pets.service.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefCategory implements Serializable {
    private String id;
    private String description;
    private RefCategoryType refCategoryType;
    private String creationDate;
    private String lastModified;
}
