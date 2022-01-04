package pets.service.app.connector;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.eclipse.jetty.http.HttpMethod;
import pets.service.app.model.*;
import pets.service.app.util.ConnectorUtil;
import pets.service.app.util.EndpointUtil;
import pets.service.app.util.Util;

import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefTypesConnector {

    private static RefAccountTypeResponse refAccountTypeResponse = null;
    private static RefBankResponse refBankResponse = null;
    private static RefCategoryResponse refCategoryResponse = null;
    private static RefCategoryTypeResponse refCategoryTypeResponse = null;
    private static RefTransactionTypeResponse refTransactionTypeResponse = null;

    public static void resetRefTypesCache() {
        refAccountTypeResponse = null;
        refBankResponse = null;
        refCategoryResponse = null;
        refCategoryTypeResponse = null;
        refTransactionTypeResponse = null;
    }

    public static RefAccountTypeResponse getAllAccountTypes() {
        return Objects.requireNonNullElseGet(refAccountTypeResponse, RefTypesConnector::getAllAccountTypesHttp);
    }

    public static RefBankResponse getAllBanks() {
        return Objects.requireNonNullElseGet(refBankResponse, RefTypesConnector::getAllBanksHttp);
    }

    public static RefCategoryResponse getAllCategories() {
        return Objects.requireNonNullElseGet(refCategoryResponse, RefTypesConnector::getAllCategoriesHttp);
    }

    public static RefCategoryTypeResponse getAllCategoryTypes() {
        return Objects.requireNonNullElseGet(refCategoryTypeResponse, RefTypesConnector::getAllCategoryTypesHttp);
    }

    public static RefTransactionTypeResponse getAllTransactionTypes() {
        return Objects.requireNonNullElseGet(refTransactionTypeResponse, RefTypesConnector::getAllTransactionTypesHttp);
    }

    private static RefAccountTypeResponse getAllAccountTypesHttp() {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String endpoint = EndpointUtil.endpointMap().get("getAllAccountTypesUrl");
        refAccountTypeResponse = (RefAccountTypeResponse) ConnectorUtil.sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefAccountTypeResponse.class);
        return refAccountTypeResponse;
    }

    private static RefBankResponse getAllBanksHttp() {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String endpoint = EndpointUtil.endpointMap().get("getAllBanksUrl");
        refBankResponse = (RefBankResponse) ConnectorUtil.sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefBankResponse.class);
        return refBankResponse;
    }

    private static RefCategoryResponse getAllCategoriesHttp() {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String endpoint = EndpointUtil.endpointMap().get("getAllCategoriesUrl");
        refCategoryResponse = (RefCategoryResponse) ConnectorUtil.sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefCategoryResponse.class);
        return refCategoryResponse;
    }

    private static RefCategoryTypeResponse getAllCategoryTypesHttp() {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String endpoint = EndpointUtil.endpointMap().get("getAllCategoryTypesUrl");
        refCategoryTypeResponse = (RefCategoryTypeResponse) ConnectorUtil.sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefCategoryTypeResponse.class);
        return refCategoryTypeResponse;
    }

    private static RefTransactionTypeResponse getAllTransactionTypesHttp() {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String endpoint = EndpointUtil.endpointMap().get("getAllTransactionTypesUrl");
        refTransactionTypeResponse = (RefTransactionTypeResponse) ConnectorUtil.sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefTransactionTypeResponse.class);
        return refTransactionTypeResponse;
    }
}
