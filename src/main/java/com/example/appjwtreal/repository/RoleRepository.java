package com.example.appjwtreal.repository;

import com.example.appjwtreal.entity.Role;
import com.example.appjwtreal.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRoleName(RoleName roleName);

}
