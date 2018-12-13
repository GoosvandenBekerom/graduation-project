//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.inthergroup.webpoc.framework.config.Constants;
import com.inthergroup.webpoc.framework.security.user.User;
import com.inthergroup.webpoc.framework.security.user.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author gvandenbekerom
 * @since 16-Nov-18
 *
 * Used as authentication success handler, Generates jwt token on success and adds this token as AUTHORIZATION header to response
 */
@Service
public class JwtService implements AuthenticationSuccessHandler {
    private static final Algorithm algorithm = Algorithm.HMAC512(Constants.TOKEN_SECRET);

    private final UserDAO userDAO;

    @Autowired
    public JwtService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        String username = auth.getName();
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Username not found"));

        String token = JWT.create()
                .withSubject(username)
                .withClaim("group", user.getGroup().getId())
                .withClaim("lan", user.getLanguageId())
                .withClaim("name", user.getLastName() + ", " + user.getName())
                // todo: find claims needed in front and and add them
                .sign(algorithm);

        response.addHeader(HttpHeaders.AUTHORIZATION, String.format("%s %s", Constants.AUTHORIZATION_PREFIX, token));
    }
}