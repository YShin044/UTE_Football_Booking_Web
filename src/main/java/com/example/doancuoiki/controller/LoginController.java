package com.example.doancuoiki.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.doancuoiki.model.UserModel;
import com.example.doancuoiki.service.IUserServices;
import com.example.doancuoiki.utils.Constant;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;


@Controller
public class LoginController {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
    private IUserServices userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Chuyển hướng tới trang login.html
    }
    
    
    
    @PostMapping("/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String passwordHashFromClient,
            @RequestParam(value = "remember", required = false) String remember,
            HttpServletResponse response,
            HttpSession session,
            Model model) {

        boolean isRememberMe = "on".equals(remember);
        String alertMsg = "";

        if (username.isEmpty() || passwordHashFromClient.isEmpty()) {
            model.addAttribute("alert", "Tài khoản hoặc mật khẩu không được rỗng");
            return "login";
        }

        UserModel userFromDb = userService.FindByUserName(username);

        if (userFromDb != null && userFromDb.getPassword().equals(passwordHashFromClient)) {
            // Đúng mật khẩu SHA-256 (từ phía client)
            session.setAttribute(Constant.SESSION_USERNAME, username);
            session.setAttribute(Constant.SESSION_ACCOUNT, "account");
            session.setAttribute(Constant.SESSION_FULLNAME, userFromDb.getFullname());
            session.setAttribute(Constant.SESSION_EMAIL, userFromDb.getEmail());
            session.setAttribute(Constant.SESSION_PHONE, userFromDb.getPhone());
            session.setAttribute(Constant.SESSION_USERID, String.valueOf(userFromDb.getId()));
            session.setAttribute(Constant.SESSION_STATUS, userFromDb.getStatus());
            session.setAttribute("user", userFromDb);

            if (isRememberMe) {
                saveRememberMe(response, username);
            }

            return (userFromDb.getStatus() == 0) ? "redirect:/home" : "redirect:/admin";
        } else {
            model.addAttribute("alert", "Tài khoản hoặc mật khẩu không đúng");
            return "login";
        }
    }
    
    private void saveRememberMe(HttpServletResponse response, String username) {
    	Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, username);
    	cookie.setMaxAge(Constant.COOKIE_MAX_AGE);
    	cookie.setPath("/"); // Đảm bảo cookie có thể truy cập từ toàn bộ ứng dụng
    	response.addCookie(cookie);
    }
    
    @GetMapping("/forgotpassword")
    public String forgotpassword() {
    	
        return "forgotpwd"; // Chuyển hướng tới trang login.html
    }
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Các ký tự có thể dùng
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        // Chọn ngẫu nhiên ký tự từ chuỗi trên để tạo dãy
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            stringBuilder.append(characters.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }
    
    @PostMapping("/forgotpassword")
    public String forgotpasswordPage(@RequestParam("email") String email, Model model) {
        if (email == null || email.isEmpty()) {
            model.addAttribute("error", "Vui lòng nhập email.");
            return "forgotpwd"; // Trả về trang quên mật khẩu
        }
        
        if (userService.FindByEmail(email) == null) {
            model.addAttribute("error", "Email không tồn tại.");
            return "forgotpwd"; // Trả về trang quên mật khẩu
        }

        //Random random = new Random();
        String randomString = generateRandomString(10);
        userService.updatePassword(email, randomString);
        userService.sendmail(email, "change password", randomString);
        
        return "result"; // Chuyển đến trang kết quả
    }
    
    
}
