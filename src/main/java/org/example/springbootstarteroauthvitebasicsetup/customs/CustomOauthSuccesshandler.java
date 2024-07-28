package org.example.springbootstarteroauthvitebasicsetup.customs;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springbootstarteroauthvitebasicsetup.dto.User;
import org.example.springbootstarteroauthvitebasicsetup.repository.UserRepository;
import org.example.springbootstarteroauthvitebasicsetup.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomOauthSuccesshandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService userService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            OAuth2AuthenticatedPrincipal principal=(OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
            String email=principal.getAttribute("email");
            if(userService.findByEmail(email)==null) {
                User newUser= User
                        .builder()
                        .email(email)
                        .name(principal.getAttribute("name"))
                        .password(UUID.randomUUID().toString())
                        .code(UUID.randomUUID().toString())
                        .isEnabled(true)
                        .codeExpiry(LocalDateTime.now().plusHours(2))
                        .picture(principal.getAttribute("picture"))
                        .build();
                userService.saveUser(newUser);
            }

        }
        catch (Exception e) {
            //do nothing and skipp
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}