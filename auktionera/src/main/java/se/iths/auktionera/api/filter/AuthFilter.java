package se.iths.auktionera.api.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

@Component
public class AuthFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Principal userPrincipal = httpServletRequest.getUserPrincipal();
        if (userPrincipal == null) {
            String mockUser = "TestUser6";
            httpServletRequest.setAttribute("authId", mockUser);
            log.info("Mock user: {}", mockUser);
        } else {
            httpServletRequest.setAttribute("authId", userPrincipal.getName());
            log.info("User: {}", userPrincipal.getName());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
