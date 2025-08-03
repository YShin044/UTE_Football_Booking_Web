package com.example.doancuoiki.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.doancuoiki.model.UserModel;
import com.example.doancuoiki.service.IUserServices;
import com.example.doancuoiki.utils.Constant;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
    private IUserServices userService;
	// Phương thức giúp lấy fullname và account từ session và thêm vào model
	private void addUserToModel(HttpSession session, Model model) {
		String fullname = (String) session.getAttribute(Constant.SESSION_FULLNAME);
		String account = (String) session.getAttribute(Constant.SESSION_ACCOUNT);
		String username = (String) session.getAttribute(Constant.SESSION_USERNAME);
		String email = (String) session.getAttribute(Constant.SESSION_EMAIL);
		String phone = (String) session.getAttribute(Constant.SESSION_PHONE);

		if (account != null && fullname != null) {
			model.addAttribute("fullname", fullname);
			model.addAttribute("username", username);
			model.addAttribute("email", email);
			model.addAttribute("phone", phone);
		}

	}

	@GetMapping("/")
	public String home1(HttpSession session, Model model) {
		addUserToModel(session, model); // Gọi phương thức để thêm thông tin người dùng vào model
		return "index"; // Trả về trang home.html
	}

	@GetMapping("/home")
	public String home(HttpSession session, Model model) {
		addUserToModel(session, model); // Gọi phương thức để thêm thông tin người dùng vào model
		return "index"; // Trả về trang home.html
	}

	@GetMapping("/gallery")
	public String gallery(Model model, HttpSession session) {
		addUserToModel(session, model); // Gọi phương thức để thêm thông tin người dùng vào model
		return "gallery"; // Chuyển hướng đến trang gallery.html
	}

	@GetMapping("/booknow")
	public String testPage(Model model, HttpSession session) {
		
		 String fullname = (String) session.getAttribute(Constant.SESSION_FULLNAME);
	    String username = (String) session.getAttribute(Constant.SESSION_USERNAME);
	    if (username != null) {
	        model.addAttribute("username", username);
	        model.addAttribute("fullname", fullname);
	    } else {
	        model.addAttribute("username", "Guest");
	        model.addAttribute("fullname", fullname);// Nếu không có username, hiển thị Guest
	    }
	    return "booknow"; // Chuyển hướng đến trang test.html
	}

	@GetMapping("/profile")
	public String profile(Model model, HttpSession session) {
	    addUserToModel(session, model); // Gọi phương thức để thêm thông tin người dùng vào model
	    String username = (String) session.getAttribute("username"); // Lấy tên người dùng từ session
		UserModel user = userService.FindByUserName(username);
	    if (user != null) {
	        model.addAttribute("profileImage",user.getImages()); // Truyền tên ảnh vào model
	    }
	    return "profile"; // Trả về trang profile
	}

	@PostMapping("/changepassword")
	public String changepassword(@RequestParam("currentPassword") String currentPassword,
			@RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword,
			HttpSession session, Model model) {

		// Lấy thông tin người dùng từ session
		String username = (String) session.getAttribute("username"); // Lấy tên người dùng từ session
		UserModel user = userService.FindByUserName(username); // Tìm người dùng theo tên

		if (user != null) {
			// Kiểm tra mật khẩu cũ
			if (!user.getPassword().equals(currentPassword)) {
				model.addAttribute("error", "Mật khẩu cũ không đúng!");
				return "changepassword";
				}
			if (!newPassword.equals(confirmPassword)) {
				model.addAttribute("error", "Mật khẩu nhập lại không khớp!");
				return "changepassword";
				}
			}
		
			userService.changepassword(confirmPassword, user);
			
		return "profile";
	}
	
	@GetMapping("/changepassword")
	public String changepassword(Model model, HttpSession session) {
		addUserToModel(session, model); // Gọi phương thức để thêm thông tin người dùng vào model
		return "changepassword"; // Chuyển hướng đến trang booknow.html
	}


}
