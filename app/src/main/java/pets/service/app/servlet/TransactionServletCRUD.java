package pets.service.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.service.app.model.Status;
import pets.service.app.model.TransactionFilters;
import pets.service.app.model.TransactionRequest;
import pets.service.app.model.TransactionResponse;
import pets.service.app.service.TransactionService;
import pets.service.app.util.Util;

import java.io.IOException;
import java.util.Collections;

public class TransactionServletCRUD extends HttpServlet {
    protected void doPostPutDelete(HttpServletRequest request, HttpServletResponse response,
                                   boolean isDelete, boolean isSave, boolean isUpdate) throws IOException {
        String errMsg = null;
        TransactionResponse transactionResponse = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = Util.getRequestPathParameter(request, 5, 3);

        if (Util.hasText(username)) {
            if (isDelete || isUpdate) {
                String id = request.getParameter("id");

                if (Util.hasText(id)) {
                    if (isDelete) {
                        transactionResponse = TransactionService.deleteTransaction(id);
                    } else {
                        TransactionRequest transactionRequest = (TransactionRequest) Util.getRequestBody(request, TransactionRequest.class);

                        if (TransactionService.isValidTransactionRequest(transactionRequest)) {
                            transactionResponse = TransactionService.updateTransaction(username, id, transactionRequest, true);
                        } else {
                            errMsg = "Error Processing Update Request! Invalid Request Body";
                        }
                    }
                } else {
                    errMsg = String.format("Error Processing Request! Invalid Transaction Id: %s", id);
                }
            } else if (isSave) {
                TransactionRequest transactionRequest = (TransactionRequest) Util.getRequestBody(request, TransactionRequest.class);

                if (TransactionService.isValidTransactionRequest(transactionRequest)) {
                    transactionResponse = TransactionService.saveNewTransaction(username, transactionRequest, true);
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
                    .transactions(Collections.emptyList())
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

        response.getWriter().print(Util.getGson().toJson(transactionResponse));
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
        TransactionResponse transactionResponse;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = Util.getRequestPathParameter(request, 5, 3);

        if (Util.hasText(username)) {
            String id = request.getParameter("id");

            if (Util.hasText(id)) {
                transactionResponse = TransactionService.getTransactionById(username, id, true);
            } else {
                TransactionFilters transactionFilters = (TransactionFilters) Util.getRequestBody(request, TransactionFilters.class);
                transactionResponse = TransactionService.getTransactionsByUser(username, transactionFilters, true);
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

        response.getWriter().print(Util.getGson().toJson(transactionResponse));
    }
}
