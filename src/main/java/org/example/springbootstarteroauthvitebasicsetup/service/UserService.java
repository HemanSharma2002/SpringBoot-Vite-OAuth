package org.example.springbootstarteroauthvitebasicsetup.service;

import org.example.springbootstarteroauthvitebasicsetup.dto.ResponseUserDto;
import org.example.springbootstarteroauthvitebasicsetup.dto.User;
import org.example.springbootstarteroauthvitebasicsetup.exception.UserException;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User findByEmail(String email) throws UserException;
    User saveUser(User user) throws UserException;
    ResponseUserDto getUserByEmail(String email) throws UserException;
}