package com.example.appjwtreal.repository;

import com.example.appjwtreal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.Email;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(@Email String email);
    Optional<User> findByEmail(@Email String email);
    Optional<User> findByEmailCodeAndEmail(String emailCode, @Email String email);
}
