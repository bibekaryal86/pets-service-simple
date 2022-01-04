package pets.service.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.service.app.model.RefBankResponse;
import pets.service.app.model.Status;
import pets.service.app.service.RefTypesService;
import pets.service.app.util.Util;

import java.io.IOException;

public class BankServletR extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RefBankResponse refBankResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = Util.getRequestPathParameter(request);

        if (Util.hasText(username)) {
            refBankResponse = RefTypesService.getAllBanks();

            if (refBankResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            refBankResponse = RefBankResponse.builder()
                    .status(Status.builder()
                            .errMsg(String.format("Error Retrieving Banks by Invalid Username: %s",
                                    username))
                            .build())
                    .build();
        }

        response.getWriter().print(Util.getGson().toJson(refBankResponse));
    }
}
