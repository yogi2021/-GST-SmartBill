
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
import org.springframework.security.core.context.SecurityContext;
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

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {

            Claims claims = jwtService.extractAllClaims(token);

            String email = claims.getSubject();

            Number tenantIdValue =
                    claims.get("tenantId", Number.class);

            if (email == null || tenantIdValue == null) {
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Invalid JWT claims"
                );
                return;
            }

            Long tenantId = tenantIdValue.longValue();

            User user = userRepository
                    .findByEmailAndTenant_Id(email, tenantId)
                    .orElse(null);

            if (user == null || !jwtService.isTokenValid(token, user)) {
                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Invalid or expired token"
                );
                return;
            }

            if (SecurityContextHolder.getContext()
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

                SecurityContext context =
                        SecurityContextHolder.createEmptyContext();

                context.setAuthentication(authentication);

                SecurityContextHolder.setContext(context);
            }

        } catch (JwtException | IllegalArgumentException ex) {

            SecurityContextHolder.clearContext();

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid or expired JWT"
            );
            return;
        }

        filterChain.doFilter(request, response);


    }

}
