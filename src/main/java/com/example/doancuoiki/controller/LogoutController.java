package com.example.doancuoiki.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {
	
	@GetMapping("/logout")
    public String logout(HttpSession session) {
        // Hủy session hiện tại
        session.invalidate();  // Xóa tất cả các dữ liệu trong session

        // Chuyển hướng về trang đăng nhập
        return "redirect:/home";  // Chuyển hướng về trang đăng nhập (có thể thay đổi thành trang khác)
	}
}
