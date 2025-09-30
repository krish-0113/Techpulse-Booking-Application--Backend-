package com.spring_security.Spring_Security_Demo.service;

import com.spring_security.Spring_Security_Demo.entity.User;
import com.spring_security.Spring_Security_Demo.repository.UserRepository;
import com.spring_security.Spring_Security_Demo.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        // Yahan manually CustomUserDetails return karte hain
        return new CustomUserDetails(user);
    }
}
