package com.example.prak.repository;

import com.example.prak.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByTokenAndConfirmedFalse(String token);
    int countByEmail(String email);
    List<User> findByFirstnameAndConfirmedTrue(String username);
    User findByUsername(String username);

    User findByUsernameAndConfirmedTrue(String username);
}