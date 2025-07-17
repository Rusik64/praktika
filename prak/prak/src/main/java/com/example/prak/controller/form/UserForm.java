package com.example.prak.controller.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
public class UserForm {
    @NotNull
    @Email(message = "Адрес электронной почты должен содержать @")
    private String email;
    @NotNull
    @Size(min = 8, max = 255)
    private String password;
    @NotNull
    @Size(min = 3, max = 16)
    private String username;
    @NotNull
    @Size(max = 255)
    private String firstname;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}