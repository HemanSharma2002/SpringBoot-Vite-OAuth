package org.example.springbootstarteroauthvitebasicsetup.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/v1/api/test")
public class TestController {
    //check
    @GetMapping("/1")
    public Object test1(Authentication authentication) {
        String username = authentication.getName();
        try{
            OAuth2AuthenticatedPrincipal principal = (OAuth2AuthenticatedPrincipal) authentication.getPrincipal();
            username = principal.getAttribute("email");
            return principal;
        }
        catch (Exception e){
            //do nothing
        }
        return username;
    }
}
