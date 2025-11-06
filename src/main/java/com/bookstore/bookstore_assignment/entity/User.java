package com.bookstore.bookstore_assignment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Name should be required")
    private String name;

    @NotBlank(message = "Email should be required")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password should be required")
    private String password;

    //for roles
    @JoinTable(
            name = "user_roles",  // 🔹 join table ka name
            joinColumns = @JoinColumn(name = "userId"), // 🔹 owning entity (User) ka FK
            inverseJoinColumns = @JoinColumn(name = "roleId") // 🔹 target entity (Role) ka FK
    )
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
}
