package com.example.FOT_Event_Managment_System.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // Get the roles of the logged-in user
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/dashboard");
        } else if (roles.contains("ROLE_ORGANIZER")) {
            response.sendRedirect("/events");
        } else if(roles.contains("ROLE_PARTICIPANT")) {
            response.sendRedirect("/participant/");
        }
        else {
            response.sendRedirect("/"); // Default fallback
        }
    }
}