package com.olacabs.jackhammer.filters;

import com.google.inject.Inject;

import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.*;
import com.olacabs.jackhammer.db.RoleTaskDAO;
import com.olacabs.jackhammer.db.RoleUserDAO;
import com.olacabs.jackhammer.db.TaskDAO;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.models.*;
import com.olacabs.jackhammer.security.JwtSecurity;

import com.olacabs.jackhammer.service.factories.DataServiceBuilderFactory;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.HttpStatus;

import javax.annotation.Priority;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Slf4j
@Priority(7000)
public class AuthenticationFilter implements Filter {
    @Inject
    JwtSecurity jwtSecurity;


    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if (servletRequest instanceof HttpServletRequest) {
            Boolean validAccessToken = validateAccessToken(servletRequest);
            if (validAccessToken) {
                filterChain.doFilter(servletRequest, servletResponse); // This signals that the request should pass this filter
            } else {
                HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                httpResponse.setStatus(HttpStatus.UNAUTHORIZED_401);
                httpResponse.getWriter().print(HttpResponseMessages.UNAUTHORIZED);
            }
        }
    }

    private Boolean validateAccessToken(ServletRequest servletRequest) {
        boolean validAccessToken = false;
        String accessToken = ((HttpServletRequest) servletRequest).getHeader(HttpKeys.AUTHORIZATION);
        User currentUser = jwtSecurity.getCurrentUser(accessToken);
        if (currentUser != null) validAccessToken = true;
        return validAccessToken;
    }

    public void destroy() {

    }
}
