package pets.service.app.connector;

import lombok.NonNull;
import org.eclipse.jetty.http.HttpMethod;
import pets.service.app.model.UserRequest;
import pets.service.app.model.UserResponse;
import pets.service.app.util.ConnectorUtil;
import pets.service.app.util.EndpointUtil;
import pets.service.app.util.Util;

import java.util.Map;

public class UserConnector {

    public UserResponse getUserByUsername(@NonNull final String username) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        headers.put("user-header", username);
        String getUserByUsernameUrl = EndpointUtil.endpointMap().get("getUserByUsernameUrl");
        String endpoint = String.format(getUserByUsernameUrl, username);
        return (UserResponse) ConnectorUtil.sendHttpRequest(
                endpoint, HttpMethod.GET, null, headers, UserResponse.class);
    }

    public UserResponse saveNewUser(@NonNull final UserRequest userRequest) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String endpoint = EndpointUtil.endpointMap().get("saveNewUserUrl");
        return (UserResponse) ConnectorUtil.sendHttpRequest(
                endpoint, HttpMethod.POST, userRequest, headers, UserResponse.class);
    }

    public UserResponse updateUser(@NonNull final String id, @NonNull final UserRequest userRequest) {
        Map<String, String> headers = Util.getPetsDatabaseAuthHeaders();
        String updateUserPutUrl = EndpointUtil.endpointMap().get("updateUserPutUrl");
        String endpoint = String.format(updateUserPutUrl, id);
        return (UserResponse) ConnectorUtil.sendHttpRequest(
                endpoint, HttpMethod.PUT, userRequest, headers, UserResponse.class);
    }
}
