package com.olacabs.jackhammer.filters;

import com.olacabs.jackhammer.common.Constants;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CORSFilter implements Filter {

    /**
     * Default constructor.
     */
    public CORSFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // Authorize (allow) all domains to consume the content
        ((HttpServletResponse) servletResponse).addHeader(Constants.ACCESS_CONTROL_ALLOW_ORIGIN_KEY, request.getHeader(Constants.ACCESS_CONTROL_ALLOW_ORIGIN_VALUE));
        ((HttpServletResponse) servletResponse).addHeader(Constants.ACCESS_CONTROL_ALLOW_CREDENTIALS_KEY,Constants.ACCESS_CONTROL_ALLOW_CREDENTIALS_VALUE);
        ((HttpServletResponse) servletResponse).setHeader(Constants.ACCESS_CONTROL_ALLOW_HEADERS_KEY,Constants.ACCESS_CONTROL_ALLOW_HEADERS_VALUE);
        ((HttpServletResponse) servletResponse).addHeader(Constants.ACCESS_CONTROL_ALLOW_METHODS_KEY,Constants.ACCESS_CONTROL_ALLOW_METHODS_VALUE);

        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake

        if (request.getMethod().equals(Constants.OPTIONS_HTTP_METHOD)) {
            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }

        // pass the request along the filter chain
        chain.doFilter(request, servletResponse);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }
}
