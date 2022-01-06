package pets.service.app.service;

import lombok.NonNull;
import pets.service.app.model.Account;
import pets.service.app.model.AccountResponse;
import pets.service.app.model.RefCategory;
import pets.service.app.model.RefCategoryType;
import pets.service.app.model.ReportCashFlows;
import pets.service.app.model.ReportCategories;
import pets.service.app.model.ReportCategoryTypes;
import pets.service.app.model.ReportCurrentBalances;
import pets.service.app.model.ReportsResponse;
import pets.service.app.model.Status;
import pets.service.app.model.Transaction;
import pets.service.app.model.TransactionFilters;
import pets.service.app.model.TransactionResponse;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.time.LocalDate.parse;
import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static pets.service.app.util.Util.ACCOUNT_TYPE_ID_CASH;
import static pets.service.app.util.Util.ACCOUNT_TYPE_ID_CHECKING;
import static pets.service.app.util.Util.ACCOUNT_TYPE_ID_CREDIT_CARD;
import static pets.service.app.util.Util.ACCOUNT_TYPE_ID_INVESTMENT;
import static pets.service.app.util.Util.ACCOUNT_TYPE_ID_LOANS_MORTGAGES;
import static pets.service.app.util.Util.ACCOUNT_TYPE_ID_OTHER_DEPOSITS;
import static pets.service.app.util.Util.ACCOUNT_TYPE_ID_OTHER_LOANS;
import static pets.service.app.util.Util.ACCOUNT_TYPE_ID_SAVINGS;
import static pets.service.app.util.Util.CATEGORY_ID_LOAN_PAYMENTS;
import static pets.service.app.util.Util.CATEGORY_ID_REFUNDS;
import static pets.service.app.util.Util.TRANSACTION_TYPE_ID_EXPENSE;
import static pets.service.app.util.Util.TRANSACTION_TYPE_ID_INCOME;
import static pets.service.app.util.Util.TRANSACTION_TYPE_ID_TRANSFER;

public class ReportsService {

    private static final String DATE_FORMAT = "%s-%s-%s";

    public ReportsResponse getCurrentBalancesReport(String username) {
        ReportsResponse reportsResponse;

        AccountResponse accountResponse = new AccountService().getAccountsByUser(username, null, false);

        if (accountResponse.getStatus() == null) {
            reportsResponse = ReportsResponse.builder()
                    .reportCurrentBalances(calculateCurrentBalances(accountResponse.getAccounts()))
                    .build();
        } else {
            reportsResponse = reportResponseError("Current Balances", accountResponse.getStatus().getErrMsg());
        }

        return reportsResponse;
    }

