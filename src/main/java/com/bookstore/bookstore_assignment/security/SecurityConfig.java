package com.bookstore.bookstore_assignment.security;

import com.bookstore.bookstore_assignment.jwt.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 👈 For using @PreAuthorize
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private UserDetailsService userDetailsService; // load user from DB

      @Autowired
      private BCryptPasswordEncoder passwordEncoder;

    // ✅ Authentication Provider for Spring Security
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    // ✅ AuthenticationManager Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // ✅ Security Filter Chain (Main Security Config)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for REST APIs
                .authorizeHttpRequests(auth -> auth
                        // 🔓 Public endpoints
                        .requestMatchers(
                                "/auth/user/register",
                                "/auth/user/login",
                                "/auth/public/**"
                        ).permitAll()

                        // 👑 Admin endpoints — can add, update, delete books/authors, view all orders
                        .requestMatchers("/auth/admin/**", "/admin/**").hasRole("ADMIN")

                        // 🧑‍💼 Manager endpoints — can view books, update stock, and view orders
                        .requestMatchers("/auth/manager/**", "/manager/**").hasAnyRole("MANAGER", "ADMIN")

                        // 👤 Customer/User endpoints — can browse, place order, view their orders
                        .requestMatchers("/auth/user/**", "/orders/**").hasAnyRole("USER", "ADMIN", "MANAGER")

                        // 📚 Publicly visible books
                        .requestMatchers("/books/**").permitAll()

                        // 🔒 Any other endpoint requires authentication
                        .anyRequest().authenticated()
                )

                // ✅ Custom authentication provider
                .authenticationProvider(authenticationProvider())

                // ✅ JWT filter (before UsernamePasswordAuthenticationFilter)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // ✅ Disable form login & HTTP Basic (for API-based login)
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
