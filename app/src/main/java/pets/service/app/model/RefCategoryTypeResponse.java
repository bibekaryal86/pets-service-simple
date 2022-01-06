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
public class RefCategoryTypeResponse implements Serializable {
    private List<RefCategoryType> refCategoryTypes;
    private Long deleteCount;
    private Status status;
}
