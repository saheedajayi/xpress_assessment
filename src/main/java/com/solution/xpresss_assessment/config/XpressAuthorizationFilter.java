package com.solution.xpresss_assessment.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.solution.xpresss_assessment.Utilities.Constants.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@AllArgsConstructor
public class XpressAuthorizationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailService;
    private final JwtService jwtUtils;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader(AUTHORIZATION);
        if (!StringUtils.hasText(header) ||
                !StringUtils.startsWithIgnoreCase(header, BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (StringUtils.hasText(header) &&
                StringUtils.startsWithIgnoreCase(header, BEARER)) {
            final String token = header.substring(BEARER.length());
            if (jwtUtils.isValid(token)) {
                final String username = jwtUtils.extractUsernameFromToken(token);

                if (username != null) {
                    UserDetails userDetails = userDetailService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(
                            response.getOutputStream(),
                            "Authentication failed"
                    );
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
