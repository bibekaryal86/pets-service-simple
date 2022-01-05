package pets.service.app.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pets.service.app.connector.TransactionConnector;
import pets.service.app.model.*;
import pets.service.app.util.TransactionHelper;
import pets.service.app.util.Util;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionService {

    public static TransactionResponse getTransactionById(String username, String id, boolean applyAllDetails) {
        TransactionResponse transactionResponse;

        try {
            transactionResponse = TransactionConnector.getTransactionById(id);
        } catch (Exception ex) {
            log.error("Exception in Get Transaction by Id: {} | {} | {}", username, id, applyAllDetails);
            transactionResponse = TransactionResponse.builder()
                    .transactions(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Transaction Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }

        if (!transactionResponse.getTransactions().isEmpty() && applyAllDetails) {
            applyAllDetails(username, transactionResponse);
        }

        return transactionResponse;
    }

    public static TransactionResponse getTransactionsByUser(String username,
                                                            TransactionFilters transactionFilters,
                                                            boolean applyAllDetails) {
        TransactionResponse transactionResponse;

        try {
            transactionResponse = TransactionConnector.getTransactionsByUser(username);
        } catch (Exception ex) {
            log.error("Exception in Get Transactions By User: {} | {} | {}", username, transactionFilters, applyAllDetails);
            transactionResponse = TransactionResponse.builder()
                    .transactions(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Transactions Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }

        if (!transactionResponse.getTransactions().isEmpty()) {
            if (applyAllDetails) {
                applyAllDetails(username, transactionResponse);
            }

            if (transactionFilters != null) {
                transactionResponse = TransactionHelper.applyFilters(transactionResponse, transactionFilters);
            }
        }

        return transactionResponse;
    }

    public static TransactionResponse saveNewTransaction(String username,
                                                         TransactionRequest transactionRequest,
                                                         boolean applyAllDetails) {
        TransactionResponse transactionResponse;

        // check if user selected to enter a new merchant
        if (Util.hasText(transactionRequest.getNewMerchant())) {
            // save merchant first and set it as merchant id in transaction request
            RefMerchantResponse refMerchantResponse = MerchantService.saveNewMerchant(RefMerchantRequest.builder()
                    .username(username)
                    .description(transactionRequest.getNewMerchant())
                    .build());

            if (refMerchantResponse.getStatus() == null &&
                    !refMerchantResponse.getRefMerchants().isEmpty()) {
                String newMerchantId = refMerchantResponse.getRefMerchants().get(0).getId();
                log.info("Save New Transaction, New Merchant Id: {} | {}", username, newMerchantId);
                transactionRequest.setMerchantId(newMerchantId);
            } else {
                log.error("Error Saving New Merchant for Save Transaction: {} | {}", username, transactionRequest.getNewMerchant());
                return TransactionResponse.builder()
                        .transactions(Collections.emptyList())
                        .status(Status.builder()
                                .errMsg("Save Transaction Unavailable! New Merchant Not Saved!! Please Try Again!!!")
                                .build())
                        .build();
            }
        }

        try {
            transactionResponse = TransactionConnector.saveNewTransaction(transactionRequest);
        } catch (Exception ex) {
            log.error("Exception in Save New Transaction: {} | {} | {}", username, transactionRequest, applyAllDetails);
            transactionResponse = TransactionResponse.builder()
                    .transactions(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Save Transaction Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }

        if (!transactionResponse.getTransactions().isEmpty() &&
                applyAllDetails) {
            applyAllDetails(username, transactionResponse);
        }

        return transactionResponse;
    }

    public static TransactionResponse updateTransaction(String username, String id,
                                                        TransactionRequest transactionRequest,
                                                        boolean applyAllDetails) {
        TransactionResponse transactionResponse;

        // check if user selected to enter a new merchant
        if (Util.hasText(transactionRequest.getNewMerchant())) {
            // save merchant first and set it as merchant id in transaction request
            RefMerchantResponse merchantResponse = MerchantService.saveNewMerchant(RefMerchantRequest.builder()
                    .username(username)
                    .description(transactionRequest.getNewMerchant())
                    .build());

            if (merchantResponse.getStatus() == null &&
                    !merchantResponse.getRefMerchants().isEmpty()) {
                String newMerchantId = merchantResponse.getRefMerchants().get(0).getId();
                log.info("Update Transaction, New Merchant Id: {} | {}", username, newMerchantId);
                transactionRequest.setMerchantId(newMerchantId);
            } else {
                log.error("Error Saving New Merchant for Update Transaction: {} | {}", username, transactionRequest.getNewMerchant());
                return TransactionResponse.builder()
                        .transactions(Collections.emptyList())
                        .status(Status.builder()
                                .errMsg("Update Transaction Unavailable! New Merchant Not Saved!! Please Try Again!!!")
                                .build())
                        .build();
            }
        }

        try {
            transactionResponse = TransactionConnector.updateTransaction(id, transactionRequest);
        } catch (Exception ex) {
            log.error("Exception in Update Transaction: {} | {} | {} | {}", username, id, transactionRequest, applyAllDetails);
            transactionResponse = TransactionResponse.builder()
                    .transactions(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Update Transaction Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }

        if (!transactionResponse.getTransactions().isEmpty() && applyAllDetails) {
            applyAllDetails(username, transactionResponse);
        }

        return transactionResponse;
    }

    public static TransactionResponse deleteTransaction(String id) {
        try {
            return TransactionConnector.deleteTransaction(id);
        } catch (Exception ex) {
            log.error("Exception in Delete Transaction: {}", id);
            return TransactionResponse.builder()
                    .transactions(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Delete Transaction Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
    }

    public static TransactionResponse deleteTransactionsByAccount(String accountId) {
        try {
            return TransactionConnector.deleteTransactionsByAccount(accountId);
        } catch (Exception ex) {
            log.error("Exception in Delete Transactions by Account: {}", accountId);
            return TransactionResponse.builder()
                    .transactions(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Update Transaction by Account Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
    }

    private static void applyAllDetails(String username, TransactionResponse transactionResponse) {
        CompletableFuture<AccountResponse> accountResponseCompletableFuture = AccountService.getAccountsByUserFuture(username);
        CompletableFuture<RefCategoryResponse> refCategoryResponseCompletableFuture = RefTypesService.getAllCategoriesFuture();
        CompletableFuture<RefMerchantResponse> refMerchantResponseCompletableFuture = MerchantService.getMerchantsByUserFuture(username);
        CompletableFuture<RefTransactionTypeResponse> refTransactionTypeResponseCompletableFuture = RefTypesService.getAllTransactionTypesFuture();

        TransactionHelper.applyAllDetailsStatic(transactionResponse, accountResponseCompletableFuture, refCategoryResponseCompletableFuture,
                refMerchantResponseCompletableFuture, refTransactionTypeResponseCompletableFuture);
    }

    public static boolean isValidTransactionRequest(TransactionRequest transactionRequest) {
        return transactionRequest != null &&
                Util.hasText(transactionRequest.getAccountId()) &&
                Util.hasText(transactionRequest.getTypeId()) &&
                Util.hasText(transactionRequest.getCategoryId()) &&
                (Util.hasText(transactionRequest.getMerchantId()) ||
                        Util.hasText(transactionRequest.getNewMerchant())) &&
                Util.hasText(transactionRequest.getUsername()) &&
                Util.hasText(transactionRequest.getDate());
    }
}
