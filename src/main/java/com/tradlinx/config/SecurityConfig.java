package com.tradlinx.config;

import com.tradlinx.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .mvcMatchers(HttpMethod.POST, "/signup").permitAll()
                .mvcMatchers(HttpMethod.POST, "/signin").permitAll()
                .antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/swagger-resources/configuration/ui").permitAll()
                .antMatchers("/swagger-ui/index.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/api/auth/**")
                .antMatchers("/v3/api-docs/**")
                .antMatchers("configuration/**")
                .antMatchers("/swagger*/**")
                .antMatchers("/webjars/**")
                .antMatchers("/swagger-ui/**");
    }

}
