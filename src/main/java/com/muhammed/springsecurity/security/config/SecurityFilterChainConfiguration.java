package com.muhammed.springsecurity.security.config;

import com.muhammed.springsecurity.security.filter.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * Configuration class to define security filters and rules for HTTP requests.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity // To enable method level security
public class SecurityFilterChainConfiguration {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    private static final String[] WHITE_LIST_URL = {"/h2-console/**",
            "/api/v1/customers/register", "/api/v1/customers/login", "/api/v1/customers/refresh-token"};

    /**
     * Defines the security filter chain with specific rules for HTTP requests.
     *
     * @param httpSecurity The HttpSecurity object used to configure security.
     * @return The SecurityFilterChain with configured security rules.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(WHITE_LIST_URL).permitAll()
//                        .requestMatchers("/api/v1/customers/**").hasAnyRole(ADMIN.name(), CUSTOMER.name())
//                        .requestMatchers(GET, "/api/v1/admin/**").hasAnyAuthority(ADMIN_READ.name())
//                        .requestMatchers(POST, "/api/v1/admin/**").hasAnyAuthority(ADMIN_CREATE.name())
//                        .requestMatchers(PUT, "/api/v1/admin/**").hasAnyAuthority(ADMIN_UPDATE.name())
//                        .requestMatchers(DELETE, "/api/v1/admin/**").hasAnyAuthority(ADMIN_DELETE.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .logout((logout) ->
                        logout
                                .logoutUrl("/api/v1/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return httpSecurity.build();
    }
}
