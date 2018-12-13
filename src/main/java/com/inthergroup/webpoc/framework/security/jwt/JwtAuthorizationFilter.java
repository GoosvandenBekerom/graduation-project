//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.inthergroup.webpoc.framework.config.Constants;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author gvandenbekerom
 * @since 16-Nov-18
 *
 * Checks if authorized requests contain a valid authorization token
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(AuthenticationManager authManager, UserDetailsService userDetailsService) {
        super(authManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(authHeader)) {
            super.doFilterInternal(request, response, chain);
            return;
        }

        String token = authHeader.substring(Constants.AUTHORIZATION_PREFIX.length()).trim();

        if (StringUtils.isEmpty(token)) {
            throw new BadCredentialsException("Received authorization header was empty");
        }

        try {
            DecodedJWT decoded = JWT.decode(token);
            UserDetails details = userDetailsService.loadUserByUsername(decoded.getSubject());
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(details.getUsername(), null, details.getAuthorities())
            );
        } catch (Exception e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            throw new BadCredentialsException("Failed to decode authorization token");
        }
        chain.doFilter(request, response);
    }
}
