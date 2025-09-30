package com.spring_security.Spring_Security_Demo.security;

import com.spring_security.Spring_Security_Demo.entity.Role;
import com.spring_security.Spring_Security_Demo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

import java.util.Collection;


public class CustomUserDetails implements UserDetails {

    private final User user;  // final kar diya taki immutability ho

    // Constructor to inject User
    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 1️⃣ user.getRoles() → User entity se role list le raha hai
        // 2️⃣ stream() → List<Role> pe stream bana raha hai taaki map kar sake
        // 3️⃣ map(...) → Har Role entity ko SimpleGrantedAuthority me convert kar raha hai
        //      SimpleGrantedAuthority → Spring Security ko batata hai ki user kaunse roles/authorities rakhta hai
        // 4️⃣ collect(Collectors.toList()) → stream ko List<SimpleGrantedAuthority> me convert kar raha hai
        // 5️⃣ return → Spring Security authentication aur authorization me ye List use hoti hai
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()
                ))
                .collect(Collectors.toList());

    }

    @Override
    public String getPassword() {
        return user.getPassword();  // user entity se password return karega
    }

    @Override
    public String getUsername() {
        return user.getEmail();  // yaha aap email ko username ke liye use kar rahe ho
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // agar expiry ka logic nahi hai to true rakho
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // agar lock/unlock ka feature nahi hai to true rakho
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // agar password expiry nahi hai to true rakho
    }

    //to convert list<roles> to string
    //Frontend friendly → JSON me sirf role names chahiye.
        public List<String> getRoleAsString(){
            // Agar user ke roles DB me:
    //[ Role(roleName="ROLE_USER"), Role(roleName="ROLE_ADMIN") ]
    // Ye method return karega:
    //[ "ROLE_USER", "ROLE_ADMIN" ]


                return user.getRoles().stream()       // 1️⃣ User entity se roles ki List<Role> le raha hai
                        .map(Role::getRoleName)   // 2️⃣ Har Role object se roleName string nikal raha hai
                        .toList();                // 3️⃣ Stream ko List<String> me convert kar raha hai
            }

    public Long getUserId() {
        return user.getUserId(); // Lombok ke getter se milega
    }



    @Override
    public boolean isEnabled() {
        return true; // agar account enable/disable ka feature nahi hai to true rakho
    }


}
