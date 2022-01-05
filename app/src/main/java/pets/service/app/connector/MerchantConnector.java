package pets.service.app.connector;

import lombok.NonNull;
import org.eclipse.jetty.http.HttpMethod;
import pets.service.app.model.RefMerchantRequest;
import pets.service.app.model.RefMerchantResponse;
import pets.service.app.util.ConnectorUtil;
import pets.service.app.util.EndpointUtil;
import pets.service.app.util.Util;

import java.util.Map;

public class MerchantConnector {

    public RefMerchantResponse getMerchantById(@NonNull final String id) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String getMerchantByIdUrl = EndpointUtil.endpointMap().get("getMerchantByIdUrl");
        String endpoint = String.format(getMerchantByIdUrl, id);
        return (RefMerchantResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.GET, null, headers, RefMerchantResponse.class);
    }

    public RefMerchantResponse getMerchantsByUser(@NonNull final String username) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String getMerchantsByUserUrl = EndpointUtil.endpointMap().get("getMerchantsByUserUrl");
        String endpoint = String.format(getMerchantsByUserUrl, username);
        return (RefMerchantResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.GET, null, headers, RefMerchantResponse.class);
    }

    public RefMerchantResponse saveNewMerchant(@NonNull final RefMerchantRequest refMerchantRequest) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String endpoint = EndpointUtil.endpointMap().get("saveNewMerchantUrl");
        return (RefMerchantResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.POST, refMerchantRequest, headers, RefMerchantResponse.class);
    }

    public RefMerchantResponse updateMerchant(@NonNull final String id,
                                              @NonNull final RefMerchantRequest refMerchantRequest) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String updateMerchantUrl = EndpointUtil.endpointMap().get("updateMerchantUrl");
        String endpoint = String.format(updateMerchantUrl, id);
        return (RefMerchantResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.PUT, refMerchantRequest, headers, RefMerchantResponse.class);
    }

    public RefMerchantResponse deleteMerchant(@NonNull final String id) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String deleteMerchantUrl = EndpointUtil.endpointMap().get("deleteMerchantUrl");
        String endpoint = String.format(deleteMerchantUrl, id);
        return (RefMerchantResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.DELETE, null, headers, RefMerchantResponse.class);
    }
}
