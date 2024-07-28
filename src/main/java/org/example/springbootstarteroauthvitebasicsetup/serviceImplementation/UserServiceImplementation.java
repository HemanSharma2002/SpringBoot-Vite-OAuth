package org.example.springbootstarteroauthvitebasicsetup.serviceImplementation;

import lombok.RequiredArgsConstructor;
import org.example.springbootstarteroauthvitebasicsetup.dto.ResponseDto;
import org.example.springbootstarteroauthvitebasicsetup.dto.ResponseUserDto;
import org.example.springbootstarteroauthvitebasicsetup.dto.User;
import org.example.springbootstarteroauthvitebasicsetup.exception.UserException;
import org.example.springbootstarteroauthvitebasicsetup.repository.UserRepository;
import org.example.springbootstarteroauthvitebasicsetup.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    @Override
    public User findByEmail(String email) throws UserException {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }

    @Override
    public User saveUser(User user) throws UserException {
        return userRepository.save(user);
    }

    @Override
    public ResponseUserDto getUserByEmail(String email) throws UserException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return new ResponseUserDto(true,"User", user.get());        }
        return new  ResponseUserDto(false,"User not found",null);
    }
}
