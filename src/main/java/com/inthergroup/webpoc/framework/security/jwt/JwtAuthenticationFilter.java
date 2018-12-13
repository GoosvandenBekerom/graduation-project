//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.security.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author gvandenbekerom
 * @since 16-Nov-18
 *
 * Exposes /login endpoint and links the configured authenticationManager to it
 * Also sets jwt service as success handler
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public JwtAuthenticationFilter(AuthenticationManager authManager, JwtService jwtService) {
        setAuthenticationManager(authManager);
        if (jwtService != null) {
            setAuthenticationSuccessHandler(jwtService);
        }
    }
}