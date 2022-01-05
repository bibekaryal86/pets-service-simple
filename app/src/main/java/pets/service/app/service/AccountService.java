package pets.service.app.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pets.service.app.connector.AccountConnector;
import pets.service.app.model.*;
import pets.service.app.util.AccountHelper;
import pets.service.app.util.Util;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountService {

    public static AccountResponse getAccountById(String username, String id, boolean applyAllDetails) {
        AccountResponse accountResponse;

        try {
            accountResponse = AccountConnector.getAccountById(id);
        } catch (Exception ex) {
            log.error("Exception in Get Account by Id: {}", id);
            accountResponse = AccountResponse.builder()
                    .accounts(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Account Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }

        if (!accountResponse.getAccounts().isEmpty()) {
            if (applyAllDetails) {
                applyAllDetails(accountResponse);
            }

            calculateCurrentBalance(username, accountResponse);
        }

        return accountResponse;
    }

    public static CompletableFuture<AccountResponse> getAccountsByUserFuture(String username) {
        return CompletableFuture.supplyAsync(() -> getAccountsByUser(username, null, true));
    }

    public static AccountResponse getAccountsByUser(String username, AccountFilters accountFilters, boolean applyAllDetails) {
        AccountResponse accountResponse;

        try {
            accountResponse = AccountConnector.getAccountsByUser(username);
        } catch (Exception ex) {
            log.error("Exception in Get Accounts By User: {} | {} | {}", username, accountFilters, applyAllDetails);
            accountResponse = AccountResponse.builder()
                    .accounts(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Accounts Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }

        if (!accountResponse.getAccounts().isEmpty()) {
            if (accountFilters != null) {
                accountResponse = AccountHelper.applyFilters(accountResponse, accountFilters);
            }

            if (applyAllDetails) {
                applyAllDetails(accountResponse);
            }

            calculateCurrentBalance(username, accountResponse);
        }

        return accountResponse;
    }

    public static AccountResponse deleteAccount(String username, String id) {
        AccountResponse accountResponse;

        try {
            accountResponse = AccountConnector.deleteAccount(id);

            if (accountResponse.getDeleteCount().intValue() > 0) {
                TransactionResponse transactionResponse = TransactionService.deleteTransactionsByAccount(id);

                if (transactionResponse.getStatus() != null) {
                    accountResponse = AccountResponse.builder()
                            .accounts(Collections.emptyList())
                            .status(Status.builder()
                                    .errMsg("Account Deleted! But Error Deleting Related Transactions!! Please Try Again!!!")
                                    .build())
                            .build();
                }
            }
        } catch (Exception ex) {
            log.error("Exception in Delete Account: {} | {}", username, id);
            accountResponse = AccountResponse.builder()
                    .accounts(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Delete Account Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }

        return accountResponse;
    }

    private static void applyAllDetails(AccountResponse accountResponse) {
        RefAccountTypeResponse refAccountTypeResponse = RefTypesService.getAllAccountTypes();
        RefBankResponse refBankResponse = RefTypesService.getAllBanks();
        AccountHelper.applyAllDetailsStatic(accountResponse, refAccountTypeResponse.getRefAccountTypes(),
                refBankResponse.getRefBanks());
    }

    private static void calculateCurrentBalance(String username, AccountResponse accountResponse) {
        TransactionResponse transactionResponse = TransactionService.getTransactionsByUser(username, null, false);
        AccountHelper.calculateCurrentBalanceStatic(accountResponse, transactionResponse.getTransactions());
    }

    public static boolean isValidAccountRequest(AccountRequest accountRequest) {
        return accountRequest != null &&
                Util.hasText(accountRequest.getUsername()) &&
                Util.hasText(accountRequest.getDescription()) &&
                Util.hasText(accountRequest.getTypeId()) &&
                Util.hasText(accountRequest.getBankId()) &&
                Util.hasText(accountRequest.getStatus());
    }

    public static AccountResponse saveNewAccount(String username,
                                                 AccountRequest accountRequest,
                                                 boolean applyAllDetails) {
        AccountResponse accountResponse;

        try {
            accountResponse = AccountConnector.saveNewAccount(accountRequest);
        } catch (Exception ex) {
            log.error("Exception in Save New Account: {} | {} | {}", username, accountRequest, applyAllDetails);
            accountResponse = AccountResponse.builder()
                    .accounts(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Save Account Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }

        if (!accountResponse.getAccounts().isEmpty()) {
            if (applyAllDetails) {
                applyAllDetails(accountResponse);
            }

            calculateCurrentBalance(username, accountResponse);
        }

        return accountResponse;
    }

    public static AccountResponse updateAccount(String username, String id,
                                                AccountRequest accountRequest, boolean applyAllDetails) {
        AccountResponse accountResponse;

        try {
            accountResponse = AccountConnector.updateAccount(id, accountRequest);
        } catch (Exception ex) {
            log.error("Exception in Update Account: {} | {} | {} | {}", username, id, accountRequest, applyAllDetails);
            accountResponse = AccountResponse.builder()
                    .accounts(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Update Account Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }

        if (!accountResponse.getAccounts().isEmpty()) {
            if (applyAllDetails) {
                applyAllDetails(accountResponse);
            }

            calculateCurrentBalance(username, accountResponse);
        }

        return accountResponse;
    }
}
