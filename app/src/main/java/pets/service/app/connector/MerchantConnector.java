package pets.service.app.connector;

import lombok.NonNull;
import org.eclipse.jetty.http.HttpMethod;
import pets.service.app.model.RefMerchantRequest;
import pets.service.app.model.RefMerchantResponse;

import java.util.Map;

import static pets.service.app.util.ConnectorUtil.sendHttpRequest;
import static pets.service.app.util.EndpointUtil.endpointMap;
import static pets.service.app.util.Util.getPetsDatabaseAuthHeaders;

public class MerchantConnector {

    public RefMerchantResponse getMerchantById(@NonNull final String id) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String getMerchantByIdUrl = endpointMap().get("getMerchantByIdUrl");
        String endpoint = String.format(getMerchantByIdUrl, id);
        return (RefMerchantResponse) sendHttpRequest(endpoint, HttpMethod.GET, null, headers, RefMerchantResponse.class);
    }

    public RefMerchantResponse getMerchantsByUser(@NonNull final String username) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String getMerchantsByUserUrl = endpointMap().get("getMerchantsByUserUrl");
        String endpoint = String.format(getMerchantsByUserUrl, username);
        return (RefMerchantResponse) sendHttpRequest(endpoint, HttpMethod.GET, null, headers, RefMerchantResponse.class);
    }

    public RefMerchantResponse saveNewMerchant(@NonNull final RefMerchantRequest refMerchantRequest) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String endpoint = endpointMap().get("saveNewMerchantUrl");
        return (RefMerchantResponse) sendHttpRequest(endpoint, HttpMethod.POST, refMerchantRequest, headers, RefMerchantResponse.class);
    }

    public RefMerchantResponse updateMerchant(@NonNull final String id,
                                              @NonNull final RefMerchantRequest refMerchantRequest) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String updateMerchantUrl = endpointMap().get("updateMerchantUrl");
        String endpoint = String.format(updateMerchantUrl, id);
        return (RefMerchantResponse) sendHttpRequest(endpoint, HttpMethod.PUT, refMerchantRequest, headers, RefMerchantResponse.class);
    }

    public RefMerchantResponse deleteMerchant(@NonNull final String id) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String deleteMerchantUrl = endpointMap().get("deleteMerchantUrl");
        String endpoint = String.format(deleteMerchantUrl, id);
        return (RefMerchantResponse) sendHttpRequest(endpoint, HttpMethod.DELETE, null, headers, RefMerchantResponse.class);
    }
}
