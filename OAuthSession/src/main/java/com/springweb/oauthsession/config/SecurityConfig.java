package com.springweb.oauthsession.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf)-> csrf.disable());
        http
                .formLogin((login)-> login.disable());
        http
                .httpBasic((httpBasic)-> httpBasic.disable());
        http
                .oauth2Login(Customizer.withDefaults());
        http//인가작업
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/oauth2/**", "/login**", "/logout**").permitAll()//허용 루트
                        .anyRequest().authenticated());

        return http.build();
    }
}
