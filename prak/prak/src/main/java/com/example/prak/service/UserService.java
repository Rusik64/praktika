package com.example.prak.service;

import com.example.prak.controller.form.UserForm;
import com.example.prak.repository.UserRepository;
import com.example.prak.repository.model.User;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }
    public List<User> getList() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }
    public boolean isUserWithEmailExist(String email) {
        return userRepository.countByEmail(email) != 0 ? true : false;
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User findById(Long userId) {
        return userRepository.findById(userId).get();
    }
    public Optional<User> registerUser(@Valid UserForm userForm) {
        String passwordHash = passwordEncoder.encode(userForm.getPassword());
        User user = new User();
        user.setUsername(userForm.getUsername());
        user.setEmail(userForm.getEmail());
        user.setPassword(passwordHash);
        user.setFirstname(userForm.getFirstname());
        user.setToken(generateToken(userForm.getEmail()));
        userRepository.save(user);
        System.out.print("User saved");
        mailService.sendRegistrationMail(user);
        System.out.print("Send email to" + userForm.getEmail());
        return Optional.of(user);
    }
    private String generateToken(String email) {
        String token = "";
        Long secondsFromEpoch = Instant.ofEpochSecond(0L).until(Instant.now(),
                ChronoUnit.SECONDS);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String preToken = email + secondsFromEpoch;
            byte[] array = md.digest(preToken.getBytes());
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            token = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            token = secondsFromEpoch.toString();
        }
        return token;
    }
    public Optional<User> checkEmailToken(String token) {
        Optional<User> user = userRepository.findByTokenAndConfirmedFalse(token);
        if (user.isPresent()) {
            User updateUser = user.get();
            updateUser.setConfirmed(true);
            updateUser.setToken(null);
            userRepository.save(updateUser);
        }
        return user;
    }
}