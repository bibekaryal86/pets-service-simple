package pets.service.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pets.service.app.model.RefCategoryType;
import pets.service.app.model.RefCategoryTypeResponse;
import pets.service.app.model.Transaction;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryTypeHelper {

    public static RefCategoryTypeResponse applyUsedInTransactionsOnlyFilter(List<RefCategoryType> refCategoryTypes,
                                                                            List<Transaction> transactions) {
        Set<String> usedCategoryTypes = transactions.stream()
                .map(transaction -> transaction.getRefCategory().getRefCategoryType().getId())
                .collect(toSet());

        List<RefCategoryType> filteredRefCategoryTypesList = refCategoryTypes.stream()
                .filter(refCategoryType -> usedCategoryTypes.contains(refCategoryType.getId()))
                .collect(toList());

        return RefCategoryTypeResponse.builder()
                .refCategoryTypes(filteredRefCategoryTypesList)
                .build();
    }
}
