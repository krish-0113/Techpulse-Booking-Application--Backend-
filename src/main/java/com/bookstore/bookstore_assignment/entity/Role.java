package com.bookstore.bookstore_assignment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roleId;
    @Column(nullable = false, unique = true)
    private String roleName;   // ROLE_USER, ROLE_ADMIN, etc.

    @ManyToMany(mappedBy = "roles")
    private Set<User> user;
}
