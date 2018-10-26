package com.olacabs.jackhammer.filters;

import com.google.inject.Inject;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.common.HttpResponseMessages;
import com.olacabs.jackhammer.models.Task;
import com.olacabs.jackhammer.models.User;
import com.olacabs.jackhammer.security.JwtSecurity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import java.io.IOException;

@Slf4j
public class AuthorizationFilter implements Filter {

    @Inject
    JwtSecurity jwtSecurity;

    @Inject
    Task task;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            Boolean validAuthorization = validateAuthorization(servletRequest);
            if (validAuthorization) {
                filterChain.doFilter(servletRequest, servletResponse); // This signals that the request should pass this filter
            } else {
                HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                httpResponse.setStatus(HttpStatus.UNAUTHORIZED_401);
                httpResponse.getWriter().print(HttpResponseMessages.UNAUTHORIZED);
            }
        }
    }

    private Boolean validateAuthorization(ServletRequest servletRequest) {
        String accessToken = ((HttpServletRequest) servletRequest).getHeader(HttpKeys.AUTHORIZATION);
        User currentUser = jwtSecurity.getCurrentUser(accessToken);
        if (currentUser == null) return false;

        String requestURI = ((HttpServletRequest) servletRequest).getPathInfo();
        String requestMethod = ((HttpServletRequest) servletRequest).getMethod();

        if (isLogout(requestURI) || isResetPassword(requestURI)) return true;

        if (StringUtils.equals(requestMethod, HttpMethod.GET) || StringUtils.equals(requestMethod, HttpMethod.PUT) || StringUtils.equals(requestMethod, HttpMethod.DELETE)) {
            String[] requestApi = requestURI.split(Constants.URL_SEPARATOR);
            if (isStringInt(requestApi[requestApi.length - 1]) == false) return false;
            requestApi[requestApi.length - 1] = Constants.ID_PARAM;
            String dbFormatUri = StringUtils.join(requestApi, Constants.URL_SEPARATOR);
            if (StringUtils.equals(requestMethod, HttpMethod.GET)) {
                Boolean canAccess = task.canAccess(currentUser, dbFormatUri, HttpMethod.PUT);
                if (canAccess) return true;
                return task.canAccess(currentUser, dbFormatUri, requestMethod);
            } else {
                return task.canAccess(currentUser, dbFormatUri, requestMethod);
            }
        } else {
            if(requestURI.contains(Constants.CREATE_SCAN)) requestURI = Constants.CREATE_SCAN_API;
            return task.canAccess(currentUser, requestURI, requestMethod);
        }
    }

    private Boolean isLogout(String requestURI) {
        return StringUtils.equals(requestURI, Constants.LOGOUT_API);
    }

    private Boolean isResetPassword(String requestURI) {
        return StringUtils.equals(requestURI, Constants.RESET_PASSWORD_API);
    }

    @Override
    public void destroy() {
    }

    private Boolean isStringInt(String paramValue) {
        try {
            Integer.parseInt(paramValue);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
