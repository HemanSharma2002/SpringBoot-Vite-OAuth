package org.example.springbootstarteroauthvitebasicsetup.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springbootstarteroauthvitebasicsetup.dto.ResponseDto;
import org.example.springbootstarteroauthvitebasicsetup.dto.ResponseUserDto;
import org.example.springbootstarteroauthvitebasicsetup.exception.UserException;
import org.example.springbootstarteroauthvitebasicsetup.service.AuthService;
import org.example.springbootstarteroauthvitebasicsetup.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    @PostMapping("/signup")
    public ResponseDto signUp(@RequestParam String email,@RequestParam String password,@RequestParam String name) {
      return authService.signUp(email, password, name);
    }
    @GetMapping("verify/{userId}/{verifyCode}")
    public RedirectView verifyCode(@PathVariable Long userId, @PathVariable String verifyCode) {
        return authService.verify(verifyCode,userId).getSuccess()?new RedirectView("/login?verified=true"):new RedirectView("/login?verified=false");

    }
    @PostMapping("/reset-password/{userId}/{verifyCode}")
    public ResponseDto resetPassword(@PathVariable Long userId, @PathVariable String verifyCode,@RequestParam String password) {
        return authService.resetPassword(userId,verifyCode,password);
    }

    // Request to reset password
    @PostMapping("/reset-password")
    public ResponseDto requestPasswordReset(@RequestParam String email) {

        return authService.requestForPasswordChange(email);
    }
    @PostMapping("/v1/api/changePassword")
    public ResponseDto changePasswordWithOldPassword(@RequestParam String oldPassword,@RequestParam String password,Authentication authentication) throws UserException {
        String email = authentication.getName();
        try {
            OAuth2AuthenticatedPrincipal principal=(OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
            email=principal.getAttribute("email").toString();
        }catch (Exception e){
            //do nothing
        }
        return authService.changePassword(email,oldPassword,password);
    }
    @GetMapping("/v1/api/getUserDetails")
    public ResponseUserDto getUserDetails(Authentication authentication) throws UserException {
        String email = authentication.getName();
        try {
            OAuth2AuthenticatedPrincipal principal=(OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
            email=principal.getAttribute("email").toString();
        }catch (Exception e){
            //do nothing
        }
        return userService.getUserByEmail(email);
    }
}