package pets.service.app.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pets.service.app.connector.UserConnector;
import pets.service.app.model.Status;
import pets.service.app.model.UserRequest;
import pets.service.app.model.UserResponse;
import pets.service.app.util.Util;

import java.util.Collections;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {

    public static UserResponse getUserByUsername(String username) {
        try {
            return UserConnector.getUserByUsername(username);
        } catch (Exception ex) {
            log.error("Exception in Get User by Username: {}", username);
            return UserResponse.builder()
                    .users(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("User Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
    }

    public static UserResponse saveNewUser(UserRequest userRequest) {
        try {
            return UserConnector.saveNewUser(userRequest);
        } catch (Exception ex) {
            log.error("Exception in Save New User: {}", userRequest);
            return UserResponse.builder()
                    .users(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Save User Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
    }

    public static UserResponse updateUser(String id, UserRequest userRequest) {
        try {
            return UserConnector.updateUser(id, userRequest);
        } catch (Exception ex) {
            log.error("Exception in Update New User: {} | {}", id, userRequest);
            return UserResponse.builder()
                    .users(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg("Update User Unavailable! Please Try Again!!!")
                            .build())
                    .build();
        }
    }

    public static boolean isValidUserRequest(UserRequest userRequest) {
        return userRequest != null &&
                Util.hasText(userRequest.getUsername()) &&
                Util.hasText(userRequest.getPassword()) &&
                Util.hasText(userRequest.getFirstName()) &&
                Util.hasText(userRequest.getLastName()) &&
                Util.hasText(userRequest.getEmail()) &&
                Util.hasText(userRequest.getPhone()) &&
                Util.hasText(userRequest.getStatus());
    }
}
