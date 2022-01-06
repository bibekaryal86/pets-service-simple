package pets.service.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.service.app.model.RefCategoryTypeResponse;
import pets.service.app.model.Status;
import pets.service.app.service.RefTypesService;

import java.io.IOException;

import static pets.service.app.util.Util.getGson;
import static pets.service.app.util.Util.getRequestPathParameter;
import static pets.service.app.util.Util.hasText;

public class CategoryTypeServletR extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RefCategoryTypeResponse refCategoryTypeResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = getRequestPathParameter(request, 5, 3);
        boolean usedInTxnsOnly = Boolean.parseBoolean(request.getParameter("usedInTxnsOnly"));

        if (hasText(username)) {
            refCategoryTypeResponse = new RefTypesService().getAllCategoryTypes(username, usedInTxnsOnly);

            if (refCategoryTypeResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            refCategoryTypeResponse = RefCategoryTypeResponse.builder()
                    .status(Status.builder()
                            .errMsg(String.format("Error Retrieving Category Types by Invalid Username: %s",
                                    username))
                            .build())
                    .build();
        }

        response.getWriter().print(getGson().toJson(refCategoryTypeResponse));
    }
}
