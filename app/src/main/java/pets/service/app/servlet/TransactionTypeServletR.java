package pets.service.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.service.app.model.RefTransactionTypeResponse;
import pets.service.app.model.Status;
import pets.service.app.service.RefTypesService;

import java.io.IOException;

import static pets.service.app.util.Util.getGson;
import static pets.service.app.util.Util.getRequestPathParameter;
import static pets.service.app.util.Util.hasText;

public class TransactionTypeServletR extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RefTransactionTypeResponse refTransactionTypeResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = getRequestPathParameter(request, 5, 3);

        if (hasText(username)) {
            refTransactionTypeResponse = new RefTypesService().getAllTransactionTypes();

            if (refTransactionTypeResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            refTransactionTypeResponse = RefTransactionTypeResponse.builder()
                    .status(Status.builder()
                            .errMsg(String.format("Error Retrieving Transaction Types by Invalid Username: %s",
                                    username))
                            .build())
                    .build();
        }

        response.getWriter().print(getGson().toJson(refTransactionTypeResponse));
    }
}
