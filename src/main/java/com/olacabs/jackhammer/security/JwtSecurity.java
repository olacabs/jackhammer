package com.olacabs.jackhammer.security;

import com.google.inject.Inject;
import com.olacabs.jackhammer.common.HttpKeys;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.enums.Handler;
import com.olacabs.jackhammer.exceptions.AbstractException;
import com.olacabs.jackhammer.models.User;
import com.olacabs.jackhammer.service.factories.DataServiceBuilderFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class JwtSecurity {
    @Inject
    private JackhammerConfiguration jackhammerConfiguration;

    @Inject
    DataServiceBuilderFactory dataServiceBuilderFactory;

    public User authenticateUserToken(String token) {
        User user = null;
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey())
                    .parseClaimsJws(token)
                    .getBody();
            user = new User();
            user.setEmail(body.getSubject());
        } catch (JwtException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getCurrentUser(String accessToken) {
        User user = null;
        user = authenticateUserToken(accessToken);
        try {
            user = (User) dataServiceBuilderFactory.getService(Handler.USER_SERVICE).fetchRecordByname(user);
        } catch (AbstractException ae) {
            log.error("Error while finding user", ae);
        }
        return user;
    }
}
