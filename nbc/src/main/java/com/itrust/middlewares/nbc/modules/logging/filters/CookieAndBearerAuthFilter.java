package com.itrust.middlewares.nbc.modules.logging.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * This filter checks for Bearer token in the Authorization header or JWT in httpOnly cookie.
 * If either is present and valid, it sets the authentication in the SecurityContext.
 * You should replace the JWT validation logic with your own implementation.
 */
@Component
public class CookieAndBearerAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CookieAndBearerAuthFilter.class);

    private final String jwtCookieName ="access_token";

    private final JwtDecoder jwtDecoder;

    public CookieAndBearerAuthFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
        logger.info("Initialized CookieAndBearerAuthFilter with default jwtCookieName: {}", jwtCookieName);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("Processing request for path: {}", request.getRequestURI());
        logger.debug("Request Origin: {}", request.getHeader("Origin"));
        String jwt = resolveToken(request);
        if (StringUtils.hasText(jwt)) {
            if (jwtDecoder == null) {
                logger.error("JwtDecoder is null, cannot decode JWT");
                SecurityContextHolder.clearContext();
            } else {
                try {
                    logger.debug("Attempting to decode JWT");
                    Jwt decodedJwt = jwtDecoder.decode(jwt);
                    logger.debug("Decoded JWT claims: {}", decodedJwt.getClaims());
                    UserDetails userDetails = User.withUsername(decodedJwt.getSubject())
                            .password("")
                            .authorities(Collections.emptyList()) // Optionally map roles/authorities
                            .build();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Authentication set: {}", SecurityContextHolder.getContext().getAuthentication());
                    // Forward the token to downstream services as Bearer token

                } catch (JwtException e) {
                    logger.error("JWT validation failed: {}", e.getMessage(), e);
                    SecurityContextHolder.clearContext();
                }
            }
        } else {
            logger.debug("No JWT found in request");
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        // 1. Check Authorization header
        logger.debug("Checking Authorization header for Bearer token");
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            logger.debug("Found Bearer token in Authorization header");
            return bearerToken.substring(7);
        }
        logger.debug("No Bearer token found in Authorization header");

        // 2. Check cookies for JWT
        logger.debug("jwtCookieName: {}", jwtCookieName);
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (jwtCookieName.equals(cookie.getName())) {
                    logger.debug("Found JWT in cookie: {}", jwtCookieName);
                    return cookie.getValue();
                }
            }
            logger.debug("No matching JWT cookie found for name: {}", jwtCookieName);
        } else {
            logger.debug("No cookies found in request");
        }
        return null;
    }
}