package pets.service.app.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import pets.service.app.util.Util;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class ServletFilter implements Filter {

    private static final String TRACE = "TRACE";
    private static final List<String> DO_NOT_FILTER = List.of(Util.CONTEXT_PATH + "/tests/ping");

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if ("/favicon.ico".equals(httpServletRequest.getRequestURI())) {
            httpServletResponse.setStatus(200);
        } else {
            httpServletRequest.setAttribute(TRACE, ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE));
            logRequest(httpServletRequest);

            if (DO_NOT_FILTER.contains(httpServletRequest.getRequestURI()) ||
                    Util.isAuthenticatedRequest(httpServletRequest)) {
                chain.doFilter(request, response);
                logResponse(httpServletRequest, httpServletResponse);
            } else {
                httpServletResponse.setCharacterEncoding("utf-8");
                httpServletResponse.setContentType("application/json");
                httpServletResponse.setStatus(401);
                httpServletResponse.getWriter().print("{\"errMsg\": \"Error! Authorization Missing!! Please Try Again!!!\"}");
                logResponse(httpServletRequest, httpServletResponse);
            }
        }
    }

    private void logRequest(HttpServletRequest httpServletRequest) {
        log.info("[ {} ] | REQUEST::: Incoming: [ {} ] | Method: [ {} ]",
                httpServletRequest.getAttribute(TRACE), httpServletRequest.getRequestURI(), httpServletRequest.getMethod());
    }

    private void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        log.info("[ {} ] | RESPONSE::: Status [ {} ]", httpServletRequest.getAttribute(TRACE), httpServletResponse.getStatus());
    }
}
