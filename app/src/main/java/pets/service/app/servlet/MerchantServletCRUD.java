package pets.service.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.service.app.model.RefMerchantFilters;
import pets.service.app.model.RefMerchantRequest;
import pets.service.app.model.RefMerchantResponse;
import pets.service.app.model.Status;
import pets.service.app.service.MerchantService;

import java.io.IOException;

import static java.util.Collections.emptyList;
import static pets.service.app.util.Util.getGson;
import static pets.service.app.util.Util.getRequestBody;
import static pets.service.app.util.Util.getRequestPathParameter;
import static pets.service.app.util.Util.hasText;


public class MerchantServletCRUD extends HttpServlet {
    private String getPostRequestType(String requestUri) {
        try {
            return requestUri.split("/")[4];
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean isValidMerchantRequest(RefMerchantRequest refMerchantRequest) {
        return refMerchantRequest != null &&
                hasText(refMerchantRequest.getUsername()) &&
                hasText(refMerchantRequest.getDescription());
    }

    protected void doPostPutDelete(HttpServletRequest request, HttpServletResponse response,
                                   boolean isDelete, boolean isSave, boolean isUpdate) throws IOException {
        String errMsg = null;
        RefMerchantResponse refMerchantResponse = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = getRequestPathParameter(request, 5, 3);

        if (hasText(username)) {
            if (isDelete || isUpdate) {
                String id = request.getParameter("id");

                if (hasText(id)) {
                    if (isDelete) {
                        refMerchantResponse = new MerchantService().deleteMerchant(username, id);
                    } else {
                        RefMerchantRequest refMerchantRequest = (RefMerchantRequest) getRequestBody(request, RefMerchantRequest.class);

                        if (isValidMerchantRequest(refMerchantRequest)) {
                            refMerchantResponse = new MerchantService().updateMerchant(id, refMerchantRequest);
                        } else {
                            errMsg = "Error Processing Update Request! Invalid Request Body";
                        }
                    }
                } else {
                    errMsg = String.format("Error Processing Request! Invalid Merchant Id: %s", id);
                }
            } else if (isSave) {
                RefMerchantRequest refMerchantRequest = (RefMerchantRequest) getRequestBody(request, RefMerchantRequest.class);

                if (isValidMerchantRequest(refMerchantRequest)) {
                    refMerchantResponse = new MerchantService().saveNewMerchant(refMerchantRequest);
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
                    .refMerchants(emptyList())
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

        response.getWriter().print(getGson().toJson(refMerchantResponse));
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestType = getPostRequestType(request.getRequestURI());
        if ("merchants".equals(requestType)) {
            doPostGet(request, response);
        } else {
            doPostPutDelete(request, response, false, true, false);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPostPutDelete(request, response, false, false, true);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPostPutDelete(request, response, true, false, false);
    }

    protected void doPostGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RefMerchantResponse refMerchantResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = getRequestPathParameter(request, 5, 3);

        if (hasText(username)) {
            String id = request.getParameter("id");

            if (hasText(id)) {
                refMerchantResponse = new MerchantService().getMerchantById(id);
            } else {
                RefMerchantFilters refMerchantFilters = (RefMerchantFilters) getRequestBody(request, RefMerchantFilters.class);
                refMerchantResponse = new MerchantService().getMerchantsByUser(username, refMerchantFilters);
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

        response.getWriter().print(getGson().toJson(refMerchantResponse));
    }
}
