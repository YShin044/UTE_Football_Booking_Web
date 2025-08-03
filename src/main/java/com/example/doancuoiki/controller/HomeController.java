package com.example.doancuoiki.controller;

import com.example.doancuoiki.entity.Booking;
import com.example.doancuoiki.model.UserModel;
import com.example.doancuoiki.respository.BookingRepository;
import com.example.doancuoiki.service.IUserServices;
import com.example.doancuoiki.utils.Constant;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.List; // <<< THÊM

@Controller
public class HomeController {

	@Autowired
    private IUserServices userService;
    
    // --- THÊM VÀO ---
    @Autowired
    private BookingRepository bookingRepository;
    // --- KẾT THÚC THÊM VÀO ---

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
		
	    // 1. Giữ nguyên logic cũ để hiển thị thông tin người dùng
	    addUserToModel(session, model);
	    String username = (String) session.getAttribute("username");
		UserModel user = userService.FindByUserName(username);
	    if (user != null) {
	        model.addAttribute("profileImage", user.getImages());
	    }
        
        // 2. Lấy userid từ session
	    String userIdStr = (String) session.getAttribute(Constant.SESSION_USERID);
        if (userIdStr != null) {
            try {
                // Chuyển đổi userId từ String sang Long
                Long userId = Long.parseLong(userIdStr);
                
                // 3. Lấy lịch sử đặt sân từ repository
                List<Booking> bookingHistory = bookingRepository.findByUseridOrderByDateDesc(userId);
                
                // 4. Gửi danh sách lịch sử đến view
                model.addAttribute("bookingHistory", bookingHistory);
            } catch (NumberFormatException e) {
                // Xử lý trường hợp userId trong session không phải là số
                System.err.println("Lỗi: User ID trong session không hợp lệ: " + userIdStr);
            }
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

    private static final String UPLOAD_DIR = "E:/upload/";

    @PostMapping("/upload-image")
    public String uploadImage(
            @RequestParam("image") MultipartFile image, 
            HttpSession session, 
            RedirectAttributes redirectAttributes) {

        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }
        
        if (image.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng chọn một file ảnh.");
            return "redirect:/profile";
        }

        try {
            UserModel user = userService.FindByUserName(username);
            if (user == null) {
                return "redirect:/login";
            }
            
            // Tạo tên file và lưu ảnh
            String fileName = user.getId() + "-" + image.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);

            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Cập nhật tên file vào database
            userService.saveimg(fileName, user);

            redirectAttributes.addFlashAttribute("success", "Cập nhật ảnh thành công!");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi lưu ảnh.");
        }
        
        return "redirect:/profile";
    }
}
