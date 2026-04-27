package com.line.business.work.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.line.business.work.service.UserAuthService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserAuthService userDetailsService;

    // ✅ Skip login API
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/auth");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, java.io.IOException {

        try {

            String header = request.getHeader("Authorization");

            // No token → continue request
            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = header.substring(7);
            String username = jwtUtil.extractUsername(token);

            if (username == null) {
                sendInvalid(response, "Invalid Token");
                return;
            }

            // Already authenticated → continue
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            // ❌ Invalid JWT signature/expired
            if (!jwtUtil.isTokenValid(token, userDetails.getUsername())) {
                sendInvalid(response, "Invalid Token");
                return;
            }

            // 🔥 Single session validation
            String dbToken = userDetailsService.getStoredToken(username);

            if (dbToken == null || !dbToken.equals(token)) {
                sendInvalid(response, "Invalid Session");
                return;
            }

            // ✅ VALID → authenticate
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            sendInvalid(response, "Invalid Session");
        }
    }

    // ✅ Custom JSON response sender
    private void sendInvalid(HttpServletResponse response, String message)
            throws java.io.IOException {

        // VERY IMPORTANT — stop Spring from continuing
        SecurityContextHolder.clearContext();

        response.resetBuffer();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = """
            {
              "status":"Failed",
              "code":"401",
              "message":"%s",
              "data":[]
            }
            """.formatted(message);

        response.getWriter().write(json);
        response.flushBuffer(); // stop further processing
    }
}
