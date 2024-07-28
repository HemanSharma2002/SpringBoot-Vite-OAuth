package org.example.springbootstarteroauthvitebasicsetup.service;

import org.example.springbootstarteroauthvitebasicsetup.dto.ResponseDto;
import org.example.springbootstarteroauthvitebasicsetup.exception.UserException;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ResponseDto signUp(String email, String password,String name);
    ResponseDto verify(String code,Long id);
    ResponseDto resetPassword(Long UserId,String token,String password);
    ResponseDto requestForPasswordChange(String email);
    ResponseDto changePassword(String email,String oldPassword,String newPassword) throws UserException;
}
