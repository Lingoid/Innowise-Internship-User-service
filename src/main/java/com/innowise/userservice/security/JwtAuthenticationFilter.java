package com.innowise.userservice.security;

import com.innowise.userservice.util.SecretWordAccessDeniedException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate;
    private final String authServiceUrl;
    private final String secretWord;

    public JwtAuthenticationFilter(RestTemplate restTemplate, String authServiceUrl, String secretWord) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
        this.secretWord = secretWord;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/users") && request.getMethod().equals("POST")) {
            String secretHeader = request.getHeader("secret");
            if (secretHeader == null || !secretHeader.equals(secretWord)) {
                throw new SecretWordAccessDeniedException();
            }
        }

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwt);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Void> authResponse = restTemplate.exchange(
                    authServiceUrl,
                    HttpMethod.POST,
                    entity,
                    Void.class
            );

            if (authResponse.getStatusCode().is2xxSuccessful()) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(jwt, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
