package se.iths.auktionera.api.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

@Component
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Principal userPrincipal = httpServletRequest.getUserPrincipal();
        if (userPrincipal == null) {
            httpServletRequest.setAttribute("authId", "TestUser");
        } else {
            httpServletRequest.setAttribute("authId", userPrincipal.getName());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
