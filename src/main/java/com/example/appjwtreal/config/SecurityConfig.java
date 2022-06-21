package com.example.appjwtreal.config;

import com.example.appjwtreal.security.JwtFilter;
import com.example.appjwtreal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Properties;
/**
 *@EnableWebSecurity bu bizni classimizni security ekanligini aytadi va shuni ichida sozlamalar yozamiz <br>
 * @Configuration bu bean qilib berai va class ichida bean yaratsa boladi
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter { // WebSecurityConfigurerAdapter bu ctrl+o ishlatishimizga va methodlarni ovrride qilishimizga kk

    @Autowired
    UserService userService;
    @Autowired
    JwtFilter jwtFilter;
    /**
     * Bu userlarni sistemaga kiritish uchun(ma'lumot myAuthService dan keladi)
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    /**
     * bu parol va userlani solishtiradi
     */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * Yollarga ruhsat berish
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /**
                 * GET DAN BOSHQASIGA HAM RUHSAT BER DEGANI
                 */
                .csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/register","/api/auth/verifyEmail","/api/auth/login").permitAll()
                .anyRequest().authenticated();
        /**
         * Biz aytyapmizki httpga JwtFilter ishlasin <br>
         * (hali parol loginlani solishtirmasda sistemaga kirmasdan) <br>
         * UsernamePasswordAuthenticationFilter.class dan oldin dedik
         */
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        /**
         * SPRING SECURITYGA SESSIONGA USHLAB OLMASLIGINI BUYIRYAPMIZ
         */
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    // FOR ENCODER IN PASSWORD
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


   // FOR EMAIL
    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("jakhongir27231@gmail.com");
        mailSender.setPassword("Jakhongir1011");
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol","smtp");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.debug","true");
        return mailSender;
    }


}
