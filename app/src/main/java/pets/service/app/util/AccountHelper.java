package pets.service.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pets.service.app.model.Account;
import pets.service.app.model.AccountFilters;
import pets.service.app.model.AccountResponse;
import pets.service.app.model.RefAccountType;
import pets.service.app.model.RefBank;
import pets.service.app.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountHelper {

    public static AccountResponse applyFilters(AccountResponse accountResponse, AccountFilters accountFilters) {
        List<Account> accounts = accountResponse.getAccounts();

        if (Util.hasText(accountFilters.getAccountTypeId())) {
            accounts = applyAccountTypeFilter(accounts, accountFilters.getAccountTypeId());
        }

        if (Util.hasText(accountFilters.getBankId())) {
            accounts = applyBankFilter(accounts, accountFilters.getBankId());
        }

        if (Util.hasText(accountFilters.getStatus())) {
            accounts = applyStatusFilter(accounts, accountFilters.getStatus());
        }

        return AccountResponse.builder()
                .accounts(accounts)
                .build();
    }

    public static void applyAllDetailsStatic(AccountResponse accountResponse, @NonNull List<RefAccountType> accountTypes,
                                             @NonNull List<RefBank> refBanks) {
        applyRefAccountTypeDetails(accountResponse, accountTypes);
        applyRefBankDetails(accountResponse, refBanks);
    }

    public static void calculateCurrentBalanceStatic(AccountResponse accountResponse, @NonNull List<Transaction> transactions) {
        for (Account account : accountResponse.getAccounts()) {
            account.setCurrentBalance(calculateCurrentBalance(account.getId(), account.getRefAccountType().getId(),
                    account.getOpeningBalance(), transactions));
        }
    }

    private static List<Account> applyAccountTypeFilter(List<Account> accountList, String accountTypeId) {
        return accountList.stream()
                .filter(account -> account.getRefAccountType().getId().equals(accountTypeId))
                .collect(toList());
    }

    private static List<Account> applyBankFilter(List<Account> accountList, String bankId) {
        return accountList.stream()
                .filter(account -> account.getRefBank().getId().equals(bankId))
                .collect(toList());
    }

    private static List<Account> applyStatusFilter(List<Account> accountList, String status) {
        return accountList.stream()
                .filter(account -> account.getStatus().equals(status))
                .collect(toList());
    }

    private static void applyRefAccountTypeDetails(AccountResponse accountResponse, List<RefAccountType> accountTypes) {
        accountTypes.forEach(refAccountType -> accountResponse.getAccounts().stream()
                .filter(account -> account.getRefAccountType() != null &&
                        Util.hasText(account.getRefAccountType().getId()) &&
                        Util.hasText(refAccountType.getId()))
                .filter(account -> account.getRefAccountType().getId().equals(refAccountType.getId()))
                .forEach(account -> account.getRefAccountType().setDescription(refAccountType.getDescription())));
    }

    private static void applyRefBankDetails(AccountResponse accountResponse, List<RefBank> refBanks) {
        refBanks.forEach(refBank -> accountResponse.getAccounts().stream()
                .filter(account -> account.getRefBank() != null &&
                        Util.hasText(account.getRefBank().getId()) &&
                        Util.hasText(refBank.getId()))
                .filter(account -> account.getRefBank().getId().equals(refBank.getId()))
                .forEach(account -> account.getRefBank().setDescription(refBank.getDescription())));
    }

    private static BigDecimal calculateCurrentBalance(String accountId, String accountTypeId, BigDecimal openingBalance,
                                                      List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.getAccount().getId().equals(accountId)) {
                openingBalance = calculateCurrentBalance(transaction.getRefTransactionType().getId(), accountTypeId,
                        openingBalance, transaction.getAmount());
            } else if (transaction.getTrfAccount() != null &&
                    Util.hasText(transaction.getTrfAccount().getId()) &&
                    transaction.getTrfAccount().getId().equals(accountId)) {
                if (Util.ACCOUNT_TYPES_DEPOSIT_ACCOUNTS.contains(accountTypeId)) {
                    openingBalance = openingBalance.add(transaction.getAmount());
                } else if (Util.ACCOUNT_TYPES_LOAN_ACCOUNTS.contains(accountTypeId)) {
                    openingBalance = openingBalance.subtract(transaction.getAmount());
                }
            }
        }

        return openingBalance;
    }

    private static BigDecimal calculateCurrentBalance(String transactionTypeId, String accountTypeId,
                                                      BigDecimal currentBalance, BigDecimal transactionAmount) {
        switch (transactionTypeId) {
            case Util.TRANSACTION_TYPE_ID_EXPENSE:
            case Util.TRANSACTION_TYPE_ID_TRANSFER:
                if (Util.ACCOUNT_TYPES_DEPOSIT_ACCOUNTS.contains(accountTypeId)) {
                    return currentBalance.subtract(transactionAmount);
                } else if (Util.ACCOUNT_TYPES_LOAN_ACCOUNTS.contains(accountTypeId)) {
                    return currentBalance.add(transactionAmount);
                }
                break;
            case Util.TRANSACTION_TYPE_ID_INCOME:
                if (Util.ACCOUNT_TYPES_DEPOSIT_ACCOUNTS.contains(accountTypeId)) {
                    return currentBalance.add(transactionAmount);
                } else if (Util.ACCOUNT_TYPES_LOAN_ACCOUNTS.contains(accountTypeId)) {
                    return currentBalance.subtract(transactionAmount);
                }
                break;
            default:
                break;
        }
        return currentBalance;
    }
}
