/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.filter;

import erp.bean.SessionBean;
import java.io.IOException;
import javax.inject.Inject;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * This Java filter demonstrates how to intercept the request and transform the
 * response to implement authentication feature for the website's front-end.
 *
 * @author peukianm
 */
@WebFilter("/*")
public class FrontEndAuthenticationFilter implements Filter {

    @Inject
    private SessionBean sessionBean;
    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;;
    private final static String FILTER_APPLIED = "_security_filter_applied";

    private static final String[] nonRequiredLogin = {
        "resource", "login", "error","index", "resetPassword"
    };

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        httpRequest = (HttpServletRequest) request;
        httpResponse = (HttpServletResponse) response;
        String servletPath = httpRequest.getServletPath();
        HttpSession session = httpRequest.getSession(false);

        if (request.getAttribute(FILTER_APPLIED) == null && !notRequiredLogin(servletPath) ) {
            request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
            boolean isLoggedIn = (session != null && sessionBean != null && sessionBean.getUsers() != null);
            if (!isLoggedIn) {
                if ("partial/ajax".equals(httpRequest.getHeader("Faces-Request"))) {                   
                    httpResponse.setContentType("text/xml");
                    httpResponse.getWriter().append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").printf("<partial-response><redirect url=\"%s\"></redirect></partial-response>", "/index.jsp?faces-redirect=true");                           	            	
                } else {                    
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!NORMAL CALL--->REDIRECTING fROM SECURITY FILTER " + servletPath);
                    httpResponse.sendRedirect("/index.jsp?faces-redirect=true");
                }
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean notRequiredLogin(String path) {
        for (String nonloginRequiredURL : nonRequiredLogin) {
            if (path.contains(nonloginRequiredURL)) {
                return true;
            }
        }
        return false;
    }

    public void destroy() {}
    public void init(FilterConfig fConfig) throws ServletException {}

}
