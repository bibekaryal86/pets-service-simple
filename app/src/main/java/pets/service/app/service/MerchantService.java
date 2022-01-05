package pets.service.app.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pets.service.app.connector.MerchantConnector;
import pets.service.app.model.*;
import pets.service.app.util.MerchantHelper;
import pets.service.app.util.Util;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MerchantService {

    public static RefMerchantResponse getMerchantById(String id) {
        try {
            return MerchantConnector.getMerchantById(id);
        } catch (Exception ex) {
            log.error("Exception in Get Merchant by Id: {}", id);
            return RefMerchantResponse.builder()
                    .refMerchants(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Merchant Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
    }

    public static CompletableFuture<RefMerchantResponse> getMerchantsByUserFuture(String username) {
        return CompletableFuture.supplyAsync(() -> getMerchantsByUser(username, null));
    }

    public static RefMerchantResponse getMerchantsByUser(String username, RefMerchantFilters refMerchantFilters) {
        RefMerchantResponse refMerchantResponse;

        try {
            refMerchantResponse = MerchantConnector.getMerchantsByUser(username);
        } catch (Exception ex) {
            log.error("Exception in Get Merchant by Username: {}", username);
            refMerchantResponse = RefMerchantResponse.builder()
                    .refMerchants(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Merchant Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }

        if (!refMerchantResponse.getRefMerchants().isEmpty()) {
            MerchantHelper.setSystemDependentMerchants(refMerchantResponse);
            refMerchantResponse.setRefMerchantsFilterList(MerchantHelper.getRefMerchantsFilterListByName(refMerchantResponse));

            TransactionResponse transactionResponse = TransactionService.getTransactionsByUser(username, null, false);
            refMerchantResponse.setRefMerchantsNotUsedInTransactions(
                    MerchantHelper.getRefMerchantsNotUsedInTransactions(refMerchantResponse.getRefMerchants(),
                            transactionResponse.getTransactions()));

            if (refMerchantFilters != null) {
                refMerchantResponse = MerchantHelper.applyFilters(refMerchantResponse, refMerchantFilters,
                        transactionResponse.getTransactions());
            }
        }

        return refMerchantResponse;
    }

    public static RefMerchantResponse saveNewMerchant(RefMerchantRequest refMerchantRequest) {
        try {
            return MerchantConnector.saveNewMerchant(refMerchantRequest);
        } catch (Exception ex) {
            log.error("Exception in Save New Merchant: {}", refMerchantRequest);
            return RefMerchantResponse.builder()
                    .refMerchants(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Save Merchant Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
    }

    public static RefMerchantResponse deleteMerchant(String username, String id) {
        RefMerchantResponse refMerchantResponse;
        TransactionResponse transactionResponse = TransactionService.getTransactionsByUser(username, null, false);

        if (transactionResponse.getStatus() == null) {
            Transaction usedTransaction = transactionResponse.getTransactions().stream()
                    .filter(transaction -> transaction.getRefMerchant().getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (usedTransaction == null) {
                try {
                    refMerchantResponse = MerchantConnector.deleteMerchant(id);
                } catch (Exception ex) {
                    log.error("Exception in Delete Merchant: {}", id, ex);
                    refMerchantResponse = RefMerchantResponse.builder()
                            .refMerchants(Collections.emptyList())
                            .status(Status.builder()
                                    .errMsg("Delete Merchant Unavailable! Please Try Again!!!")
                                    .build())
                            .build();
                }
            } else {
                log.error("Delete Merchant Error, Merchant Used In Transactions: {} | {}", username, id);
                refMerchantResponse = RefMerchantResponse.builder()
                        .refMerchants(Collections.emptyList())
                        .status(Status.builder()
                                .errMsg("Delete Merchant Unavailable! Merchant Used in Transactions!! Please Try Again!!!")
                                .build())
                        .build();
            }
        } else {
            log.error("Delete Merchant Error, Empty Transactions List: {} | {}", username, id);
            refMerchantResponse = RefMerchantResponse.builder()
                    .refMerchants(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Delete Merchant Unavailable! Error Retrieving Transactions!! Please Try Again!!!")
                            .build())
                    .build();
        }

        return refMerchantResponse;
    }

    public static boolean isValidMerchantRequest(RefMerchantRequest refMerchantRequest) {
        return refMerchantRequest != null &&
                Util.hasText(refMerchantRequest.getUsername()) &&
                Util.hasText(refMerchantRequest.getDescription());
    }

    public RefMerchantResponse updateMerchant(String id, RefMerchantRequest merchantRequest) {
        try {
            return MerchantConnector.updateMerchant(id, merchantRequest);
        } catch (Exception ex) {
            log.error("Exception in Update Merchant: {} | {}", id, merchantRequest, ex);
            return RefMerchantResponse.builder()
                    .refMerchants(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Update Merchant Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
    }
}
