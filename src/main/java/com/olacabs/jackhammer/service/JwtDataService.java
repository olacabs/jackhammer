package com.olacabs.jackhammer.service;

import java.util.Date;
import java.util.Set;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.olacabs.jackhammer.models.PagedResponse;
import org.apache.commons.lang3.StringUtils;


import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.olacabs.jackhammer.common.Constants;
import com.olacabs.jackhammer.configuration.JackhammerConfiguration;
import com.olacabs.jackhammer.db.JwtTokenDAO;
import com.olacabs.jackhammer.models.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import com.olacabs.jackhammer.models.User;


@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class JwtDataService extends AbstractDataService<JwtToken> {

    @Inject
    @Named(Constants.JWT_TOKEN_DAO)
    private JwtTokenDAO jwtTokenDAO;

    @Inject
    private JackhammerConfiguration jackhammerConfiguration;

    @Override
    public PagedResponse<JwtToken> getAllRecords(JwtToken jwtToken) {
        return paginationRecords;
    }

    @Override
    public JwtToken fetchRecordByname(JwtToken jwtToken) {
        return null;
    }

    @Override
    public  JwtToken fetchRecordById(long id) {
        return jwtTokenDAO.get(id);
    }


    @Override
    public JwtToken createRecord(JwtToken jwtToken) {
        User user = userDAOJdbi.get(jwtToken.getUserId());
        LocalDateTime currentTime = LocalDateTime.now();
        Set<String> currentUserRoles = roleDAOJdbi.getCurrentUserRoles(user.getId());
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put(Constants.JWT_USER_ID, user.getId());
        claims.put(Constants.JWT_SCOPE, StringUtils.join(currentUserRoles,Constants.JWT_SCOPE_SEPARATOR));
        int expiryDuration = jackhammerConfiguration.getJwtConfiguration().getTokenExpirationTime();
        String token = Jwts.builder()
                .setIssuer(jackhammerConfiguration.getJwtConfiguration().getTokenIssuer())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime.plusMinutes(expiryDuration).atZone(ZoneId.systemDefault()).toInstant()))
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jackhammerConfiguration.getJwtConfiguration().getTokenSigningKey())
                .compact();
        jwtToken.setTokenValidFrom(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()));
        jwtToken.setTokenValidUntil(Date.from(currentTime.plusMinutes(expiryDuration).atZone(ZoneId.systemDefault()).toInstant()));
        jwtToken.setUserToken(token);
//        long jwtId = jwtTokenDAO.insert(jwtToken);
//        JwtToken insertedJwt =  jwtTokenDAO.get(jwtId);
//        insertedJwt.setUserToken(token);
        return jwtToken;
    }

    @Override
    public void updateRecord(JwtToken jwtToken) {
//            jwtTokenDAO.delete(jwtToken.getUserId());
    }

    @Override
    public void deleteRecord(long id){

    }
}
