package com.example.doancuoiki.controller;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.doancuoiki.service.IUserServices;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller
public class RegisterController {

    @Autowired
    private IUserServices userService;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               @RequestParam String fullname,
                               @RequestParam String phone,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        // Hash password using SHA-256
        String hashedPassword = hashSHA256(password);

        boolean isSuccess = userService.register(username, hashedPassword, email, fullname, phone);
        if (isSuccess) {
            redirectAttributes.addFlashAttribute("alert", "Đăng ký thành công!");
            return "login";
        } else {
            model.addAttribute("popup", "Đăng ký thất bại! Tên đăng nhập, email hoặc số điện thoại đã tồn tại.");
            return "register";
        }
    }

    // SHA-256 hash function
    private String hashSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}
