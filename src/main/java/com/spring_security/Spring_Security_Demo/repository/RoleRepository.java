package com.spring_security.Spring_Security_Demo.repository;

import com.spring_security.Spring_Security_Demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // ✅ Find single role by its name
    public Optional<Role> findByRoleName(String name);

    // ✅ Find all roles whose name is in the provided list of role names
    public List<Role> findAllByRoleNameIn(List<String> roleNames);
}
