package com.spring_security.Spring_Security_Demo.security;
import com.spring_security.Spring_Security_Demo.jwt.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity   // 👈 PreAuthorize ke liye zaroori hai
    public class SecurityConfig {
        @Autowired
        private JwtAuthFilter jwtAuthFilter;
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws  Exception{
            return  authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable()) // disable CSRF for testing
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/auth/user/register", "/auth/user/login").permitAll() // ✅ register & login sabke liye
                            .requestMatchers("/auth/user/**").hasAnyRole("USER","ADMIN") // ✅ ab register & login dono allowed
                            .requestMatchers("/auth/admin/**").hasRole("ADMIN") // ✅ ab register & login dono allowed
                            .requestMatchers("/auth/public/**").permitAll() // ✅ ab register & login dono allowed
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtAuthFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class) // 👈 yaha add karo
                    .formLogin(form -> form.disable())   // disable default login form
                    .httpBasic(basic -> basic.disable()); // disable basic auth

            return http.build();
        }
    }

