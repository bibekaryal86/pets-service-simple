package pets.service.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pets.service.app.exception.CustomRuntimeException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointUtil {

    private static Map<String, String> theEndpointMap = null;

    // base_urls
    private static final String PETS_DB_ENDPOINT_BASE_DEV = "http://localhost:8002/pets-database";
    private static final String PETS_DB_ENDPOINT_BASE_DOCKER = "http://pets-database:8002/pets-database";
    private static final String PETS_DB_ENDPOINT_BASE_PROD = "https://pets-database.appspot.com/pets-database";

    // endpoint_bases
    private static final String PETS_DB_ACCOUNT_BASE = "/accounts/account";
    private static final String PETS_DB_ACCOUNT_TYPE_BASE = "/refaccounttypes/refaccounttype";
    private static final String PETS_DB_BANK_BASE = "/refbanks/refbank";
    private static final String PETS_DB_CATEGORY_BASE = "/refcategories/refcategory";
    private static final String PETS_DB_CATEGORY_TYPE_BASE = "/refcategorytypes/refcategorytype";
    private static final String PETS_DB_MERCHANT_BASE = "/refmerchants/refmerchant";
    private static final String PETS_DB_TRANSACTION_BASE = "/transactions/transaction";
    private static final String PETS_DB_TRANSACTION_TYPE_BASE = "/reftransactiontypes/reftransactiontype";
    private static final String PETS_DB_USER_BASE = "/users/user";

    // endpoint_urls
    private static final String PETS_DB_URL_WITH_USERNAME = "/username/%s";
    private static final String PETS_DB_URL_WITH_USER = "/user/%s";
    private static final String PETS_DB_URL_WITH_ID = "/id/%s";
    private static final String PETS_DB_URL_WITH_ACCOUNT = "/accountid/%s";

    private static Map<String, String> setEndpointMap() {
        Map<String, String> endpointMap = new HashMap<>();
        String profile = Util.getSystemEnvProperty(Util.PROFILE);
        String endpointBasePetsDatabase;

        if (!Util.hasText(profile)) {
            throw new CustomRuntimeException("PROFILE NOT SET AT RUNTIME");
        }

        if ("development".equals(profile)) {
            endpointBasePetsDatabase = PETS_DB_ENDPOINT_BASE_DEV;
        } else if ("docker".equals(profile)) {
            endpointBasePetsDatabase = PETS_DB_ENDPOINT_BASE_DOCKER;
        } else {
            endpointBasePetsDatabase = PETS_DB_ENDPOINT_BASE_PROD;
        }

        // users
        endpointMap.put("getUserByUsernameUrl", endpointBasePetsDatabase.concat(PETS_DB_USER_BASE).concat(PETS_DB_URL_WITH_USERNAME));
        endpointMap.put("saveNewUserUrl", endpointBasePetsDatabase.concat(PETS_DB_USER_BASE));
        endpointMap.put("updateUserPutUrl", endpointBasePetsDatabase.concat(PETS_DB_USER_BASE).concat(PETS_DB_URL_WITH_ID));

        // accounts
        endpointMap.put("getAccountByIdUrl", endpointBasePetsDatabase.concat(PETS_DB_ACCOUNT_BASE).concat(PETS_DB_URL_WITH_ID));
        endpointMap.put("getAccountsByUserUrl", endpointBasePetsDatabase.concat(PETS_DB_ACCOUNT_BASE).concat(PETS_DB_URL_WITH_USER));
        endpointMap.put("saveNewAccountUrl", endpointBasePetsDatabase.concat(PETS_DB_ACCOUNT_BASE));
        endpointMap.put("updateAccountPutUrl", endpointBasePetsDatabase.concat(PETS_DB_ACCOUNT_BASE).concat(PETS_DB_URL_WITH_ID));
        endpointMap.put("deleteAccountUrl", endpointBasePetsDatabase.concat(PETS_DB_ACCOUNT_BASE).concat(PETS_DB_URL_WITH_ID));

        // ref types
        endpointMap.put("getAllAccountTypesUrl", endpointBasePetsDatabase.concat(PETS_DB_ACCOUNT_TYPE_BASE));
        endpointMap.put("getAllBanksUrl", endpointBasePetsDatabase.concat(PETS_DB_BANK_BASE));
        endpointMap.put("getAllCategoriesUrl", endpointBasePetsDatabase.concat(PETS_DB_CATEGORY_BASE));
        endpointMap.put("getAllCategoryTypesUrl", endpointBasePetsDatabase.concat(PETS_DB_CATEGORY_TYPE_BASE));
        endpointMap.put("getAllTransactionTypesUrl", endpointBasePetsDatabase.concat(PETS_DB_TRANSACTION_TYPE_BASE));

        // merchants
        endpointMap.put("getMerchantByIdUrl", endpointBasePetsDatabase.concat(PETS_DB_MERCHANT_BASE).concat(PETS_DB_URL_WITH_ID));
        endpointMap.put("getMerchantsByUserUrl", endpointBasePetsDatabase.concat(PETS_DB_MERCHANT_BASE).concat(PETS_DB_URL_WITH_USER));
        endpointMap.put("saveNewMerchantUrl", endpointBasePetsDatabase.concat(PETS_DB_MERCHANT_BASE));
        endpointMap.put("updateMerchantUrl", endpointBasePetsDatabase.concat(PETS_DB_MERCHANT_BASE).concat(PETS_DB_URL_WITH_ID));
        endpointMap.put("deleteMerchantUrl", endpointBasePetsDatabase.concat(PETS_DB_MERCHANT_BASE).concat(PETS_DB_URL_WITH_ID));

        // transactions
        endpointMap.put("getTransactionByIdUrl", endpointBasePetsDatabase.concat(PETS_DB_TRANSACTION_BASE).concat(PETS_DB_URL_WITH_ID));
        endpointMap.put("getTransactionsByUserUrl", endpointBasePetsDatabase.concat(PETS_DB_TRANSACTION_BASE).concat(PETS_DB_URL_WITH_USER));
        endpointMap.put("saveNewTransactionUrl", endpointBasePetsDatabase.concat(PETS_DB_TRANSACTION_BASE));
        endpointMap.put("updateTransactionPutUrl", endpointBasePetsDatabase.concat(PETS_DB_TRANSACTION_BASE).concat(PETS_DB_URL_WITH_ID));
        endpointMap.put("deleteTransactionUrl", endpointBasePetsDatabase.concat(PETS_DB_TRANSACTION_BASE).concat(PETS_DB_URL_WITH_ID));
        endpointMap.put("deleteTransactionsByAccountUrl", endpointBasePetsDatabase.concat(PETS_DB_TRANSACTION_BASE).concat(PETS_DB_URL_WITH_ACCOUNT));

        theEndpointMap = new HashMap<>();
        theEndpointMap.putAll(endpointMap);

        return endpointMap;
    }

    public static Map<String, String> endpointMap() {
        return Objects.requireNonNullElseGet(theEndpointMap, EndpointUtil::setEndpointMap);
    }
}
