package org.example.springbootstarteroauthvitebasicsetup.configuration;

import lombok.RequiredArgsConstructor;
import org.example.springbootstarteroauthvitebasicsetup.customs.CustomAuthenticationFailureHandler;
import org.example.springbootstarteroauthvitebasicsetup.customs.CustomOauthSuccesshandler;
import org.example.springbootstarteroauthvitebasicsetup.customs.CustomUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CustomOauthSuccesshandler customOauthSuccesshandler;
    private final CustomUserDetailService customUserDetailService;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    // This code only works with Same-origin
    // if u want to run it con cross-origin customise urls with ur host url e.g. http://localhost:3000/**
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth->auth.requestMatchers("/v1/api/**").authenticated().anyRequest().permitAll());
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(login->login
                        .loginPage("/login")
                        .defaultSuccessUrl("/",true)
                        .failureHandler(customAuthenticationFailureHandler)
                        .successHandler(customOauthSuccesshandler)
                );
        http.oauth2Login(oAuth->oAuth
                        .loginPage("/login")
                        .defaultSuccessUrl("/",true)
                        .failureUrl("/login?error=true")
                        .successHandler(customOauthSuccesshandler)
                );
        http.logout(oAuth->oAuth.logoutUrl("/logout").logoutSuccessUrl("/login?logout=true"));
        return http.build();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }
}