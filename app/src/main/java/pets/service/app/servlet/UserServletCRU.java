package pets.service.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.service.app.model.Status;
import pets.service.app.model.UserRequest;
import pets.service.app.model.UserResponse;
import pets.service.app.service.UserService;

import java.io.IOException;

import static pets.service.app.util.Util.getGson;
import static pets.service.app.util.Util.getRequestBody;
import static pets.service.app.util.Util.getRequestPathParameter;
import static pets.service.app.util.Util.hasText;

public class UserServletCRU extends HttpServlet {
    private static final String ENCODING = "utf-8";
    private static final String CONTENT = "application/json";

    private boolean isValidUserRequest(UserRequest userRequest) {
        return userRequest != null &&
                hasText(userRequest.getUsername()) &&
                hasText(userRequest.getPassword()) &&
                hasText(userRequest.getFirstName()) &&
                hasText(userRequest.getLastName()) &&
                hasText(userRequest.getEmail()) &&
                hasText(userRequest.getPhone()) &&
                hasText(userRequest.getStatus());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserResponse userResponse;
        response.setCharacterEncoding(ENCODING);
        response.setContentType(CONTENT);

        UserRequest userRequest = (UserRequest) getRequestBody(request, UserRequest.class);

        if (isValidUserRequest(userRequest)) {
            userResponse = new UserService().saveNewUser(userRequest);

            if (userResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            userResponse = UserResponse.builder()
                    .status(Status.builder()
                            .errMsg("Error Saving User! Invalid Request Body!!")
                            .build())
                    .build();
        }

        response.getWriter().print(getGson().toJson(userResponse));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserResponse userResponse;
        response.setCharacterEncoding(ENCODING);
        response.setContentType(CONTENT);

        String username = getRequestPathParameter(request, 6, 5);

        if (hasText(username)) {
            userResponse = new UserService().getUserByUsername(username);

            if (userResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            userResponse = UserResponse.builder()
                    .status(Status.builder()
                            .errMsg(String.format("Error Retrieving User by Invalid Username: %s",
                                    username))
                            .build())
                    .build();
        }

        response.getWriter().print(getGson().toJson(userResponse));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserResponse userResponse;
        response.setCharacterEncoding(ENCODING);
        response.setContentType(CONTENT);

        String username = getRequestPathParameter(request, 6, 4);
        UserRequest userRequest = (UserRequest) getRequestBody(request, UserRequest.class);
        String id = request.getParameter("id");

        if (hasText(username) && hasText(id) && isValidUserRequest(userRequest)) {
            userResponse = new UserService().updateUser(username, userRequest);

            if (userResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            userResponse = UserResponse.builder()
                    .status(Status.builder()
                            .errMsg(String.format("Error Updating User! Invalid User Id / Request Body: %s", username))
                            .build())
                    .build();
        }

        response.getWriter().print(getGson().toJson(userResponse));
    }
}
