package pets.service.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pets.service.app.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionHelper {

    public static TransactionResponse applyFilters(TransactionResponse transactionResponse,
                                                   TransactionFilters transactionFilters) {
        List<Transaction> transactions = transactionResponse.getTransactions();

        if (Util.hasText(transactionFilters.getAccountId())) {
            transactions = applyAccountFilter(transactions, transactionFilters.getAccountId());
        }

        if (Util.hasText(transactionFilters.getAccountTypeId())) {
            transactions = applyAccountTypeFilter(transactions, transactionFilters.getAccountTypeId());
        }

        if (transactionFilters.getAmountFrom() != null) {
            transactions = applyAmountFilterFrom(transactions, transactionFilters.getAmountFrom());
        }

        if (transactionFilters.getAmountTo() != null) {
            transactions = applyAmountFilterTo(transactions, transactionFilters.getAmountTo());
        }

        if (Util.hasText(transactionFilters.getBankId())) {
            transactions = applyBankFilter(transactions, transactionFilters.getBankId());
        }

        if (Util.hasText(transactionFilters.getCategoryId())) {
            transactions = applyCategoryFilter(transactions, transactionFilters.getCategoryId());
        }

        if (Util.hasText(transactionFilters.getCategoryTypeId())) {
            transactions = applyCategoryTypeFilter(transactions, transactionFilters.getCategoryTypeId());
        }

        if (Util.hasText(transactionFilters.getDateFrom())) {
            transactions = applyDateFilterFrom(transactions, transactionFilters.getDateFrom());
        }

        if (Util.hasText(transactionFilters.getDateTo())) {
            transactions = applyDateFilterTo(transactions, transactionFilters.getDateTo());
        }

        if (Util.hasText(transactionFilters.getMerchantId())) {
            transactions = applyMerchantFilter(transactions, transactionFilters.getMerchantId());
        }

        if (transactionFilters.getNecessary() != null) {
            transactions = applyNecessaryFilter(transactions, transactionFilters.getNecessary());
        }

        if (transactionFilters.getRegular() != null) {
            transactions = applyRegularFilter(transactions, transactionFilters.getRegular());
        }

        if (Util.hasText(transactionFilters.getTransactionTypeId())) {
            transactions = applyTypeFilter(transactions, transactionFilters.getTransactionTypeId());
        }

        return TransactionResponse.builder()
                .transactions(transactions)
                .build();
    }

    public static void applyAllDetailsStatic(TransactionResponse transactionResponse,
                                             @NonNull CompletableFuture<AccountResponse> accountResponseCompletableFuture,
                                             @NonNull CompletableFuture<RefCategoryResponse> refCategoryResponseCompletableFuture,
                                             @NonNull CompletableFuture<RefMerchantResponse> refMerchantResponseCompletableFuture,
                                             @NonNull CompletableFuture<RefTransactionTypeResponse> refTransactionTypeResponseCompletableFuture) {
        applyTransactionTypeDetails(transactionResponse, refTransactionTypeResponseCompletableFuture);
        applyMerchantDetails(transactionResponse, refMerchantResponseCompletableFuture);
        applyCategoryDetails(transactionResponse, refCategoryResponseCompletableFuture);
        applyAccountDetails(transactionResponse, accountResponseCompletableFuture);
    }

    private static List<Transaction> applyAccountFilter(List<Transaction> transactions, String accountId) {
        List<Transaction> fromAccountId = transactions.stream()
                .filter(transaction -> transaction.getAccount().getId().equals(accountId))
                .collect(toList());
        List<Transaction> fromTrfAccountId = transactions.stream()
                .filter(transaction -> transaction.getTrfAccount() != null)
                .filter(transaction -> transaction.getTrfAccount().getId().equals(accountId))
                .collect(toList());
        if (!fromTrfAccountId.isEmpty()) {
            fromAccountId.addAll(fromTrfAccountId);
            fromAccountId.sort(comparing(Transaction::getDate).reversed());
        }
        return fromAccountId;
    }

    private static List<Transaction> applyAccountTypeFilter(List<Transaction> transactions, String accountTypeId) {
        return transactions.stream()
                .filter(transaction -> transaction.getAccount().getRefAccountType().getId().equals(accountTypeId))
                .collect(toList());
    }

    private static List<Transaction> applyAmountFilterFrom(List<Transaction> transactions, BigDecimal amountFrom) {
        return transactions.stream()
                .filter(transaction -> transaction.getAmount().compareTo(amountFrom) >= 0)
                .collect(toList());
    }

    private static List<Transaction> applyAmountFilterTo(List<Transaction> transactions, BigDecimal amountTo) {
        return transactions.stream()
                .filter(transaction -> transaction.getAmount().compareTo(amountTo) <= 0)
                .collect(toList());
    }

    private static List<Transaction> applyBankFilter(List<Transaction> transactions, String bankId) {
        return transactions.stream()
                .filter(transaction -> transaction.getAccount().getRefBank().getId().equals(bankId))
                .collect(toList());
    }

    private static List<Transaction> applyCategoryFilter(List<Transaction> transactions, String categoryId) {
        return transactions.stream()
                .filter(transaction -> transaction.getRefCategory().getId().equals(categoryId))
                .collect(toList());
    }

    private static List<Transaction> applyCategoryTypeFilter(List<Transaction> transactions, String categoryTypeId) {
        return transactions.stream()
                .filter(transaction -> transaction.getRefCategory().getRefCategoryType().getId().equals(categoryTypeId))
                .collect(toList());
    }

    private static List<Transaction> applyDateFilterFrom(List<Transaction> transactions, String dateFrom) {
        return transactions.stream()
                .filter(transaction -> LocalDate.parse(transaction.getDate()).compareTo(LocalDate.parse(dateFrom)) >= 0)
                .collect(toList());
    }

    private static List<Transaction> applyDateFilterTo(List<Transaction> transactions, String dateTo) {
        return transactions.stream()
                .filter(transaction -> LocalDate.parse(transaction.getDate()).compareTo(LocalDate.parse(dateTo)) <= 0)
                .collect(toList());
    }

    private static List<Transaction> applyMerchantFilter(List<Transaction> transactions, String merchantId) {
        return transactions.stream()
                .filter(transaction -> transaction.getRefMerchant().getId().equals(merchantId))
                .collect(toList());
    }

    private static List<Transaction> applyNecessaryFilter(List<Transaction> transactions, boolean necessary) {
        return transactions.stream()
                .filter(transaction ->
                        transaction.getNecessary() != null && transaction.getNecessary() == necessary)
                .collect(toList());
    }

    private static List<Transaction> applyRegularFilter(List<Transaction> transactions, boolean regular) {
        return transactions.stream()
                .filter(transaction ->
                        transaction.getRegular() != null && transaction.getRegular() == regular)
                .collect(toList());
    }

    private static List<Transaction> applyTypeFilter(List<Transaction> transactions, String transactionTypeId) {
        return transactions.stream()
                .filter(transaction -> transaction.getRefTransactionType().getId().equals(transactionTypeId))
                .collect(toList());
    }

    private static void applyAccountDetails(TransactionResponse transactionResponse,
                                            CompletableFuture<AccountResponse> accountResponseCompletableFuture) {
        AccountResponse accountResponse = accountResponseCompletableFuture.join();
        applyAccountDetails(transactionResponse, accountResponse);
        applyTrfAccountDetails(transactionResponse, accountResponse);
    }

    private static void applyAccountDetails(TransactionResponse transactionResponse, @NonNull AccountResponse accountResponse) {
        accountResponse.getAccounts().forEach(account -> transactionResponse.getTransactions().stream()
                .filter(transaction -> transaction.getAccount() != null &&
                        Util.hasText(transaction.getAccount().getId()) &&
                        Util.hasText(account.getId()))
                .filter(transaction -> transaction.getAccount().getId().equals(account.getId()))
                .forEach(transaction -> {
                    transaction.getAccount().setDescription(account.getDescription());
                    transaction.getAccount().setStatus(account.getStatus());
                    transaction.getAccount().setRefBank(account.getRefBank());
                }));
    }

    private static void applyTrfAccountDetails(TransactionResponse transactionResponse, @NonNull AccountResponse accountResponse) {
        accountResponse.getAccounts().forEach(account -> transactionResponse.getTransactions().stream()
                .filter(transaction -> transaction.getTrfAccount() != null &&
                        Util.hasText(transaction.getTrfAccount().getId()) &&
                        Util.hasText(account.getId()))
                .filter(transaction -> transaction.getTrfAccount().getId().equals(account.getId()))
                .forEach(transaction -> {
                    transaction.getTrfAccount().setDescription(account.getDescription());
                    transaction.getTrfAccount().setRefBank(account.getRefBank());
                }));
    }

    private static void applyCategoryDetails(TransactionResponse transactionResponse,
                                             CompletableFuture<RefCategoryResponse> refCategoryResponseCompletableFuture) {
        applyCategoryDetails(transactionResponse, refCategoryResponseCompletableFuture.join());
    }

    private static void applyCategoryDetails(TransactionResponse transactionResponse, @NonNull RefCategoryResponse refCategoryResponse) {
        refCategoryResponse.getRefCategories().forEach(category -> transactionResponse.getTransactions().stream()
                .filter(transaction -> transaction.getRefCategory() != null &&
                        Util.hasText(transaction.getRefCategory().getId()) &&
                        Util.hasText(category.getId()))
                .filter(transaction -> transaction.getRefCategory().getId().equals(category.getId()))
                .forEach(transaction -> {
                    transaction.getRefCategory().setDescription(category.getDescription());
                    transaction.getRefCategory().setRefCategoryType(category.getRefCategoryType());
                }));
    }

    private static void applyMerchantDetails(TransactionResponse transactionResponse,
                                             CompletableFuture<RefMerchantResponse> refMerchantResponseCompletableFuture) {
        applyMerchantDetails(transactionResponse, refMerchantResponseCompletableFuture.join());
    }

    private static void applyMerchantDetails(TransactionResponse transactionResponse, @NonNull RefMerchantResponse refMerchantResponse) {
        refMerchantResponse.getRefMerchants().forEach(merchant -> transactionResponse.getTransactions().stream()
                .filter(transaction -> transaction.getRefMerchant() != null &&
                        Util.hasText(transaction.getRefMerchant().getId()) &&
                        Util.hasText(merchant.getId()))
                .filter(transaction -> transaction.getRefMerchant().getId().equals(merchant.getId()))
                .forEach(transaction -> transaction.getRefMerchant().setDescription(merchant.getDescription())));
    }

    private static void applyTransactionTypeDetails(TransactionResponse transactionResponse,
                                                    CompletableFuture<RefTransactionTypeResponse> refTransactionTypeResponseCompletableFuture) {
        applyTransactionTypeDetails(transactionResponse, refTransactionTypeResponseCompletableFuture.join());
    }

    private static void applyTransactionTypeDetails(TransactionResponse transactionResponse, @NonNull RefTransactionTypeResponse refTransactionTypeResponse) {
        refTransactionTypeResponse.getRefTransactionTypes().forEach(transactionType -> transactionResponse.getTransactions().stream()
                .filter(transaction -> transaction.getRefTransactionType() != null &&
                        Util.hasText(transaction.getRefTransactionType().getId()) &&
                        Util.hasText(transactionType.getId()))
                .filter(transaction -> transaction.getRefTransactionType().getId().equals(transactionType.getId()))
                .forEach(transaction -> transaction.getRefTransactionType().setDescription(transactionType.getDescription())));
    }
}
