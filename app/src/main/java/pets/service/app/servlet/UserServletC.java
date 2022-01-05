package pets.service.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.service.app.model.Status;
import pets.service.app.model.UserRequest;
import pets.service.app.model.UserResponse;
import pets.service.app.service.UserService;
import pets.service.app.util.Util;

import java.io.IOException;

public class UserServletC extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserResponse userResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        UserRequest userRequest = (UserRequest) Util.getRequestBody(request, UserRequest.class);

        if (UserService.isValidUserRequest(userRequest)) {
            userResponse = UserService.saveNewUser(userRequest);

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

        response.getWriter().print(Util.getGson().toJson(userResponse));
    }
}
