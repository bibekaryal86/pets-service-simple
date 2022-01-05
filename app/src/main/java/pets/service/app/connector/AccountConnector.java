package pets.service.app.connector;

import lombok.NonNull;
import org.eclipse.jetty.http.HttpMethod;
import pets.service.app.model.AccountRequest;
import pets.service.app.model.AccountResponse;
import pets.service.app.util.ConnectorUtil;
import pets.service.app.util.EndpointUtil;
import pets.service.app.util.Util;

import java.util.Map;

public class AccountConnector {

    public AccountResponse getAccountById(@NonNull final String id) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String getAccountByIdUrl = EndpointUtil.endpointMap().get("getAccountByIdUrl");
        String endpoint = String.format(getAccountByIdUrl, id);
        return (AccountResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.GET, null, headers, AccountResponse.class);
    }

    public AccountResponse getAccountsByUser(@NonNull final String username) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String getAccountsByUserUrl = EndpointUtil.endpointMap().get("getAccountsByUserUrl");
        String endpoint = String.format(getAccountsByUserUrl, username);
        return (AccountResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.GET, null, headers, AccountResponse.class);
    }

    public AccountResponse saveNewAccount(@NonNull final AccountRequest accountRequest) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String endpoint = EndpointUtil.endpointMap().get("saveNewAccountUrl");
        return (AccountResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.POST, accountRequest, headers, AccountResponse.class);
    }

    public AccountResponse updateAccount(@NonNull final String id,
                                         @NonNull final AccountRequest accountRequest) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String updateAccountPutUrl = EndpointUtil.endpointMap().get("updateAccountPutUrl");
        String endpoint = String.format(updateAccountPutUrl, id);
        return (AccountResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.PUT, accountRequest, headers, AccountResponse.class);
    }

    public AccountResponse deleteAccount(@NonNull final String id) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String deleteAccountUrl = EndpointUtil.endpointMap().get("deleteAccountUrl");
        String endpoint = String.format(deleteAccountUrl, id);
        return (AccountResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.DELETE, null, headers, AccountResponse.class);
    }
}
