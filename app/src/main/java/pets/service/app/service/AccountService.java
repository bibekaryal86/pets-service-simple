package pets.service.app.service;

import lombok.extern.slf4j.Slf4j;
import pets.service.app.connector.AccountConnector;
import pets.service.app.model.AccountFilters;
import pets.service.app.model.AccountRequest;
import pets.service.app.model.AccountResponse;
import pets.service.app.model.RefAccountTypeResponse;
import pets.service.app.model.RefBankResponse;
import pets.service.app.model.Status;
import pets.service.app.model.TransactionResponse;

import java.util.concurrent.CompletableFuture;

import static java.util.Collections.emptyList;
import static pets.service.app.util.AccountHelper.applyAllDetailsStatic;
import static pets.service.app.util.AccountHelper.applyFilters;
import static pets.service.app.util.AccountHelper.calculateCurrentBalanceStatic;

@Slf4j
public class AccountService {

    public AccountResponse getAccountById(String username, String id, boolean applyAllDetails) {
        AccountResponse accountResponse;

        try {
            accountResponse = new AccountConnector().getAccountById(id);
        } catch (Exception ex) {
            log.error("Exception in Get Account by Id: {}", id);
            accountResponse = AccountResponse.builder()
                    .accounts(emptyList())
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

    public CompletableFuture<AccountResponse> getAccountsByUserFuture(String username) {
        return CompletableFuture.supplyAsync(() -> getAccountsByUser(username, null, true));
    }

    public AccountResponse getAccountsByUser(String username, AccountFilters accountFilters, boolean applyAllDetails) {
        AccountResponse accountResponse;

        try {
            accountResponse = new AccountConnector().getAccountsByUser(username);
        } catch (Exception ex) {
            log.error("Exception in Get Accounts By User: {} | {} | {}", username, accountFilters, applyAllDetails);
            accountResponse = AccountResponse.builder()
                    .accounts(emptyList())
                    .status(Status.builder()
                            .errMsg("Accounts Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }

        if (!accountResponse.getAccounts().isEmpty()) {
            if (accountFilters != null) {
                accountResponse = applyFilters(accountResponse, accountFilters);
            }

            if (applyAllDetails) {
                applyAllDetails(accountResponse);
            }

            calculateCurrentBalance(username, accountResponse);
        }

        return accountResponse;
    }

    public AccountResponse saveNewAccount(String username,
                                          AccountRequest accountRequest,
                                          boolean applyAllDetails) {
        AccountResponse accountResponse;

        try {
            accountResponse = new AccountConnector().saveNewAccount(accountRequest);
        } catch (Exception ex) {
            log.error("Exception in Save New Account: {} | {} | {}", username, accountRequest, applyAllDetails);
            accountResponse = AccountResponse.builder()
                    .accounts(emptyList())
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

    public AccountResponse updateAccount(String username, String id,
                                         AccountRequest accountRequest, boolean applyAllDetails) {
        AccountResponse accountResponse;

        try {
            accountResponse = new AccountConnector().updateAccount(id, accountRequest);
        } catch (Exception ex) {
            log.error("Exception in Update Account: {} | {} | {} | {}", username, id, accountRequest, applyAllDetails);
            accountResponse = AccountResponse.builder()
                    .accounts(emptyList())
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

    public AccountResponse deleteAccount(String username, String id) {
        AccountResponse accountResponse;

        try {
            accountResponse = new AccountConnector().deleteAccount(id);

            if (accountResponse.getDeleteCount().intValue() > 0) {
                TransactionResponse transactionResponse = new TransactionService().deleteTransactionsByAccount(id);

                if (transactionResponse.getStatus() != null) {
                    accountResponse = AccountResponse.builder()
                            .accounts(emptyList())
                            .status(Status.builder()
                                    .errMsg("Account Deleted! But Error Deleting Related Transactions!! Please Try Again!!!")
                                    .build())
                            .build();
                }
            }
        } catch (Exception ex) {
            log.error("Exception in Delete Account: {} | {}", username, id);
            accountResponse = AccountResponse.builder()
                    .accounts(emptyList())
                    .status(Status.builder()
                            .errMsg("Delete Account Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }

        return accountResponse;
    }

    private void applyAllDetails(AccountResponse accountResponse) {
        RefAccountTypeResponse refAccountTypeResponse = new RefTypesService().getAllAccountTypes();
        RefBankResponse refBankResponse = new RefTypesService().getAllBanks();
        applyAllDetailsStatic(accountResponse, refAccountTypeResponse.getRefAccountTypes(),
                refBankResponse.getRefBanks());
    }

    private void calculateCurrentBalance(String username, AccountResponse accountResponse) {
        TransactionResponse transactionResponse = new TransactionService().getTransactionsByUser(username, null, false);
        calculateCurrentBalanceStatic(accountResponse, transactionResponse.getTransactions());
    }
}
