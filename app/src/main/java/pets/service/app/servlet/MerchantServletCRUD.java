package pets.service.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.service.app.model.RefMerchantFilters;
import pets.service.app.model.RefMerchantRequest;
import pets.service.app.model.RefMerchantResponse;
import pets.service.app.model.Status;
import pets.service.app.service.MerchantService;
import pets.service.app.util.Util;

import java.io.IOException;
import java.util.Collections;

public class MerchantServletCRUD extends HttpServlet {
    protected void doPostPutDelete(HttpServletRequest request, HttpServletResponse response,
                                   boolean isDelete, boolean isSave, boolean isUpdate) throws IOException {
        String errMsg = null;
        RefMerchantResponse refMerchantResponse = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = Util.getRequestPathParameter(request, 5, 3);

        if (Util.hasText(username)) {
            if (isDelete || isUpdate) {
                String id = request.getParameter("id");

                if (Util.hasText(id)) {
                    if (isDelete) {
                        refMerchantResponse = MerchantService.deleteMerchant(username, id);
                    } else {
                        RefMerchantRequest refMerchantRequest = (RefMerchantRequest) Util.getRequestBody(request, RefMerchantRequest.class);

                        if (MerchantService.isValidMerchantRequest(refMerchantRequest)) {
                            refMerchantResponse = MerchantService.updateMerchant(id, refMerchantRequest);
                        } else {
                            errMsg = "Error Processing Update Request! Invalid Request Body";
                        }
                    }
                } else {
                    errMsg = String.format("Error Processing Request! Invalid Merchant Id: %s", id);
                }
            } else if (isSave) {
                RefMerchantRequest refMerchantRequest = (RefMerchantRequest) Util.getRequestBody(request, RefMerchantRequest.class);

                if (MerchantService.isValidMerchantRequest(refMerchantRequest)) {
                    refMerchantResponse = MerchantService.saveNewMerchant(refMerchantRequest);
                } else {
                    errMsg = "Error Processing Save Request! Invalid Request Body";
                }
            }
        } else {
            errMsg = String.format("Error Processing Request! Invalid Username: %s", username);
        }

        if (errMsg == null && refMerchantResponse == null) {
            errMsg = "Error Processing Request! Something Went Wrong!! Please Try Again!!!";
        }

        if (errMsg != null) {
            response.setStatus(400);
            refMerchantResponse = RefMerchantResponse.builder()
                    .refMerchants(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg(errMsg)
                            .build())
                    .build();
        } else {
            if (refMerchantResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        }

        response.getWriter().print(Util.getGson().toJson(refMerchantResponse));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPostPutDelete(request, response, false, true, false);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPostPutDelete(request, response, false, false, true);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPostPutDelete(request, response, true, false, false);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RefMerchantResponse refMerchantResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = Util.getRequestPathParameter(request, 5, 3);

        if (Util.hasText(username)) {
            String id = request.getParameter("id");

            if (Util.hasText(id)) {
                refMerchantResponse = MerchantService.getMerchantById(id);
            } else {
                RefMerchantFilters refMerchantFilters = (RefMerchantFilters) Util.getRequestBody(request, RefMerchantFilters.class);
                refMerchantResponse = MerchantService.getMerchantsByUser(username, refMerchantFilters);
            }

            if (refMerchantResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            refMerchantResponse = RefMerchantResponse.builder()
                    .status(Status.builder()
                            .errMsg(String.format("Error Retrieving Merchants by Invalid Username: %s",
                                    username))
                            .build())
                    .build();
        }

        response.getWriter().print(Util.getGson().toJson(refMerchantResponse));
    }
}
