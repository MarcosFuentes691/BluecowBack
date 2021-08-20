package com.bluecow.repository;

import com.bluecow.entity.Role;
import com.bluecow.consts.roleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(roleName roleName);
    boolean existsByRoleName(roleName roleName);
}
