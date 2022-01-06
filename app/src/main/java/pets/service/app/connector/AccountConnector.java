package pets.service.app.connector;

import lombok.NonNull;
import org.eclipse.jetty.http.HttpMethod;
import pets.service.app.model.AccountRequest;
import pets.service.app.model.AccountResponse;

import java.util.Map;

import static pets.service.app.util.ConnectorUtil.sendHttpRequest;
import static pets.service.app.util.EndpointUtil.endpointMap;
import static pets.service.app.util.Util.getPetsDatabaseAuthHeaders;

public class AccountConnector {

    public AccountResponse getAccountById(@NonNull final String id) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String getAccountByIdUrl = endpointMap().get("getAccountByIdUrl");
        String endpoint = String.format(getAccountByIdUrl, id);
        return (AccountResponse) sendHttpRequest(endpoint, HttpMethod.GET, null, headers, AccountResponse.class);
    }

    public AccountResponse getAccountsByUser(@NonNull final String username) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String getAccountsByUserUrl = endpointMap().get("getAccountsByUserUrl");
        String endpoint = String.format(getAccountsByUserUrl, username);
        return (AccountResponse) sendHttpRequest(endpoint, HttpMethod.GET, null, headers, AccountResponse.class);
    }

    public AccountResponse saveNewAccount(@NonNull final AccountRequest accountRequest) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String endpoint = endpointMap().get("saveNewAccountUrl");
        return (AccountResponse) sendHttpRequest(endpoint, HttpMethod.POST, accountRequest, headers, AccountResponse.class);
    }

    public AccountResponse updateAccount(@NonNull final String id,
                                         @NonNull final AccountRequest accountRequest) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String updateAccountPutUrl = endpointMap().get("updateAccountPutUrl");
        String endpoint = String.format(updateAccountPutUrl, id);
        return (AccountResponse) sendHttpRequest(endpoint, HttpMethod.PUT, accountRequest, headers, AccountResponse.class);
    }

    public AccountResponse deleteAccount(@NonNull final String id) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String deleteAccountUrl = endpointMap().get("deleteAccountUrl");
        String endpoint = String.format(deleteAccountUrl, id);
        return (AccountResponse) sendHttpRequest(endpoint, HttpMethod.DELETE, null, headers, AccountResponse.class);
    }
}
