package org.example.springbootstarteroauthvitebasicsetup.repository;

import org.example.springbootstarteroauthvitebasicsetup.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Repository
@Service
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}