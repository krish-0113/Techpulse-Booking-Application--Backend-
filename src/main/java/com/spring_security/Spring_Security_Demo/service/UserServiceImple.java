package com.spring_security.Spring_Security_Demo.service;

import com.spring_security.Spring_Security_Demo.dtos.AuthLoginResponse;
import com.spring_security.Spring_Security_Demo.dtos.RegisterRequest;
import com.spring_security.Spring_Security_Demo.dtos.RegisterResponse;
import com.spring_security.Spring_Security_Demo.entity.Role;
import com.spring_security.Spring_Security_Demo.entity.User;
import com.spring_security.Spring_Security_Demo.exceptions.UserAlreadyExitsException;
import com.spring_security.Spring_Security_Demo.jwt.JwtUtils;
import com.spring_security.Spring_Security_Demo.payload.ApiResponseSuccess;
import com.spring_security.Spring_Security_Demo.repository.RoleRepository;
import com.spring_security.Spring_Security_Demo.repository.UserRepository;
import com.spring_security.Spring_Security_Demo.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImple implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public RegisterResponse registerUser(RegisterRequest request) {
        // 1️⃣ Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExitsException("User already exists! Please register with another email.");
        }

        // 2️⃣ Create new User object
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encode password

        // 3️⃣ Assign roles
        List<Role> rolesToAssign;
        if(request.getRoles() == null || request.getRoles().isEmpty()){
            Role defaultRole = roleRepository.findByRoleName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Default Role not found"));
            rolesToAssign = List.of(defaultRole);
        } else {
            rolesToAssign = roleRepository.findAllByRoleNameIn(request.getRoles());
            if(rolesToAssign.isEmpty()) {
                throw new RuntimeException("Invalid roles provided");
            }
        }
        user.setRoles(rolesToAssign);

        // 4️⃣ Save in DB
        User savedUser = userRepository.save(user);

        // 5️⃣ Map to RegisterResponse DTO
        List<String> roleNames = savedUser.getRoles().stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());

        return new RegisterResponse(savedUser.getEmail(), roleNames, "User registered successfully!");
    }


    @Override
    public AuthLoginResponse loginUser(String email, String password) {

        // 🔹 Approach 1: Without Spring Security + Without BCrypt (Not recommended in real projects)
        // Fetch user by email
//        User existingUser = userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User Not Found with email: " + email));

        // Compare plain-text password (❌ BAD PRACTICE - never store plain passwords)
        // if (!password.equals(existingUser.getPassword())) {
        //     throw new InvalidCredentialsException("Invalid Password");
        // }
        // return "Login Successful without Security";  // just for testing

        // 🔹 Approach 2: Without Spring Security but with BCrypt (manual password validation)
        // Compare raw password with hashed password using PasswordEncoder
        // if (!passwordEncoder.matches(password, existingUser.getPassword())) {
        //     throw new InvalidCredentialsException("Invalid Password");
        // }
        // return "Login Successful with BCrypt Hashing";  // works but not best practice

        // 🔹 Approach 3: With Spring Security + BCrypt + AuthenticationManager (✅ Best Practice)
        // This approach uses Spring Security’s AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // Extract logged-in user details from Authentication object
        // ✅ Get UserDetails and cast to CustomUserDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        CustomUserDetails customUser = (CustomUserDetails) userDetails;

        //genrate token
        String jwtToken = jwtUtils.generateToken(customUser);
     // Get role from UserDetails (actual DB role)
        // ✅ Extract roles from authorities
        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());
        // // Wrap JWT + username + role in AuthLoginResponse DTO
        AuthLoginResponse authLoginResponse = new AuthLoginResponse(
                jwtToken,
                userDetails.getUsername(),
               roles
        );

        // Generate JWT Token based on authenticated user
        return new ApiResponseSuccess<AuthLoginResponse>("Login Successfully!!", authLoginResponse).getData();



    }

}
