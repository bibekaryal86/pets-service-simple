package pets.service.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.service.app.model.AccountFilters;
import pets.service.app.model.AccountRequest;
import pets.service.app.model.AccountResponse;
import pets.service.app.model.Status;
import pets.service.app.service.AccountService;
import pets.service.app.util.Util;

import java.io.IOException;
import java.util.Collections;

public class AccountServletCRUD extends HttpServlet {
    private String getPostRequestType(String requestUri) {
        try {
            return requestUri.split("/")[4];
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean isValidAccountRequest(AccountRequest accountRequest) {
        return accountRequest != null &&
                Util.hasText(accountRequest.getUsername()) &&
                Util.hasText(accountRequest.getDescription()) &&
                Util.hasText(accountRequest.getTypeId()) &&
                Util.hasText(accountRequest.getBankId()) &&
                Util.hasText(accountRequest.getStatus());
    }

    protected void doPostPutDelete(HttpServletRequest request, HttpServletResponse response,
                                   boolean isDelete, boolean isSave, boolean isUpdate) throws IOException {
        String errMsg = null;
        AccountResponse accountResponse = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = Util.getRequestPathParameter(request, 5, 3);

        if (Util.hasText(username)) {
            if (isDelete || isUpdate) {
                String id = request.getParameter("id");

                if (Util.hasText(id)) {
                    if (isDelete) {
                        accountResponse = new AccountService().deleteAccount(username, id);
                    } else {
                        AccountRequest accountRequest = (AccountRequest) Util.getRequestBody(request, AccountRequest.class);

                        if (isValidAccountRequest(accountRequest)) {
                            accountResponse = new AccountService().updateAccount(username, id, accountRequest, true);
                        } else {
                            errMsg = "Error Processing Update Request! Invalid Request Body";
                        }
                    }
                } else {
                    errMsg = String.format("Error Processing Request! Invalid Account Id: %s", id);
                }
            } else if (isSave) {
                AccountRequest accountRequest = (AccountRequest) Util.getRequestBody(request, AccountRequest.class);

                if (isValidAccountRequest(accountRequest)) {
                    accountResponse = new AccountService().saveNewAccount(username, accountRequest, true);
                } else {
                    errMsg = "Error Processing Save Request! Invalid Request Body";
                }
            }
        } else {
            errMsg = String.format("Error Processing Request! Invalid Username: %s", username);
        }

        if (errMsg == null && accountResponse == null) {
            errMsg = "Error Processing Request! Something Went Wrong!! Please Try Again!!!";
        }

        if (errMsg != null) {
            response.setStatus(400);
            accountResponse = AccountResponse.builder()
                    .accounts(Collections.emptyList())
                    .status(Status.builder()
                            .errMsg(errMsg)
                            .build())
                    .build();
        } else {
            if (accountResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        }

        response.getWriter().print(Util.getGson().toJson(accountResponse));
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestType = getPostRequestType(request.getRequestURI());

        if ("user".equals(requestType)) {
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
        AccountResponse accountResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = Util.getRequestPathParameter(request, 5, 3);

        if (Util.hasText(username)) {
            String id = request.getParameter("id");

            if (Util.hasText(id)) {
                accountResponse = new AccountService().getAccountById(username, id, true);
            } else {
                AccountFilters accountFilters = (AccountFilters) Util.getRequestBody(request, AccountFilters.class);
                accountResponse = new AccountService().getAccountsByUser(username, accountFilters, true);
            }

            if (accountResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            accountResponse = AccountResponse.builder()
                    .status(Status.builder()
                            .errMsg(String.format("Error Retrieving Accounts by Invalid Username: %s",
                                    username))
                            .build())
                    .build();
        }

        response.getWriter().print(Util.getGson().toJson(accountResponse));
    }
}
