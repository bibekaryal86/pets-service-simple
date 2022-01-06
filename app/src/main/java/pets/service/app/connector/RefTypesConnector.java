package pets.service.app.connector;

import org.eclipse.jetty.http.HttpMethod;
import pets.service.app.model.RefAccountTypeResponse;
import pets.service.app.model.RefBankResponse;
import pets.service.app.model.RefCategoryResponse;
import pets.service.app.model.RefCategoryTypeResponse;
import pets.service.app.model.RefTransactionTypeResponse;

import java.util.Map;
import java.util.Objects;

import static pets.service.app.util.ConnectorUtil.sendHttpRequest;
import static pets.service.app.util.EndpointUtil.endpointMap;
import static pets.service.app.util.Util.getPetsDatabaseAuthHeaders;

public class RefTypesConnector {

    private static RefAccountTypeResponse refAccountTypeResponse = null;
    private static RefBankResponse refBankResponse = null;
    private static RefCategoryResponse refCategoryResponse = null;
    private static RefCategoryTypeResponse refCategoryTypeResponse = null;
    private static RefTransactionTypeResponse refTransactionTypeResponse = null;

    public void resetRefTypesCache() {
        resetRefTypesCacheStatic();
    }

    public RefAccountTypeResponse getAllAccountTypes() {
        return Objects.requireNonNullElseGet(refAccountTypeResponse, RefTypesConnector::getAllAccountTypesHttp);
    }

    public RefBankResponse getAllBanks() {
        return Objects.requireNonNullElseGet(refBankResponse, RefTypesConnector::getAllBanksHttp);
    }

    public RefCategoryResponse getAllCategories() {
        return Objects.requireNonNullElseGet(refCategoryResponse, RefTypesConnector::getAllCategoriesHttp);
    }

    public RefCategoryTypeResponse getAllCategoryTypes() {
        return Objects.requireNonNullElseGet(refCategoryTypeResponse, RefTypesConnector::getAllCategoryTypesHttp);
    }

    public RefTransactionTypeResponse getAllTransactionTypes() {
        return Objects.requireNonNullElseGet(refTransactionTypeResponse, RefTypesConnector::getAllTransactionTypesHttp);
    }

    private static void resetRefTypesCacheStatic() {
        refAccountTypeResponse = null;
        refBankResponse = null;
        refCategoryResponse = null;
        refCategoryTypeResponse = null;
        refTransactionTypeResponse = null;
    }

    private static RefAccountTypeResponse getAllAccountTypesHttp() {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String endpoint = endpointMap().get("getAllAccountTypesUrl");
        refAccountTypeResponse = (RefAccountTypeResponse) sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefAccountTypeResponse.class);
        return refAccountTypeResponse;
    }

    private static RefBankResponse getAllBanksHttp() {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String endpoint = endpointMap().get("getAllBanksUrl");
        refBankResponse = (RefBankResponse) sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefBankResponse.class);
        return refBankResponse;
    }

    private static RefCategoryResponse getAllCategoriesHttp() {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String endpoint = endpointMap().get("getAllCategoriesUrl");
        refCategoryResponse = (RefCategoryResponse) sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefCategoryResponse.class);
        return refCategoryResponse;
    }

    private static RefCategoryTypeResponse getAllCategoryTypesHttp() {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String endpoint = endpointMap().get("getAllCategoryTypesUrl");
        refCategoryTypeResponse = (RefCategoryTypeResponse) sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefCategoryTypeResponse.class);
        return refCategoryTypeResponse;
    }

    private static RefTransactionTypeResponse getAllTransactionTypesHttp() {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String endpoint = endpointMap().get("getAllTransactionTypesUrl");
        refTransactionTypeResponse = (RefTransactionTypeResponse) sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefTransactionTypeResponse.class);
        return refTransactionTypeResponse;
    }
}
