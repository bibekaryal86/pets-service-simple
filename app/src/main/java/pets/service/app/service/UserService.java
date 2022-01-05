package pets.service.app.service;

import lombok.extern.slf4j.Slf4j;
import pets.service.app.connector.UserConnector;
import pets.service.app.model.Status;
import pets.service.app.model.UserRequest;
import pets.service.app.model.UserResponse;

import java.util.Collections;

@Slf4j
public class UserService {

    public UserResponse getUserByUsername(String username) {
        try {
            return new UserConnector().getUserByUsername(username);
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

    public UserResponse saveNewUser(UserRequest userRequest) {
        try {
            return new UserConnector().saveNewUser(userRequest);
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

    public UserResponse updateUser(String id, UserRequest userRequest) {
        try {
            return new UserConnector().updateUser(id, userRequest);
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
}
