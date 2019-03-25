package com.tng.portal.ana.util;

import com.tng.portal.ana.bean.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by Zero on 2016/11/14.
 */
public class JWTTokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(JWTTokenUtil.class);

    private static final String KEY = "eyJhbGciOiJIUzI1NiJ9";

    private JWTTokenUtil(){}

    public static String generateToken(String userAccount,String password,String id) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Date now = new Date();
        Claims claims = Jwts.claims().setIssuer(userAccount).setSubject(password).setIssuedAt(now).setId(id);
        return Jwts.builder().setClaims(claims).signWith(signatureAlgorithm, KEY).compact();
    }

    public static String getUserAccount(String token){
        try {
            Claims body = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
            return body.getIssuer();
        }catch(Exception e){
            return null;
        }
    }
    
    public static String getPassword(String token){
        try {
            Claims body = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
            return body.getSubject();
        }catch(Exception e){
            return null;
        }
    }

    public static Date getIssuedAt(String token){
        try {
            Claims body = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
            return body.getIssuedAt();
        }catch(Exception e){
            return null;
        }
    }

    public static String getSubject(String token){
        try {
            Claims body = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
            return body.getSubject();
        }catch(Exception e){
            return null;
        }
    }

    public static String generateToken(String account, TokenType tokenType, long nowMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Date now = new Date(nowMillis);
        Claims claims = Jwts.claims().setSubject(account).setIssuer(tokenType.name()).setExpiration(now);
        return Jwts.builder().setClaims(claims).signWith(signatureAlgorithm, KEY).compact();
    }

    public static String getAccount(String token){
        try {
            Claims body = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
            return body.getSubject();
        }catch(Exception e){
            logger.error("Exception",e);
            return null;
        }
    }

    public static TokenType getTokenType(String token){
        try {
            Claims body = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
            String tokenType = body.getIssuer();
            if(null!=tokenType){
                return TokenType.valueOf(tokenType);
            }else {
                return null;
            }
        }catch(Exception e){
            logger.error("Exception",e);
            return null;
        }
    }
    
    public static String getId(String token){
    	try {
            Claims body = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
            return body.getId();
        }catch(Exception e){
            logger.error("Exception",e);
            return null;
        }
    }

    public static void main(String[] args) {
        getAccount("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyODEyODc2MDNAcXEuY29tIiwiaXNzIjoiVVNFUl9UT0tFTiIsImV4cCI6MTQ4NDU0OTMwMH0.87RZdEgSbbBmkUJbv3OSTuYpNDYr-Fa7TEmA_bMo8ak");
    }


}
