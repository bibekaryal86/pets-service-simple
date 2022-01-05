package pets.service.app.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pets.service.app.connector.RefTypesConnector;
import pets.service.app.model.*;
import pets.service.app.util.CategoryHelper;
import pets.service.app.util.CategoryTypeHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefTypesService {

    public static void resetRefTypesCaches() {
        RefTypesConnector.resetRefTypesCache();
    }

    public static void setRefTypesCaches() {
        CompletableFuture.supplyAsync(RefTypesConnector::getAllAccountTypes);
        CompletableFuture.supplyAsync(RefTypesConnector::getAllBanks);
        CompletableFuture.supplyAsync(RefTypesConnector::getAllCategories);
        CompletableFuture.supplyAsync(RefTypesConnector::getAllCategoryTypes);
        CompletableFuture.supplyAsync(RefTypesConnector::getAllTransactionTypes);
    }

    public static RefAccountTypeResponse getAllAccountTypes() {
        try {
            return RefTypesConnector.getAllAccountTypes();
        } catch (Exception ex) {
            log.error("Exception in Get All Account Types", ex);
            return RefAccountTypeResponse.builder()
                    .refAccountTypes(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Account Types Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
    }

    public static RefBankResponse getAllBanks() {
        try {
            return RefTypesConnector.getAllBanks();
        } catch (Exception ex) {
            log.error("Exception in Get All Banks", ex);
            return RefBankResponse.builder()
                    .refBanks(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Banks Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
    }

    public static RefCategoryResponse getAllCategories(String username, RefCategoryFilters refCategoryFilters) {
        RefCategoryResponse categoryResponse;

        try {
            categoryResponse = RefTypesConnector.getAllCategories();
        } catch (Exception ex) {
            log.error("Exception in Get All Categories", ex);
            categoryResponse = RefCategoryResponse.builder()
                    .refCategories(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Categories Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }

        if (refCategoryFilters != null) {
            List<Transaction> transactions = new ArrayList<>();
            if (refCategoryFilters.isUsedInTxnsOnly()) {
                transactions = TransactionService.getTransactionsByUser(username, null, false)
                        .getTransactions();
            }

            categoryResponse = CategoryHelper.applyFilters(categoryResponse, refCategoryFilters, transactions);
        }

        if (!categoryResponse.getRefCategories().isEmpty()) {
            applyAllDetails(categoryResponse);
            categoryResponse = CategoryHelper.sortWithinRefCategoryType(categoryResponse);
        }

        return categoryResponse;
    }

    public static CompletableFuture<RefCategoryResponse> getAllCategoriesFuture() {
        return CompletableFuture.supplyAsync(() -> getAllCategories(null, null));
    }

    public static RefCategoryTypeResponse getAllCategoryTypes(String username, boolean usedInTxnsOnly) {
        RefCategoryTypeResponse refCategoryTypeResponse;

        try {
            refCategoryTypeResponse = RefTypesConnector.getAllCategoryTypes();
        } catch (Exception ex) {
            log.error("Exception in Get All Category Types", ex);
            refCategoryTypeResponse = RefCategoryTypeResponse.builder()
                    .refCategoryTypes(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Category Types Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }

        if (usedInTxnsOnly) {
            TransactionResponse transactionResponse = TransactionService.getTransactionsByUser(username, null, true);
            refCategoryTypeResponse = CategoryTypeHelper.applyUsedInTransactionsOnlyFilter(refCategoryTypeResponse.getRefCategoryTypes(), transactionResponse.getTransactions());
        }

        return refCategoryTypeResponse;
    }

    public static RefTransactionTypeResponse getAllTransactionTypes() {
        try {
            return RefTypesConnector.getAllTransactionTypes();
        } catch (Exception ex) {
            log.error("Exception in Get All Transaction Types", ex);
            return RefTransactionTypeResponse.builder()
                    .refTransactionTypes(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Transaction Types Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
    }

    public static CompletableFuture<RefTransactionTypeResponse> getAllTransactionTypesFuture() {
        return CompletableFuture.supplyAsync(RefTypesService::getAllTransactionTypes);
    }

    private static void applyAllDetails(RefCategoryResponse categoryResponse) {
        RefCategoryTypeResponse categoryTypeResponse = getAllCategoryTypes(null, false);
        CategoryHelper.applyAllDetailsStatic(categoryResponse, categoryTypeResponse.getRefCategoryTypes());
    }
}
