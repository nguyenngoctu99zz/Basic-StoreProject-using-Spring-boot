package com.boostmytool.beststore.controller;

import com.boostmytool.beststore.dao.UserRepository;
import com.boostmytool.beststore.model.User;
import com.boostmytool.beststore.model.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class Register {

    @Autowired
    private UserRepository userRes;

    @GetMapping
    public String RegisterRedirection(Model model) {
        model.addAttribute("registerUpdate", new UserDTO());
        return "register";
    }

    @PostMapping("/checkandsave")
    public String RegisterCheckAndSave(
            @RequestParam(value="username") String username,
            @RequestParam(value="email") String email,
            @RequestParam(value="phonenumber") String phonenumber,
            @RequestParam(value="matkhau") String password,
            @RequestParam(value="matkhau2") String password2,
            @RequestParam(value="role") String role,
            Model model, @Valid @ModelAttribute("registerUpdate") UserDTO userDTO
    ) {
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("errorForUserName", "Username not allow to be null");
            return "register";
        }

        User existingUser = userRes.findByUsername(username);
        if (existingUser != null) {
            model.addAttribute("existingError", "This user already exists");
            return "register";
        }

        if (password == null || password.trim().isEmpty()) {
            model.addAttribute("errorForPassword", "Password not allow to be null");
            return "register";
        }

        if (password2 == null || password2.trim().isEmpty()) {
            model.addAttribute("errorForPassword2", "Password must be repeated");
            return "register";
        }

        if (!password.equals(password2)) {
            model.addAttribute("NotMatchingError", "Passwords must match");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPhonenumber(phonenumber);
        user.setPassword("{noop}" + password); // Noop encoder for plain text password
        user.setRole(role);
        userRes.save(user);

        return "redirect:/";
    }
}
