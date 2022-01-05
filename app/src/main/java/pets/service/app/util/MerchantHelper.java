package pets.service.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pets.service.app.model.RefMerchant;
import pets.service.app.model.RefMerchantFilters;
import pets.service.app.model.RefMerchantResponse;
import pets.service.app.model.Transaction;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static java.lang.Character.isAlphabetic;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MerchantHelper {

    public static RefMerchantResponse applyFilters(RefMerchantResponse refMerchantResponse, RefMerchantFilters refMerchantFilters,
                                                   List<Transaction> transactions) {
        List<RefMerchant> merchants = refMerchantResponse.getRefMerchants();
        Set<String> refMerchantsFilterList = refMerchantResponse.getRefMerchantsFilterList();

        if (refMerchantFilters.isNotUsedInTransactionsOnly() && !transactions.isEmpty()) {
            merchants = applyMerchantNotUsedInTransactionFilter(merchants, transactions);
        } else if (Util.hasText(refMerchantFilters.getFirstChar())) {
            merchants = applyMerchantNameBeginsWithFilter(merchants, refMerchantFilters.getFirstChar().charAt(0));
        }

        return RefMerchantResponse.builder()
                .refMerchants(merchants)
                .refMerchantsFilterList(refMerchantsFilterList)
                .build();
    }

    public static Set<String> getRefMerchantsFilterListByName(RefMerchantResponse refMerchantResponse) {
        Set<String> firstLetters = new TreeSet<>();

        refMerchantResponse.getRefMerchants()
                .forEach(refMerchant -> {
                    if (isAlphabetic(refMerchant.getDescription().charAt(0))) {
                        firstLetters.add(String.valueOf(refMerchant.getDescription().charAt(0)));
                    } else {
                        firstLetters.add("#");
                    }
                });

        return firstLetters;
    }

    public static void setSystemDependentMerchants(RefMerchantResponse refMerchantResponse) {
        refMerchantResponse.getRefMerchants()
                .forEach(refMerchant -> {
                    if (Util.SYSTEM_DEPENDENT_MERCHANTS.contains(refMerchant.getId())) {
                        refMerchant.setNotEditable(true);
                    }
                });
    }

    public static List<RefMerchant> getRefMerchantsNotUsedInTransactions(List<RefMerchant> merchantList,
                                                                         List<Transaction> transactions) {
        return applyMerchantNotUsedInTransactionFilter(merchantList, transactions);
    }


    private static List<RefMerchant> applyMerchantNameBeginsWithFilter(List<RefMerchant> merchantList, char firstChar) {
        if (isAlphabetic(firstChar)) {
            return merchantList.stream()
                    .filter(refMerchant -> refMerchant.getDescription().charAt(0) == firstChar)
                    .collect(toList());
        } else {
            return merchantList.stream()
                    .filter(refMerchant -> !isAlphabetic(refMerchant.getDescription().charAt(0)))
                    .collect(toList());
        }
    }

    private static List<RefMerchant> applyMerchantNotUsedInTransactionFilter(List<RefMerchant> merchantList,
                                                                             List<Transaction> transactions) {
        Set<String> merchantIdsInTransactions = transactions.stream()
                .map(transaction -> transaction.getRefMerchant().getId())
                .collect(toSet());

        return merchantList.stream()
                .filter(refMerchant -> !merchantIdsInTransactions.contains(refMerchant.getId()))
                .collect(toList());
    }
}
