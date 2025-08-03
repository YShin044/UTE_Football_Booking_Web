package com.example.doancuoiki.controller.user;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.doancuoiki.model.UserModel;
import com.example.doancuoiki.service.IUserServices;
import com.example.doancuoiki.utils.Constant;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	private IUserServices userService; // Service để thao tác với lớp User và cơ sở dữ liệu

	//private static final String UPLOAD_DIR = "C:\\Users\\quoch\\Documents\\workspace-spring-tool-suite-4-4.20.0.RELEASE\\uploadIMG";

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


	@PostMapping("/upload-image")
	public String uploadImage(@RequestParam("image") MultipartFile image, HttpSession session, Model model) throws IOException {
	    addUserToModel(session, model);
	    String username = (String) session.getAttribute("username"); // Lấy tên người dùng từ session
	    UserModel user = userService.FindByUserName(username);
	    if (user != null) {
	        // Tạo tên file và lưu ảnh vào thư mục uploadIMG
	        String fileName = user.getId() + "-" + image.getOriginalFilename();
	        Path path = Paths.get("E:\\upload", fileName);
	        Files.write(path, image.getBytes());

	        // Lưu tên file vào thuộc tính 'image' của người dùng trong cơ sở dữ liệu
	        userService.saveimg(fileName, user);

	        // Cập nhật lại thông tin người dùng trong session sau khi lưu ảnh
	        session.setAttribute("user", user);

	    	model.addAttribute("profileImage", fileName);
	        // Quay lại trang profile
	        return "redirect:/profile";
	    }
	    // Nếu không có thông tin người dùng trong session
	    return "redirect:/login";
	}

	

}
