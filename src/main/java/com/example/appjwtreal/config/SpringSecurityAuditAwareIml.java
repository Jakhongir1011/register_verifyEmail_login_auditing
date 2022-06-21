package com.example.appjwtreal.config;

import com.example.appjwtreal.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

// bu kim yozganini bilish
public class SpringSecurityAuditAwareIml implements AuditorAware<UUID> { // USERNI ID SI UUID BOLGANI UCHUN
    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null  // sistemaga kimdur kirgan bolsin
                && authentication.isAuthenticated() // kirib turgan bolsin kimdur
                && !authentication.getPrincipal().equals("anonymousUser")){ // anonymousUser bolmasin
            User user = (User) authentication.getPrincipal();
            return Optional.of(user.getId()); // osha user ni id sini qaytarib yuboramiz
        }
        return Optional.empty();
    }
}
