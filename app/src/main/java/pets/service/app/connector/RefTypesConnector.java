package pets.service.app.connector;

import org.eclipse.jetty.http.HttpMethod;
import pets.service.app.model.RefAccountTypeResponse;
import pets.service.app.model.RefBankResponse;
import pets.service.app.model.RefCategoryResponse;
import pets.service.app.model.RefCategoryTypeResponse;
import pets.service.app.model.RefTransactionTypeResponse;
import pets.service.app.util.ConnectorUtil;
import pets.service.app.util.EndpointUtil;
import pets.service.app.util.Util;

import java.util.Map;
import java.util.Objects;

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
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String endpoint = EndpointUtil.endpointMap().get("getAllAccountTypesUrl");
        refAccountTypeResponse = (RefAccountTypeResponse) ConnectorUtil.sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefAccountTypeResponse.class);
        return refAccountTypeResponse;
    }

    private RefBankResponse getAllBanksHttp() {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String endpoint = EndpointUtil.endpointMap().get("getAllBanksUrl");
        refBankResponse = (RefBankResponse) ConnectorUtil.sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefBankResponse.class);
        return refBankResponse;
    }

    private RefCategoryResponse getAllCategoriesHttp() {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String endpoint = EndpointUtil.endpointMap().get("getAllCategoriesUrl");
        refCategoryResponse = (RefCategoryResponse) ConnectorUtil.sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefCategoryResponse.class);
        return refCategoryResponse;
    }

    private RefCategoryTypeResponse getAllCategoryTypesHttp() {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String endpoint = EndpointUtil.endpointMap().get("getAllCategoryTypesUrl");
        refCategoryTypeResponse = (RefCategoryTypeResponse) ConnectorUtil.sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefCategoryTypeResponse.class);
        return refCategoryTypeResponse;
    }

    private RefTransactionTypeResponse getAllTransactionTypesHttp() {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String endpoint = EndpointUtil.endpointMap().get("getAllTransactionTypesUrl");
        refTransactionTypeResponse = (RefTransactionTypeResponse) ConnectorUtil.sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, RefTransactionTypeResponse.class);
        return refTransactionTypeResponse;
    }
}
