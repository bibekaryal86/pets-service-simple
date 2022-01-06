package pets.service.app.connector;

import lombok.NonNull;
import org.eclipse.jetty.http.HttpMethod;
import pets.service.app.model.UserRequest;
import pets.service.app.model.UserResponse;

import java.util.Map;

import static pets.service.app.util.ConnectorUtil.sendHttpRequest;
import static pets.service.app.util.EndpointUtil.endpointMap;
import static pets.service.app.util.Util.getPetsDatabaseAuthHeaders;

public class UserConnector {

    public UserResponse getUserByUsername(@NonNull final String username) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        headers.put("user-header", username);
        String getUserByUsernameUrl = endpointMap().get("getUserByUsernameUrl");
        String endpoint = String.format(getUserByUsernameUrl, username);
        return (UserResponse) sendHttpRequest(endpoint, HttpMethod.GET, null, headers, UserResponse.class);
    }

    public UserResponse saveNewUser(@NonNull final UserRequest userRequest) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String endpoint = endpointMap().get("saveNewUserUrl");
        return (UserResponse) sendHttpRequest(endpoint, HttpMethod.POST, userRequest, headers, UserResponse.class);
    }

    public UserResponse updateUser(@NonNull final String id, @NonNull final UserRequest userRequest) {
        Map<String, String> headers = getPetsDatabaseAuthHeaders();
        String updateUserPutUrl = endpointMap().get("updateUserPutUrl");
        String endpoint = String.format(updateUserPutUrl, id);
        return (UserResponse) sendHttpRequest(endpoint, HttpMethod.PUT, userRequest, headers, UserResponse.class);
    }
}
