package com.bluecow.service;

import com.bluecow.entity.Role;
import com.bluecow.enums.roleName;
import com.bluecow.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    public Optional<Role> getByNameRole(roleName roleName){
        return roleRepository.findByRoleName(roleName);
    }

    public boolean existsNameRole(roleName roleName){
        return roleRepository.existsByRoleName(roleName);
    }

    public void save(Role role){
        roleRepository.save(role);
    }
}
