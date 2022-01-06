package pets.service.app.connector;

import lombok.NonNull;
import org.eclipse.jetty.http.HttpMethod;
import pets.service.app.model.TransactionRequest;
import pets.service.app.model.TransactionResponse;

import java.util.Map;

import static pets.service.app.util.ConnectorUtil.sendHttpRequest;
import static pets.service.app.util.EndpointUtil.endpointMap;
import static pets.service.app.util.Util.getPetsDatabaseAuthHeaders;

public class TransactionConnector {

    public TransactionResponse getTransactionById(@NonNull final String id) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String getTransactionByIdUrl = endpointMap().get("getTransactionByIdUrl");
        String endpoint = String.format(getTransactionByIdUrl, id);
        return (TransactionResponse) sendHttpRequest(endpoint, HttpMethod.GET, null, headers, TransactionResponse.class);
    }

    public TransactionResponse getTransactionsByUser(@NonNull final String username) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String getTransactionsByUserUrl = endpointMap().get("getTransactionsByUserUrl");
        String endpoint = String.format(getTransactionsByUserUrl, username);
        return (TransactionResponse) sendHttpRequest(endpoint, HttpMethod.GET, null, headers, TransactionResponse.class);
    }

    public TransactionResponse saveNewTransaction(@NonNull final TransactionRequest transactionRequest) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String endpoint = endpointMap().get("saveNewTransactionUrl");
        return (TransactionResponse) sendHttpRequest(endpoint, HttpMethod.POST, transactionRequest, headers, TransactionResponse.class);
    }

    public TransactionResponse updateTransaction(@NonNull final String id,
                                                 @NonNull final TransactionRequest transactionRequest) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String updateTransactionPutUrl = endpointMap().get("updateTransactionPutUrl");
        String endpoint = String.format(updateTransactionPutUrl, id);
        return (TransactionResponse) sendHttpRequest(endpoint, HttpMethod.PUT, transactionRequest, headers, TransactionResponse.class);
    }

    public TransactionResponse deleteTransaction(@NonNull final String id) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String deleteTransactionUrl = endpointMap().get("deleteTransactionUrl");
        String endpoint = String.format(deleteTransactionUrl, id);
        return (TransactionResponse) sendHttpRequest(endpoint, HttpMethod.DELETE, null, headers, TransactionResponse.class);
    }

    public TransactionResponse deleteTransactionsByAccount(@NonNull final String accountId) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String deleteTransactionsByAccountUrl = endpointMap().get("deleteTransactionsByAccountUrl");
        String endpoint = String.format(deleteTransactionsByAccountUrl, accountId);
        return (TransactionResponse) sendHttpRequest(endpoint, HttpMethod.DELETE, null, headers, TransactionResponse.class);
    }
}
