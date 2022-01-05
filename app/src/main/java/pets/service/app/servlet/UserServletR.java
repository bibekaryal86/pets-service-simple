package pets.service.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.service.app.model.Status;
import pets.service.app.model.UserResponse;
import pets.service.app.service.UserService;
import pets.service.app.util.Util;

import java.io.IOException;

public class UserServletR extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserResponse userResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = Util.getRequestPathParameter(request, 6, 5);

        if (Util.hasText(username)) {
            userResponse = UserService.getUserByUsername(username);

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

        response.getWriter().print(Util.getGson().toJson(userResponse));
    }
}
