package com.tradlinx.config;

import com.tradlinx.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests()
                .mvcMatchers(HttpMethod.POST, "/signup").permitAll()
                .mvcMatchers(HttpMethod.POST, "/signin").permitAll()
                .anyRequest().authenticated();
        http.csrf().disable();
                http.apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }


}
