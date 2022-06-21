package com.example.appjwtreal.security;

import com.example.appjwtreal.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {

    static final String secret = "letNoOneKnowTheSecretWork1011";
    static final long expireTime = 36_000_000;
    /**
     *
     * @param username username orqali token yasaymiz. <br>
     * Username cleant tomonidan keladi cleantni username
     * @return tokenni qaytaramiz
     */
    static public String generateToken(String username, Set<Role> roles
    ){
        Date expireDate = new Date(System.currentTimeMillis() + expireTime);
        String compact = Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("roles",roles)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return compact;
    }



    /**
     *
     * @param token KELADI JwtFilterdan yuboramiz
     * @return TOKENNI VALEDATSIYADAN OTQAZDIK (TOKEN BUZULMAGANLIGINI, MUDDAT OTMAGANLIGINI NA H.K) true or folse qaytadi
     */

     public boolean validateToken(String token){
         try{
             Jwts
                     .parser()
                     .setSigningKey(secret)
                     .parseClaimsJws(token);
             return true;
         }catch (Exception e){
          e.printStackTrace();
         }
         return false;
     }



    /**
     *
     * @param token keladi JwtFilterdan yuboramiz
     * @return Token ichidan username olamiz va username qaytadi
     */

    public String getEmailNameFromToken(String token){
        try{
            String email = Jwts
                    .parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return email;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }



}
