package com.bookstore.bookstore_assignment.service;

import com.bookstore.bookstore_assignment.entity.User;
import com.bookstore.bookstore_assignment.repository.UserRepository;
import com.bookstore.bookstore_assignment.security.CustomUserDetails;
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
