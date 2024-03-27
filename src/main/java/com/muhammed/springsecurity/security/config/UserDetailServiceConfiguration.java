package com.muhammed.springsecurity.security.config;

import com.muhammed.springsecurity.user.dataAccess.abstracts.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Configuration class providing a UserDetailsService bean to override loadUserByUsername method
 * to retrieve user details from the database.
 */
@Configuration
@RequiredArgsConstructor
public class UserDetailServiceConfiguration {

    private final UserDao userDao;

    /**
     * Bean definition for UserDetailsService.
     * Overrides the loadUserByUsername method to fetch user details from the database.
     * @return UserDetailsService bean
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username ->
                userDao
                        .findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
    }

}
