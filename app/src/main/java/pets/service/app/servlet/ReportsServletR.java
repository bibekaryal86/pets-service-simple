package pets.service.app.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pets.service.app.model.ReportsResponse;
import pets.service.app.model.Status;
import pets.service.app.service.ReportsService;
import pets.service.app.util.Util;

import java.io.IOException;
import java.util.Collections;

public class ReportsServletR extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String errMsg = null;
        ReportsResponse reportsResponse = null;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        String username = Util.getRequestPathParameter(request, 5, 3);

        if (Util.hasText(username)) {
            String reportType = ReportsService.findReportTypeRequest(request.getRequestURI());

            if ("currentbalances".equals(reportType)) {
                reportsResponse = ReportsService.getCurrentBalancesReport(username);
            } else {
                String selectedYear = request.getParameter("selectedyear");

                if (Util.hasText(selectedYear)) {
                    if ("cashflows".equals(reportType)) {
                        reportsResponse = ReportsService.getCashFlowsReport(username, selectedYear);
                    } else if ("categories".equals(reportType)) {
                        reportsResponse = ReportsService.getCategoriesReport(username, selectedYear);
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
                    .reportCashFlows(Collections.emptyList())
                    .reportCategoryTypes(Collections.emptyList())
                    .reportCategoryTypes(Collections.emptyList())
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

        response.getWriter().print(Util.getGson().toJson(reportsResponse));
    }
}
