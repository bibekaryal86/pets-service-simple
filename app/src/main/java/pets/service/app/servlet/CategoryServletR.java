package pets.service.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.service.app.model.RefCategoryFilters;
import pets.service.app.model.RefCategoryResponse;
import pets.service.app.model.Status;
import pets.service.app.service.RefTypesService;

import java.io.IOException;

import static pets.service.app.util.Util.getGson;
import static pets.service.app.util.Util.getRequestBody;
import static pets.service.app.util.Util.getRequestPathParameter;
import static pets.service.app.util.Util.hasText;

public class CategoryServletR extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RefCategoryResponse refCategoryResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = getRequestPathParameter(request, 5, 3);
        RefCategoryFilters refCategoryFilters = (RefCategoryFilters) getRequestBody(request, RefCategoryFilters.class);

        if (hasText(username)) {
            refCategoryResponse = new RefTypesService().getAllCategories(username, refCategoryFilters);

            if (refCategoryResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            refCategoryResponse = RefCategoryResponse.builder()
                    .status(Status.builder()
                            .errMsg(String.format("Error Retrieving Categories by Invalid Username: %s",
                                    username))
                            .build())
                    .build();
        }

        response.getWriter().print(getGson().toJson(refCategoryResponse));
    }
}
