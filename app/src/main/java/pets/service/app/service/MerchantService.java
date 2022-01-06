package pets.service.app.service;

import lombok.extern.slf4j.Slf4j;
import pets.service.app.connector.MerchantConnector;
import pets.service.app.model.RefMerchantFilters;
import pets.service.app.model.RefMerchantRequest;
import pets.service.app.model.RefMerchantResponse;
import pets.service.app.model.Status;
import pets.service.app.model.Transaction;
import pets.service.app.model.TransactionResponse;

import java.util.concurrent.CompletableFuture;

import static java.util.Collections.emptyList;
import static pets.service.app.util.MerchantHelper.applyFilters;
import static pets.service.app.util.MerchantHelper.getRefMerchantsFilterListByName;
import static pets.service.app.util.MerchantHelper.getRefMerchantsNotUsedInTransactions;
import static pets.service.app.util.MerchantHelper.setSystemDependentMerchants;

@Slf4j
public class MerchantService {

    public RefMerchantResponse getMerchantById(String id) {
        try {
            return new MerchantConnector().getMerchantById(id);
        } catch (Exception ex) {
            log.error("Exception in Get Merchant by Id: {}", id);
            return RefMerchantResponse.builder()
                    .refMerchants(emptyList())
                    .status(Status.builder()
                            .errMsg("Merchant Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
    }

    public CompletableFuture<RefMerchantResponse> getMerchantsByUserFuture(String username) {
        return CompletableFuture.supplyAsync(() -> getMerchantsByUser(username, null));
    }

    public RefMerchantResponse getMerchantsByUser(String username, RefMerchantFilters refMerchantFilters) {
        RefMerchantResponse refMerchantResponse;

        try {
            refMerchantResponse = new MerchantConnector().getMerchantsByUser(username);
        } catch (Exception ex) {
            log.error("Exception in Get Merchant by Username: {}", username);
            refMerchantResponse = RefMerchantResponse.builder()
                    .refMerchants(emptyList())
                    .status(Status.builder()
                            .errMsg("Merchant Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }

        if (!refMerchantResponse.getRefMerchants().isEmpty()) {
            setSystemDependentMerchants(refMerchantResponse);
            refMerchantResponse.setRefMerchantsFilterList(getRefMerchantsFilterListByName(refMerchantResponse));

            TransactionResponse transactionResponse = new TransactionService().getTransactionsByUser(username, null, false);
            refMerchantResponse.setRefMerchantsNotUsedInTransactions(
                    getRefMerchantsNotUsedInTransactions(refMerchantResponse.getRefMerchants(),
                            transactionResponse.getTransactions()));

            if (refMerchantFilters != null) {
                refMerchantResponse = applyFilters(refMerchantResponse, refMerchantFilters,
                        transactionResponse.getTransactions());
            }
        }

        return refMerchantResponse;
    }

    public RefMerchantResponse saveNewMerchant(RefMerchantRequest refMerchantRequest) {
        try {
            return new MerchantConnector().saveNewMerchant(refMerchantRequest);
        } catch (Exception ex) {
            log.error("Exception in Save New Merchant: {}", refMerchantRequest);
            return RefMerchantResponse.builder()
                    .refMerchants(emptyList())
                    .status(Status.builder()
                            .errMsg("Save Merchant Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
    }

    public RefMerchantResponse deleteMerchant(String username, String id) {
        RefMerchantResponse refMerchantResponse;
        TransactionResponse transactionResponse = new TransactionService().getTransactionsByUser(username, null, false);

        if (transactionResponse.getStatus() == null) {
            Transaction usedTransaction = transactionResponse.getTransactions().stream()
                    .filter(transaction -> transaction.getRefMerchant().getId().equals(id))
                    .findFirst()
                    .orElse(null);

            if (usedTransaction == null) {
                try {
                    refMerchantResponse = new MerchantConnector().deleteMerchant(id);
                } catch (Exception ex) {
                    log.error("Exception in Delete Merchant: {}", id, ex);
                    refMerchantResponse = RefMerchantResponse.builder()
                            .refMerchants(emptyList())
                            .status(Status.builder()
                                    .errMsg("Delete Merchant Unavailable! Please Try Again!!!")
                                    .build())
                            .build();
                }
            } else {
                log.error("Delete Merchant Error, Merchant Used In Transactions: {} | {}", username, id);
                refMerchantResponse = RefMerchantResponse.builder()
                        .refMerchants(emptyList())
                        .status(Status.builder()
                                .errMsg("Delete Merchant Unavailable! Merchant Used in Transactions!! Please Try Again!!!")
                                .build())
                        .build();
            }
        } else {
            log.error("Delete Merchant Error, Empty Transactions List: {} | {}", username, id);
            refMerchantResponse = RefMerchantResponse.builder()
                    .refMerchants(emptyList())
                    .status(Status.builder()
                            .errMsg("Delete Merchant Unavailable! Error Retrieving Transactions!! Please Try Again!!!")
                            .build())
                    .build();
        }

        return refMerchantResponse;
    }

    public RefMerchantResponse updateMerchant(String id, RefMerchantRequest merchantRequest) {
        try {
            return new MerchantConnector().updateMerchant(id, merchantRequest);
        } catch (Exception ex) {
            log.error("Exception in Update Merchant: {} | {}", id, merchantRequest, ex);
            return RefMerchantResponse.builder()
                    .refMerchants(emptyList())
                    .status(Status.builder()
                            .errMsg("Update Merchant Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
    }
}
