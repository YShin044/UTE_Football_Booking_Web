package com.example.doancuoiki.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
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
    private IUserServices userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Chuyển hướng tới trang login.html
    }
    
    @PostMapping("/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam(value = "remember", required = false) String remember,
            HttpServletResponse response,
            HttpSession session,
            Model model) {

        boolean isRememberMe = "on".equals(remember);
        String alertMsg = "";

        // Kiểm tra trường hợp tài khoản hoặc mật khẩu rỗng
        if (username.isEmpty() || password.isEmpty()) {
            alertMsg = "Tài khoản hoặc mật khẩu không được rỗng";
            model.addAttribute("alert", alertMsg);
            return "login";
        }
    
        
        // Xử lý đăng nhập
        UserModel user = userService.login(username, password);
        if (user != null) {
        	session.setAttribute(Constant.SESSION_USERNAME, username);
			session.setAttribute(Constant.SESSION_ACCOUNT, "account");
            
			
            // Lấy thông tin fullname từ cơ sở dữ liệu
            UserModel userFromDb = userService.FindByUserName(username); // Truy vấn từ UserRepository
            
            
            String fullname = userFromDb.getFullname(); // Lấy cột fullname
            String email = userFromDb.getEmail();       // Lấy cột email
            String phone = userFromDb.getPhone();       // Lấy cột phone
           
            
            Long user_id =userFromDb.getId();
            String userIdStr = user_id.toString();
            
            session.setAttribute(Constant.SESSION_FULLNAME, fullname);
            session.setAttribute(Constant.SESSION_EMAIL, email);
            session.setAttribute(Constant.SESSION_PHONE, phone);
            session.setAttribute(Constant.SESSION_USERID,userIdStr);

            
            // Lưu vào session thông tin tài khoản
            session.setAttribute("user", user); // Lưu toàn bộ thông tin user vào session
//            session.setAttribute("fullname", user.getFullname()); // Lưu fullname vào session
//            session.setAttribute(Constant.SESSION_ACCOUNT, user); // Lưu toàn bộ UserModel vào session

            if (isRememberMe) {
                saveRememberMe(response, username);
            }
            if (user.getStatus() == 0)
            	return "redirect:home"; // Đăng nhập thành công, chuyển hướng về trang chính
            else 
            	return "redirect:admin";
        } else {
            alertMsg = "Tài khoản hoặc mật khẩu không đúng";
            model.addAttribute("alert", alertMsg);
            return "/login"; // Quay lại trang đăng nhập kèm theo thông báo
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
