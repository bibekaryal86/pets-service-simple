package pets.service.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.service.app.model.ReportsResponse;
import pets.service.app.model.Status;
import pets.service.app.service.ReportsService;

import java.io.IOException;

import static java.util.Collections.emptyList;
import static pets.service.app.util.Util.getGson;
import static pets.service.app.util.Util.getRequestPathParameter;
import static pets.service.app.util.Util.hasText;

public class ReportsServletR extends HttpServlet {
    private String findReportTypeRequest(String requestUri) {
        try {
            return requestUri.split("/")[4];
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String errMsg = null;
        ReportsResponse reportsResponse = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = getRequestPathParameter(request, 5, 3);

        if (hasText(username)) {
            String reportType = findReportTypeRequest(request.getRequestURI());

            if ("currentbalances".equals(reportType)) {
                reportsResponse = new ReportsService().getCurrentBalancesReport(username);
            } else {
                String selectedYear = request.getParameter("selectedyear");

                if (hasText(selectedYear)) {
                    if ("cashflows".equals(reportType)) {
                        reportsResponse = new ReportsService().getCashFlowsReport(username, selectedYear);
                    } else if ("categories".equals(reportType)) {
                        reportsResponse = new ReportsService().getCategoriesReport(username, selectedYear);
                    }
                } else {
                    errMsg = String.format("Error Retrieving Reports by Invalid ReportType: %s", reportType);
                }
            }
        } else {
            errMsg = String.format("Error Retrieving Accounts by Invalid Username: %s", username);
        }

        if (errMsg == null && reportsResponse == null) {
            errMsg = "Error Processing Request! Something Went Wrong!! Please Try Again!!!";
        }

        if (errMsg != null) {
            response.setStatus(400);
            reportsResponse = ReportsResponse.builder()
                    .reportCashFlows(emptyList())
                    .reportCategoryTypes(emptyList())
                    .reportCategoryTypes(emptyList())
                    .status(Status.builder()
                            .errMsg(errMsg)
                            .build())
                    .build();
        } else {
            if (reportsResponse.getStatus() == null) {
                response.setStatus(200);
            } else {
                response.setStatus(500);
            }
        }

        response.getWriter().print(getGson().toJson(reportsResponse));
    }
}
