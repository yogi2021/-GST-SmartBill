
package com.yogii.gstsmartbill.security.jwt;

import com.yogii.gstsmartbill.entity.User;
import com.yogii.gstsmartbill.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Read Authorization header
        String authHeader = request.getHeader("Authorization");

        // 2. Check Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            // 3. Extract claims from JWT
            Claims claims = jwtService.extractAllClaims(token);

            String email = claims.getSubject();

            Number tenantIdValue = claims.get("tenantId", Number.class);

            if (email == null || tenantIdValue == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Long tenantId = tenantIdValue.longValue();

            // 4. Load user from the correct tenant
            User user = userRepository
                    .findByEmailAndTenant_Id(email, tenantId)
                    .orElse(null);

            // 5. Validate token and authenticate user
            if (user != null
                    && jwtService.isTokenValid(token, user)
                    && SecurityContextHolder.getContext()
                    .getAuthentication() == null) {

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name()
                        );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                List.of(authority)
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

        } catch (JwtException | IllegalArgumentException ex) {
            // Invalid, expired or malformed token
            SecurityContextHolder.clearContext();
        }


        filterChain.doFilter(request, response);
    }
}