    private List<ReportCurrentBalances> calculateCurrentBalances(@NonNull List<Account> accounts) {
        BigDecimal totalCash = accounts.stream()
                .filter(account -> account.getRefAccountType().getId().equals(ACCOUNT_TYPE_ID_CASH))
                .map(Account::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCheckingAccounts = accounts.stream()
                .filter(account -> account.getRefAccountType().getId().equals(ACCOUNT_TYPE_ID_CHECKING))
                .map(Account::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalSavingsAccounts = accounts.stream()
                .filter(account -> account.getRefAccountType().getId().equals(ACCOUNT_TYPE_ID_SAVINGS))
                .map(Account::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalInvestmentAccounts = accounts.stream()
                .filter(account -> account.getRefAccountType().getId().equals(ACCOUNT_TYPE_ID_INVESTMENT))
                .map(Account::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalOtherDepositAccounts = accounts.stream()
                .filter(account -> account.getRefAccountType().getId().equals(ACCOUNT_TYPE_ID_OTHER_DEPOSITS))
                .map(Account::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCreditCards = accounts.stream()
                .filter(account -> account.getRefAccountType().getId().equals(ACCOUNT_TYPE_ID_CREDIT_CARD))
                .map(Account::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalLoansAndMortgages = accounts.stream()
                .filter(account -> account.getRefAccountType().getId().equals(ACCOUNT_TYPE_ID_LOANS_MORTGAGES))
                .map(Account::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalOtherLoanAccounts = accounts.stream()
                .filter(account -> account.getRefAccountType().getId().equals(ACCOUNT_TYPE_ID_OTHER_LOANS))
                .map(Account::getCurrentBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalAssets = totalCash.add(totalCheckingAccounts)
                .add(totalSavingsAccounts)
                .add(totalInvestmentAccounts)
                .add(totalOtherDepositAccounts);
        BigDecimal totalDebts = totalCreditCards
                .add(totalLoansAndMortgages)
                .add(totalOtherLoanAccounts);
        BigDecimal netWorth = totalAssets.subtract(totalDebts);

        return asList(ReportCurrentBalances.builder()
                        .totalCash(totalCash)
                        .accountTypeCashId(ACCOUNT_TYPE_ID_CASH)
                        .totalCheckingAccounts(totalCheckingAccounts)
                        .accountTypeCheckingAccountsId(ACCOUNT_TYPE_ID_CHECKING)
                        .totalSavingsAccounts(totalSavingsAccounts)
                        .accountTypeSavingsAccountsId(ACCOUNT_TYPE_ID_SAVINGS)
                        .totalInvestmentAccounts(totalInvestmentAccounts)
                        .accountTypeInvestmentAccountsId(ACCOUNT_TYPE_ID_INVESTMENT)
                        .totalOtherDepositAccounts(totalOtherDepositAccounts)
                        .accountTypeOtherDepositAccountsId(ACCOUNT_TYPE_ID_OTHER_DEPOSITS)
                        .totalCreditCards(totalCreditCards)
                        .accountTypeCreditCardsId(ACCOUNT_TYPE_ID_CREDIT_CARD)
                        .totalLoansAndMortgages(totalLoansAndMortgages)
                        .accountTypeLoansAndMortgagesId(ACCOUNT_TYPE_ID_LOANS_MORTGAGES)
                        .totalOtherLoanAccounts(totalOtherLoanAccounts)
                        .accountTypeOtherLoanAccountsId(ACCOUNT_TYPE_ID_OTHER_LOANS)
                        .build(),
                ReportCurrentBalances.builder()
                        .totalAssets(totalAssets)
                        .totalDebts(totalDebts)
                        .netWorth(netWorth)
                        .build());
    }

    public ReportsResponse getCashFlowsReport(String username, String selectedYear) {
        ReportsResponse reportsResponse;

        TransactionFilters transactionFilters = TransactionFilters.builder()
                .dateFrom(String.format(DATE_FORMAT, selectedYear, "01", "01"))
                .dateTo(String.format(DATE_FORMAT, selectedYear, "12", "31"))
                .build();
        TransactionResponse transactionResponse = new TransactionService().getTransactionsByUser(username, transactionFilters, false);

        if (transactionResponse.getStatus() == null) {
            reportsResponse = ReportsResponse.builder()
                    .reportCashFlows(calculateCashFlows(transactionResponse.getTransactions(), selectedYear))
                    .build();
        } else {
            reportsResponse = reportResponseError("Cash Flows", transactionResponse.getStatus().getErrMsg());
        }

        return reportsResponse;
    }

    private List<ReportCashFlows> calculateCashFlows(@NonNull List<Transaction> transactions, String selectedYear) {
        List<ReportCashFlows> reportCashFlows = new ArrayList<>();

        Set<Month> months = transactions.stream()
                .map(transaction -> parse(transaction.getDate()).getMonth())
                .collect(toSet());

        months.forEach(month -> {
            List<Transaction> monthlyTransactions = transactions.stream()
                    .filter(transaction -> month.equals(parse(transaction.getDate()).getMonth()))
                    .collect(toList());
            reportCashFlows.add(calculateCashFlow(monthlyTransactions, month, Integer.parseInt(selectedYear)));
        });

        reportCashFlows.sort(comparingInt(ReportCashFlows::getMonthToSort));

        return reportCashFlows;
    }

    private ReportCashFlows calculateCashFlow(List<Transaction> monthlyTransactions, Month month, int selectedYear) {
        BigDecimal totalIncomes = monthlyTransactions.stream()
                .filter(transaction -> transaction.getRefTransactionType().getId().equals(TRANSACTION_TYPE_ID_INCOME)
                        && !transaction.getRefCategory().getId().equals(CATEGORY_ID_REFUNDS))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpenses = monthlyTransactions.stream()
                .filter(transaction -> transaction.getRefTransactionType().getId().equals(TRANSACTION_TYPE_ID_EXPENSE)
                        || (transaction.getRefTransactionType().getId().equals(TRANSACTION_TYPE_ID_TRANSFER)
                        && CATEGORY_ID_LOAN_PAYMENTS.contains(transaction.getRefCategory().getId())))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal netSavings = totalIncomes.subtract(totalExpenses);

        boolean isLeapYear = (((selectedYear % 4 == 0) && (selectedYear % 100 != 0)) || (selectedYear % 400 == 0));
        String monthValue = month.getValue() < 10 ? String.format("0%s", month.getValue()) : String.valueOf(month.getValue());
        String beginDate = String.format(DATE_FORMAT, selectedYear, monthValue, "01");
        String endDate = String.format(DATE_FORMAT, selectedYear, monthValue, month.length(isLeapYear));

        return ReportCashFlows.builder()
                .month(month.name())
                .monthToSort(month.getValue())
                .monthBeginDate(beginDate)
                .monthEndDate(endDate)
                .totalIncomes(totalIncomes)
                .totalExpenses(totalExpenses)
                .netSavings(netSavings)
                .build();
    }

    public ReportsResponse getCategoriesReport(String username, String selectedYear) {
        ReportsResponse reportsResponse;

        TransactionFilters transactionFilters = TransactionFilters.builder()
                .dateFrom(String.format(DATE_FORMAT, selectedYear, "01", "01"))
                .dateTo(String.format(DATE_FORMAT, selectedYear, "12", "31"))
                .build();
        TransactionResponse transactionResponse = new TransactionService().getTransactionsByUser(username, transactionFilters, true);

        if (transactionResponse.getStatus() == null) {
            reportsResponse = ReportsResponse.builder()
                    .reportCategoryTypes(calculateCategoryTypesTotals(transactionResponse.getTransactions()))
                    .build();
        } else {
            reportsResponse = reportResponseError("Categories", transactionResponse.getStatus().getErrMsg());
        }

        return reportsResponse;
    }

    private List<ReportCategoryTypes> calculateCategoryTypesTotals(@NonNull List<Transaction> transactions) {
        List<ReportCategoryTypes> reportCategoryTypes = new ArrayList<>();

        Set<RefCategoryType> refCategoryTypes = transactions.stream()
                .filter(transaction -> !transaction.getRefTransactionType().getId().equals(TRANSACTION_TYPE_ID_TRANSFER))
                .map(transaction -> transaction.getRefCategory().getRefCategoryType())
                .collect(toSet());

        refCategoryTypes.forEach(refCategoryType -> {
            List<Transaction> categoryTypesTransactions = transactions.stream()
                    .filter(transaction -> transaction.getRefCategory().getRefCategoryType().getId().equals(refCategoryType.getId()))
                    .collect(toList());
            reportCategoryTypes.add(calculateCategoryTypesTotals(categoryTypesTransactions, refCategoryType));
        });

        reportCategoryTypes.sort(comparing(reportCategoryType -> reportCategoryType.getRefCategoryType().getDescription()));

        return reportCategoryTypes;
    }

    private ReportCategoryTypes calculateCategoryTypesTotals(List<Transaction> categoryTypeTransactions, RefCategoryType refCategoryType) {
        BigDecimal totalCategoryType = categoryTypeTransactions.stream()
                .filter(transaction -> transaction.getRefCategory().getRefCategoryType().getId().equals(refCategoryType.getId()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ReportCategoryTypes.builder()
                .refCategoryType(refCategoryType)
                .totalRefCategoryType(totalCategoryType)
                .reportCategories(calculateCategoriesTotals(categoryTypeTransactions))
                .build();
    }

    private List<ReportCategories> calculateCategoriesTotals(List<Transaction> categoryTypeTransactions) {
        List<ReportCategories> reportCategories = new ArrayList<>();

        Set<RefCategory> refCategories = categoryTypeTransactions.stream()
                .map(Transaction::getRefCategory)
                .collect(toSet());

        refCategories.forEach(refCategory -> {
            List<Transaction> categoryTransactions = categoryTypeTransactions.stream()
                    .filter(transaction -> transaction.getRefCategory().getId().equals(refCategory.getId()))
                    .collect(toList());
            reportCategories.add(calculateCategoriesTotals(categoryTransactions, refCategory));
        });

        reportCategories.sort(comparing(reportCategory -> reportCategory.getRefCategory().getDescription()));

        return reportCategories;
    }

    private ReportCategories calculateCategoriesTotals(List<Transaction> categoryTransactions, RefCategory refCategory) {
        BigDecimal totalCategory = categoryTransactions.stream()
                .filter(transaction -> transaction.getRefCategory().getId().equals(refCategory.getId()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ReportCategories.builder()
                .refCategory(RefCategory.builder()
                        .id(refCategory.getId())
                        .description(refCategory.getDescription())
                        .build())
                .totalRefCategory(totalCategory)
                .build();
    }

    private ReportsResponse reportResponseError(String errMsg, String message) {
        return ReportsResponse.builder()
                .status(Status.builder()
                        .errMsg(String.format("Something Went Wrong! Error Retrieving %s Report!! Please Try Again!!!", errMsg))
                        .message(message)
                        .build())
                .build();
    }


}
