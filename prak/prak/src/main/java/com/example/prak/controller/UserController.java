package com.example.prak.controller;

import com.example.prak.repository.model.Product;
import com.example.prak.repository.model.User;
import com.example.prak.controller.form.UserForm;
import com.example.prak.controller.form.UserFormValidator;
import com.example.prak.service.ProductService;
import com.example.prak.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
@Controller
public class UserController {
    private final UserService userService;
    UserFormValidator userFormValidator;
    private final ProductService productService;

    public UserController(UserService userService, UserFormValidator userFormValidator, ProductService productService) {
        this.userService = userService;
        this.userFormValidator = userFormValidator;
        this.productService = productService;
    }
    @InitBinder
    private void InitBinder(WebDataBinder webDataBinder) {
        if (webDataBinder.getTarget() != null && webDataBinder.getTarget().getClass() == UserForm.class) {
            webDataBinder.setValidator(userFormValidator);
        }
    }
    @GetMapping("/user/registration")
    public ModelAndView userRegistration(ModelAndView model) {
        model.addObject(new UserForm());
        model.setViewName("registration");
        return model;
    }
    @PostMapping("/user/registration")
    public ModelAndView userRegistrationSubmit(ModelAndView model, @ModelAttribute @Valid UserForm userForm, BindingResult result) {
        if(result.hasErrors()) {
            model.setViewName("registration");
            return model;
        }
        System.out.print("user registration");
        Optional<User> newUser = userService.registerUser(userForm);
        model.addObject("email", userForm.getEmail());
        model.setViewName("mail-confirmation");
        return model;
    }
    @GetMapping("/user/confirm-email")
    public ModelAndView validateEmail(ModelAndView model, @RequestParam String token) {
        Optional<User> user = userService.checkEmailToken(token);
        if (user.isEmpty()) {
            model.setViewName("mail-not-confirmed");
            return model;
        }
        model.setViewName("mail-confirmed");
        return model;
    }

    @GetMapping("/login")
    public ModelAndView loginPage(ModelAndView model) {
        model.setViewName("login");
        return model;
    }

    @GetMapping("/profile")
    public ModelAndView userProfile(Principal principal) {
        ModelAndView model = new ModelAndView("profile");
        User user = userService.getByUsername(principal.getName());
        List<Product> products = productService.getAllByAuthor(user);
        model.addObject("user", user);
        model.addObject("products", products);
        return model;
    }

}