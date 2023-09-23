package com.developersworld.restapi.config;

import com.developersworld.restapi.helper.JWTService;
import com.developersworld.restapi.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JWTService jwtHelper;

    @Autowired
    CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal
            (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwtRequestTokenHeader = request.getHeader("Authorization");
        String userName = null;
        String bearerToken = null;
        if(jwtRequestTokenHeader != null && jwtRequestTokenHeader.startsWith("Bearer")) {
            bearerToken = jwtRequestTokenHeader.substring(7);
            try {
                userName = jwtHelper.extractUsername(bearerToken);
            } catch (Exception e) {
                throw new UsernameNotFoundException(e.getMessage());
            }
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtHelper.validateToken(bearerToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                            UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } else {
                System.out.println("user is not validated");
            }
        }
        filterChain.doFilter(request,response);
    }

}
