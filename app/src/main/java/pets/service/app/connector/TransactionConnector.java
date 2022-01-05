package pets.service.app.connector;

import lombok.NonNull;
import org.eclipse.jetty.http.HttpMethod;
import pets.service.app.model.TransactionRequest;
import pets.service.app.model.TransactionResponse;
import pets.service.app.util.ConnectorUtil;
import pets.service.app.util.EndpointUtil;
import pets.service.app.util.Util;

import java.util.Map;

public class TransactionConnector {

    public TransactionResponse getTransactionById(@NonNull final String id) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String getTransactionByIdUrl = EndpointUtil.endpointMap().get("getTransactionByIdUrl");
        String endpoint = String.format(getTransactionByIdUrl, id);
        return (TransactionResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.GET, null, headers, TransactionResponse.class);
    }

    public TransactionResponse getTransactionsByUser(@NonNull final String username) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String getTransactionsByUserUrl = EndpointUtil.endpointMap().get("getTransactionsByUserUrl");
        String endpoint = String.format(getTransactionsByUserUrl, username);
        return (TransactionResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.GET, null, headers, TransactionResponse.class);
    }

    public TransactionResponse saveNewTransaction(@NonNull final TransactionRequest transactionRequest) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String endpoint = EndpointUtil.endpointMap().get("saveNewTransactionUrl");
        return (TransactionResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.POST, transactionRequest, headers, TransactionResponse.class);
    }

    public TransactionResponse updateTransaction(@NonNull final String id,
                                                 @NonNull final TransactionRequest transactionRequest) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String updateTransactionPutUrl = EndpointUtil.endpointMap().get("updateTransactionPutUrl");
        String endpoint = String.format(updateTransactionPutUrl, id);
        return (TransactionResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.PUT, transactionRequest, headers, TransactionResponse.class);
    }

    public TransactionResponse deleteTransaction(@NonNull final String id) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String deleteTransactionUrl = EndpointUtil.endpointMap().get("deleteTransactionUrl");
        String endpoint = String.format(deleteTransactionUrl, id);
        return (TransactionResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.DELETE, null, headers, TransactionResponse.class);
    }

    public TransactionResponse deleteTransactionsByAccount(@NonNull final String accountId) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String deleteTransactionsByAccountUrl = EndpointUtil.endpointMap().get("deleteTransactionsByAccountUrl");
        String endpoint = String.format(deleteTransactionsByAccountUrl, accountId);
        return (TransactionResponse) ConnectorUtil.sendHttpRequest(endpoint, HttpMethod.DELETE, null, headers, TransactionResponse.class);
    }
}
