package org.example.springbootstarteroauthvitebasicsetup.serviceImplementation;

import lombok.RequiredArgsConstructor;
import org.example.springbootstarteroauthvitebasicsetup.dto.ResponseDto;
import org.example.springbootstarteroauthvitebasicsetup.dto.User;
import org.example.springbootstarteroauthvitebasicsetup.exception.UserException;
import org.example.springbootstarteroauthvitebasicsetup.repository.UserRepository;
import org.example.springbootstarteroauthvitebasicsetup.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public ResponseDto signUp(String email, String password, String name) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                if(user.get().getIsEnabled()){
                    return new ResponseDto(false, "User already exists, please Sign In");
                }
                //send link to email
                String code= UUID.randomUUID().toString();
                user.get().setPassword(passwordEncoder.encode(password));
                user.get().setName(name);
                user.get().setCode(code);
                user.get().setCodeExpiry(LocalDateTime.now().plusHours(2));
                // sample sout need to be replaced with email service baseurl+/auth/verify/{userId}/{verifyCode}
                System.out.println("/verify/"+user.get().getId()+"/"+code);
                System.out.println("point");
                return new ResponseDto(false, "User exist but is not enabled, Verify first using email");
            }

            String code= UUID.randomUUID().toString();
            User newUser = User
                    .builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .name(name)
                    .code(code)
                    .codeExpiry(LocalDateTime.now().plusHours(2))
                    .isEnabled(false)
                    .build();
            newUser=userRepository.save(newUser);
            //  send email...............
            // sample sout need to be replaced with email service baseurl+/auth/verify/{userId}/{verifyCode}
            System.out.println("/verify/"+newUser.getId()+"/"+code);
            System.out.println("point");
            return new ResponseDto(true, "User saved, Verify first using email");
        }
        catch (Exception e){
            return new ResponseDto(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto verify(String code, Long id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent()) {
                if(!user.get().getIsEnabled()){
                    if(user.get().getCode().equals(code)){
                        if(user.get().getCodeExpiry().isAfter(LocalDateTime.now())){
                            user.get().setIsEnabled(true);
                            user.get().setCode(UUID.randomUUID().toString());
                            userRepository.save(user.get());
                            return new ResponseDto(true, "");
                        }
                        String newCode=UUID.randomUUID().toString();
                        // send code to email
                        // sample sout need to be replaced with email service baseurl+/auth/verify/{userId}/{verifyCode}
                        System.out.println("/verify/"+user.get().getId()+"/"+code);


                        user.get().setCode(newCode);
                        user.get().setCodeExpiry(LocalDateTime.now().plusHours(2));
                        userRepository.save(user.get());
                        return new ResponseDto(false, "");
                    }
                    return new ResponseDto(false, "");
                }
                return new ResponseDto(false, "");
            }
            throw new UserException("User not found");
        }
        catch (Exception e){
            return new ResponseDto(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto resetPassword(Long UserId, String token, String password) {
        try {
            Optional<User> user = userRepository.findById(UserId);
            if (user.isPresent()) {
                if(user.get().getCode().equals(token)){
                    if(user.get().getCodeExpiry().isAfter(LocalDateTime.now())){
                        if(passwordEncoder.matches(password, user.get().getPassword())){
                            return new ResponseDto(false, "Previous password and new password are same");
                        }
                        else{
                            user.get().setPassword(passwordEncoder.encode(password));
                            user.get().setCode(UUID.randomUUID().toString());
                            userRepository.save(user.get());
                            return new ResponseDto(true, "Password changed");
                        }
                    }
                    // send email
                    String code= UUID.randomUUID().toString();
                    user.get().setCode(code);
                    user.get().setCodeExpiry(LocalDateTime.now().plusHours(2));
                    userRepository.save(user.get());

                    // sample sout need to be replaced with email service baseurl+/auth/reset-password/{userId}/{verifyCode}
                    System.out.println("/resetPassword/"+user.get().getId()+"/"+code);

                    return new ResponseDto(false, "Code is expired, New Link is send to email");
                }
                return new ResponseDto(false, "Invalid Request");
            }
            throw new UserException("User not found");
        }
        catch (Exception e){
            return new ResponseDto(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto requestForPasswordChange(String email) {
        try {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                // send email
                String code= UUID.randomUUID().toString();
                user.get().setCode(code);
                user.get().setCodeExpiry(LocalDateTime.now().plusHours(2));
                userRepository.save(user.get());

                // sample sout need to be replaced with email service baseurl+/auth/reset-password/{userId}/{verifyCode}
                System.out.println("/resetPassword/"+user.get().getId()+"/"+code);

                return new ResponseDto(true, "Email send");
            }
            throw new UserException("User not found");
        }
        catch (Exception e){
            return new ResponseDto(false, e.getMessage());
        }
    }

    @Override
    public ResponseDto changePassword(String email, String oldPassword, String newPassword) throws UserException {
        try{
            Optional<User> user= userRepository.findByEmail(email);
            if (user.isPresent()) {
                if(passwordEncoder.matches(oldPassword, user.get().getPassword())){
                    user.get().setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user.get());
                    return new ResponseDto(true, "Password changed");
                }
                return new ResponseDto(false, "Old password doesn't match");
            }
            throw new UserException("User not found");
        }
        catch (Exception e){
            return new ResponseDto(false, e.getMessage());
        }
    }
}
