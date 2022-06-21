package com.example.appjwtreal.security;

import com.example.appjwtreal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JwtFilter: har bir request controllirga borishdan oldin <br>
 * JwtFilterga kelsin HEADERIDAN TOKENNI OLIB TEKSHIRAMIZ  <br>
 *  VA VALIDETIONDAN OTSA SYSYIMAGA KIRSIN DEYMIZ
 */
// har bitta requestni filter qilishimiz uchun OncePreRequestFilter dan extance olish kk

    @Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // REQUESTDAN TOKEN OLISH
        String token = request.getHeader("Authorization");

        // TOKEN BORLIGINI VA BEARER BO'LISHINI TEKSHIRADI
        if (token != null && token.startsWith("Bearer")){
            // AYNAN TOKEN O'ZINI OLDI
             token = token.substring(7);

            // TOKENNI VALEDATSIYADAN OTQAZDIK (TOKEN BUZULMAGANLIGINI, MUDDAT OTMAGANLIGINI NA H.K)
            boolean validateToken = jwtProvider.validateToken(token);
             if (validateToken){
                 /**
                  *  Endi userni systemaga kiritishimiz kk. <br>
                  *  Biz hozir tokinni tekshirdik. <br>
                  *  Endi tokendan userni kimligini bilishimiz kk<br>
                  */
                 String email = jwtProvider.getEmailNameFromToken(token);
                 if (email != null){
                     // USER_DETAILS ORQALI AUTHENTICATION YARATIB OLAMIZ
                     UserDetails userDetails = userService.loadUserByUsername(email);

                     /**
                      * userDetails nimaga kerak bizga kerak boladihan asosiy narsasi <br>
                      * shu token bilan kelgan user kirdi deyishimiz kk <br>
                      * Authentication tipiddagi object yaratib beradi
                      */

                     UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                             new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                     /**
                      * 3 tanlikdan foydalansak systimaga kirdi derkan <br>
                      * 2 talikdan foydalansak parol loginlarni solishtirarkan
                      */
                     System.out.println(SecurityContextHolder.getContext().getAuthentication());

                     /**
                      * bu sistemaga kirdi deb elon qiladi kimligimizni aytadi secutityga
                      */
                     // SISTEMAGA KIM KIRGANLIGINI ORNATDIK
                     SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                     System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                 }
             }
        }
       // BOSHQA FILTELA ISHLATADI OZINI FILTIRINI QIL OZIMIZNIKINI ISHLATIB BOLDIK DEDIK
        filterChain.doFilter(request,response);
    }
}
