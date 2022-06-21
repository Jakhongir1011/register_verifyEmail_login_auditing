package com.example.appjwtreal.service;

import com.example.appjwtreal.entity.Role;
import com.example.appjwtreal.entity.User;
import com.example.appjwtreal.entity.enums.RoleName;
import com.example.appjwtreal.payload.ApiResponse;
import com.example.appjwtreal.payload.LoginDto;
import com.example.appjwtreal.payload.RegisterDto;
import com.example.appjwtreal.repository.RoleRepository;
import com.example.appjwtreal.repository.UserRepository;
import com.example.appjwtreal.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

 @Autowired
 UserRepository userRepository;
 @Autowired
 RoleRepository roleRepository;
 @Autowired
 PasswordEncoder passwordEncoder;
 @Autowired
 JavaMailSender javaMailSender;

 @Autowired
 AuthenticationManager authenticationManager;
 @Autowired
    JwtProvider jwtProvider;
 public ApiResponse registerUserService(RegisterDto registerDto){

        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail){
         return new ApiResponse("Such an email is available",false);
        }
        User user = new User();
        user.setFirstname(registerDto.getFirstname());
        user.setLastname(registerDto.getLastname());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getUsername()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_USER)));
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);

        // EMAILGA XABAR YUBORISH METHODINI YUBORYAPMIZ
     Boolean aBoolean = sendEmail(user.getEmail(), user.getEmailCode());
     System.out.println(aBoolean);

     return new ApiResponse("Muaffaqiyatli ro'yhatdan otdingiz. Accountni activlashtirish uchun tasdiqlang",true);
       }

        public Boolean sendEmail(String sendingEmail, String emailCode){
        try {
         SimpleMailMessage mailMessage = new SimpleMailMessage();
         mailMessage.setFrom("Test@fido.com");
         mailMessage.setTo(sendingEmail);
         mailMessage.setSubject("Accountni tasdiqlang");
         mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail + "'>Tasdiqlang</a>");
         javaMailSender.send(mailMessage);
         return true;

        }catch (Exception e){
         return false;
        }
 }


    public ApiResponse verifyEmailService(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findByEmailCodeAndEmail(emailCode,email);
        System.out.println("emailCode: "+emailCode);
        System.out.println("email: "+email);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Account tasdiqlandi", true);
        }
        return new ApiResponse("Account allaqachom tasdiqlangan", false);
    }



    public ApiResponse loginService(LoginDto loginDto) {
     try{
         Authentication authenticate = authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken
                         (loginDto.getUsername(),
                                 loginDto.getPassword()
                         )
         );

         User users = (User) authenticate.getPrincipal();
         Set<Role> roles = users.getRoles();

         String token = JwtProvider.generateToken(loginDto.getUsername(), roles);

         return new ApiResponse("Success",true, token);
    }catch (Exception e){
         return new ApiResponse("password or username error", false);
     }
 }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> byEmail = userRepository.findByEmail(username);
        if (byEmail.isPresent())
            return byEmail.get();
        throw new UsernameNotFoundException("error user");
    }
}
