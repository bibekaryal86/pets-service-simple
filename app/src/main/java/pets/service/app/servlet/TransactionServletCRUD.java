package pets.service.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.service.app.model.Status;
import pets.service.app.model.TransactionFilters;
import pets.service.app.model.TransactionRequest;
import pets.service.app.model.TransactionResponse;
import pets.service.app.service.TransactionService;

import java.io.IOException;

import static java.util.Collections.emptyList;
import static pets.service.app.util.Util.getGson;
import static pets.service.app.util.Util.getRequestBody;
import static pets.service.app.util.Util.getRequestPathParameter;
import static pets.service.app.util.Util.hasText;


public class TransactionServletCRUD extends HttpServlet {
    private String getPostRequestType(String requestUri) {
        try {
            return requestUri.split("/")[4];
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean isValidTransactionRequest(TransactionRequest transactionRequest) {
        return transactionRequest != null &&
                hasText(transactionRequest.getAccountId()) &&
                hasText(transactionRequest.getTypeId()) &&
                hasText(transactionRequest.getCategoryId()) &&
                (hasText(transactionRequest.getMerchantId()) ||
                        hasText(transactionRequest.getNewMerchant())) &&
                hasText(transactionRequest.getUsername()) &&
                hasText(transactionRequest.getDate());
    }

    protected void doPostPutDelete(HttpServletRequest request, HttpServletResponse response,
                                   boolean isDelete, boolean isSave, boolean isUpdate) throws IOException {
        String errMsg = null;
        TransactionResponse transactionResponse = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = getRequestPathParameter(request, 5, 3);

        if (hasText(username)) {
            if (isDelete || isUpdate) {
                String id = request.getParameter("id");

                if (hasText(id)) {
                    if (isDelete) {
                        transactionResponse = new TransactionService().deleteTransaction(id);
                    } else {
                        TransactionRequest transactionRequest = (TransactionRequest) getRequestBody(request, TransactionRequest.class);

                        if (isValidTransactionRequest(transactionRequest)) {
                            transactionResponse = new TransactionService().updateTransaction(username, id, transactionRequest, true);
                        } else {
                            errMsg = "Error Processing Update Request! Invalid Request Body";
                        }
                    }
                } else {
                    errMsg = String.format("Error Processing Request! Invalid Transaction Id: %s", id);
                }
            } else if (isSave) {
                TransactionRequest transactionRequest = (TransactionRequest) getRequestBody(request, TransactionRequest.class);

                if (isValidTransactionRequest(transactionRequest)) {
                    transactionResponse = new TransactionService().saveNewTransaction(username, transactionRequest, true);
                } else {
                    errMsg = "Error Processing Save Request! Invalid Request Body";
                }
            }
        } else {
            errMsg = String.format("Error Processing Request! Invalid Username: %s", username);
        }

        if (errMsg == null && transactionResponse == null) {
            errMsg = "Error Processing Request! Something Went Wrong!! Please Try Again!!!";
        }

        if (errMsg != null) {
            response.setStatus(400);
            transactionResponse = TransactionResponse.builder()
                    .transactions(emptyList())
                    .status(Status.builder()
                            .errMsg(errMsg)
                            .build())
                    .build();
        } else {
            if (transactionResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        }

        response.getWriter().print(getGson().toJson(transactionResponse));
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
        TransactionResponse transactionResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = getRequestPathParameter(request, 5, 3);

        if (hasText(username)) {
            String id = request.getParameter("id");

            if (hasText(id)) {
                transactionResponse = new TransactionService().getTransactionById(username, id, true);
            } else {
                TransactionFilters transactionFilters = (TransactionFilters) getRequestBody(request, TransactionFilters.class);
                transactionResponse = new TransactionService().getTransactionsByUser(username, transactionFilters, true);
            }

            if (transactionResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        } else {
            response.setStatus(400);
            transactionResponse = TransactionResponse.builder()
                    .status(Status.builder()
                            .errMsg(String.format("Error Retrieving Transactions by Invalid Username: %s",
                                    username))
                            .build())
                    .build();
        }

        response.getWriter().print(getGson().toJson(transactionResponse));
    }
}
