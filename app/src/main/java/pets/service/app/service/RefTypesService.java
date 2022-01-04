package pets.service.app.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pets.service.app.connector.RefTypesConnector;
import pets.service.app.model.*;

import java.util.Collections;
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
            return RefTypesConnector.getAllCategories();
        } catch (Exception ex) {
            log.error("Exception in Get All Categories", ex);
            return RefCategoryResponse.builder()
                    .refCategories(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Categories Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
    }

    public static RefCategoryTypeResponse getAllCategoryTypes(String username, boolean usedInTxnsOnly) {
        RefCategoryTypeResponse refCategoryTypeResponse;
        try {
           return RefTypesConnector.getAllCategoryTypes();
        } catch (Exception ex) {
            log.error("Exception in Get All Category Types", ex);
            return RefCategoryTypeResponse.builder()
                    .refCategoryTypes(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Category Types Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
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
}
