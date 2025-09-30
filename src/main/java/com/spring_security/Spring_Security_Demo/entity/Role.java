package com.spring_security.Spring_Security_Demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roleId;
    private String roleName;   // ROLE_USER, ROLE_ADMIN, etc.

    @ManyToMany(mappedBy = "roles")
    private List<User> user;
}
