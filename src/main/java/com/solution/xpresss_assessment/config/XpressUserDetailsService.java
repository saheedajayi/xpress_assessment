package com.solution.xpresss_assessment.config;

import com.solution.xpresss_assessment.config.auth.data.models.User;
import com.solution.xpresss_assessment.config.auth.data.repositories.UserRepository;
import com.solution.xpresss_assessment.exceptions.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class XpressUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAddress(email).orElseThrow(UserNotFoundException::new);
        return new AuthenticatedUser(user);
    }
}
