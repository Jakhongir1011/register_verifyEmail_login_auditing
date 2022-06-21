package com.example.appjwtreal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User implements UserDetails {

    // SPRINGDAGI ASOSIY USER SIFATIDA KORISHI UCHUN UserDetails implements OLAMIZ
    // QAYSI CLASS IMPLEMENTS OLSA USERDETAILSDAN U SPRINGDA SISTEMAGA KIRUVCHI KARENT PRINSIPLE
    // BOLISH IMKONINI BERADI.

    @Id
    @GeneratedValue
    private UUID id; // USERNING TAKRORLANMAS QISMI

    @Column(nullable = false, length = 50)
    private String firstname;

    @Column(nullable = false, length = 50)
    private String lastname;

    @Column(nullable = false)
    private String password;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createAt;

    @UpdateTimestamp
    private Timestamp updateAt;

    @ManyToMany(fetch = FetchType.EAGER)// shunday qilishimiz kk unda jpa xatolik beradi googledan topildi
    private Set<Role> roles;

    boolean accountNonExpired = true;
    boolean accountNonLocked = true;
    boolean credentialsNonExpired = true;
    boolean enabled=true;

    // ----------------- THIS UserDetails METHODS -------------------------------//

    // BU USERNING XUQUQLAR ROYXATLARI
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    // BU USERNING USERNAMENI QAYTARUVCHI METHOD
    @Override
    public String getUsername() {
        return this.email;
    }

    // ACCOUNTNING AMMAL QILISH MUDDATINI QAYTARADI
    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    // ACCOUNT BLOCKLANGANLIK XOLATINI QAYTARADI
    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    // ACCOUNTNING ISHONCHLILIK MUDDATI TUGAGAN YOKI TUGAMAGANLIGINI QAYTARADI
    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    // ACCOUNTNING YONIQ YOKI O'CHIQLIGINI QAYTRADI
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    private String emailCode; // emailCode
}
