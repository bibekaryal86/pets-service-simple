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

    private RefAccountTypeResponse refAccountTypeResponse = null;
    private RefBankResponse refBankResponse = null;
    private RefCategoryResponse refCategoryResponse = null;
    private RefCategoryTypeResponse refCategoryTypeResponse = null;
    private RefTransactionTypeResponse refTransactionTypeResponse = null;

    public void resetRefTypesCache() {
        refAccountTypeResponse = null;
        refBankResponse = null;
        refCategoryResponse = null;
        refCategoryTypeResponse = null;
        refTransactionTypeResponse = null;
    }

    public RefAccountTypeResponse getAllAccountTypes() {
        return Objects.requireNonNullElseGet(refAccountTypeResponse, this::getAllAccountTypesHttp);
    }

    public RefBankResponse getAllBanks() {
        return Objects.requireNonNullElseGet(refBankResponse, this::getAllBanksHttp);
    }

    public RefCategoryResponse getAllCategories() {
        return Objects.requireNonNullElseGet(refCategoryResponse, this::getAllCategoriesHttp);
    }

    public RefCategoryTypeResponse getAllCategoryTypes() {
        return Objects.requireNonNullElseGet(refCategoryTypeResponse, this::getAllCategoryTypesHttp);
    }

    public RefTransactionTypeResponse getAllTransactionTypes() {
        return Objects.requireNonNullElseGet(refTransactionTypeResponse, this::getAllTransactionTypesHttp);
    }

    private RefAccountTypeResponse getAllAccountTypesHttp() {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String endpoint = endpointMap().get("getAllAccountTypesUrl");
        refAccountTypeResponse = (RefAccountTypeResponse) sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefAccountTypeResponse.class);
        return refAccountTypeResponse;
    }

    private RefBankResponse getAllBanksHttp() {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String endpoint = endpointMap().get("getAllBanksUrl");
        refBankResponse = (RefBankResponse) sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefBankResponse.class);
        return refBankResponse;
    }

    private RefCategoryResponse getAllCategoriesHttp() {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String endpoint = endpointMap().get("getAllCategoriesUrl");
        refCategoryResponse = (RefCategoryResponse) sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefCategoryResponse.class);
        return refCategoryResponse;
    }

    private RefCategoryTypeResponse getAllCategoryTypesHttp() {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String endpoint = endpointMap().get("getAllCategoryTypesUrl");
        refCategoryTypeResponse = (RefCategoryTypeResponse) sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefCategoryTypeResponse.class);
        return refCategoryTypeResponse;
    }

    private RefTransactionTypeResponse getAllTransactionTypesHttp() {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String endpoint = endpointMap().get("getAllTransactionTypesUrl");
        refTransactionTypeResponse = (RefTransactionTypeResponse) sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefTransactionTypeResponse.class);
        return refTransactionTypeResponse;
    }
}
