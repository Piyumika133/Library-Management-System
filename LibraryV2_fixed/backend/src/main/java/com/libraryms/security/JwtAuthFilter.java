package com.libraryms.security;
import jakarta.servlet.FilterChain; import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest; import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    public JwtAuthFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils=jwtUtils; this.userDetailsService=userDetailsService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(req);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String email = jwtUtils.getEmailFromJwtToken(jwt);
                UserDetails ud = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) { logger.error("JWT auth error: "+e.getMessage()); }
        chain.doFilter(req, res);
    }
    private String parseJwt(HttpServletRequest req) {
        String h = req.getHeader("Authorization");
        return (StringUtils.hasText(h) && h.startsWith("Bearer ")) ? h.substring(7) : null;
    }
}
