package com.example.doancuoiki.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.doancuoiki.entity.Booking;
import com.example.doancuoiki.model.UserModel;
import com.example.doancuoiki.respository.BookingRepository;
import com.example.doancuoiki.respository.UserRepository;
import com.example.doancuoiki.service.IUserServices;
import com.example.doancuoiki.utils.Constant;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	 @Autowired
	    private BookingRepository bookingRepository;
	 @Autowired
	    private UserRepository userRepository;
	//guitamihsidjhaidh
	@Autowired
	private IUserServices userService;
 
	
	
	
	
	private void addUserToModel(HttpSession session, Model model) {
		String fullname = (String) session.getAttribute(Constant.SESSION_FULLNAME);
		String account = (String) session.getAttribute(Constant.SESSION_ACCOUNT);

		if (account != null && fullname != null) {
			model.addAttribute("fullname", fullname);
		}
	}
	
     
	
	
	
	
	@GetMapping("/admin")
	public String adminhome(HttpSession session, Model model) {
	    Object status = session.getAttribute(Constant.SESSION_STATUS);

	    // Nếu chưa đăng nhập hoặc không phải admin/manager (status ≠ 1 hoặc 2) thì về home
	    if (status == null || (!(status.equals(1) || status.equals(2)))) {
	        return "redirect:/home"; 
	    }

	    addUserToModel(session, model); // Đã đăng nhập hợp lệ

	    List<UserModel> users = userRepository.findAll();
	    model.addAttribute("totalUsers", users.size());

	    List<Booking> unconfirmedBookings = bookingRepository.findByConfirmNull();
	    model.addAttribute("unconfirmedCount", unconfirmedBookings.size());

	    return "adminhome";
	}


	@GetMapping("/admin/user")
	public String adminuser(HttpSession session, Model model) {
		addUserToModel(session, model);

		// Lấy danh sách user từ service
		List<UserModel> users = userService.getalluser();

		// Lọc user có status khác 2
		List<UserModel> filteredUsers = users.stream().filter(user -> user.getStatus() != 2).toList();

		// Thêm danh sách đã lọc vào model
		model.addAttribute("users", filteredUsers);
		return "ManaUser";
	}

	@GetMapping("/admin/user/adduser")
	public String adminuseradd(HttpSession session, Model model){
		addUserToModel(session, model);	
		return "AddUser";
	}    
	@PostMapping("/admin/adduser")
	public String addUser(
	        @RequestParam String username,
	        @RequestParam String password,
	        @RequestParam String email,
	        @RequestParam String fullname,
	        @RequestParam String phone,
	        @RequestParam int status,
	        Model model) {

	    // Kiểm tra biên dữ liệu
	    if (username.length() < 4 || username.length() > 20 || !username.matches("^[a-zA-Z0-9_]+$")) {
	        model.addAttribute("message", "Tên đăng nhập phải từ 4-20 ký tự, chỉ chứa chữ, số hoặc dấu gạch dưới.");
	        return "AddUser";
	    }
	    if (password.length() < 6 || password.length() > 50) {
	        model.addAttribute("message", "Mật khẩu phải từ 6-50 ký tự.");
	        return "AddUser";
	    }
	    if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
	        model.addAttribute("message", "Email không hợp lệ.");
	        return "AddUser";
	    }
	    if (fullname.length() < 2 || fullname.length() > 50) {
	        model.addAttribute("message", "Họ tên phải từ 2-50 ký tự.");
	        return "AddUser";
	    }
	    if (!phone.matches("^\\d{10,11}$")) {
	        model.addAttribute("message", "Số điện thoại phải có 10-11 chữ số.");
	        return "AddUser";
	    }
	    if (status < 0 || status > 2) {
	        model.addAttribute("message", "Trạng thái không hợp lệ.");
	        return "AddUser";
	    }

	    boolean isSuccess = userService.addUser(username, password, email, fullname, phone, status);
	    if (isSuccess) {
	        model.addAttribute("message", "Đăng ký thành công!");
	        return "redirect:/admin/user";
	    } else {
	        model.addAttribute("message", "Đăng ký thất bại! Tên đăng nhập, email hoặc số điện thoại đã tồn tại.");
	        return "AddUser";
	    }
	}

        
	@GetMapping("/admin/user/delete/{id}")
	public String userdelete(@PathVariable int id, HttpSession session, Model model) {
		addUserToModel(session, model);
		userService.deleteuser(id);
		return "redirect:/admin/user";
	}

	@GetMapping("/admin/user/edit/{id}")
	public String userEdit(@PathVariable("id") int id, HttpSession session, Model model) {
		addUserToModel(session, model);
		UserModel user = userService.findById(id);
		if (user != null) {
			model.addAttribute("user", user);
		} else {
			return "redirect:/admin/user";
		}
		return "ManaUserEdit";
	}

	@PostMapping("/admin/user/edit/{id}")
	public String useredit(
	        @RequestParam("id") int id,
	        @RequestParam("fullname") String name,
	        @RequestParam("email") String email,
	        @RequestParam("phone") String phone,
	        @RequestParam("status") int status,
	        HttpSession session,
	        Model model) {

	    addUserToModel(session, model);

	    // Kiểm tra biên
	    if (name.length() < 2 || name.length() > 50) {
	        model.addAttribute("message", "Họ tên phải từ 2-50 ký tự.");
	        return "ManaUserEdit";
	    }
	    if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
	        model.addAttribute("message", "Email không hợp lệ.");
	        return "ManaUserEdit";
	    }
	    if (!phone.matches("^\\d{10,11}$")) {
	        model.addAttribute("message", "Số điện thoại phải có 10-11 chữ số.");
	        return "ManaUserEdit";
	    }
	    if (status < 0 || status > 2) {
	        model.addAttribute("message", "Trạng thái không hợp lệ.");
	        return "ManaUserEdit";
	    }

	    userService.updateuser(name, phone, email, id, status);
	    return "redirect:/admin/user";
	}

	@PostMapping("/admin/confirm-booking")
	@ResponseBody
	public ResponseEntity<?> confirmBooking(@RequestParam Long bookingId) {
	    Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
	    if (bookingOpt.isPresent()) {
	        Booking booking = bookingOpt.get();
	        booking.setConfirm(true); // Xác nhận đặt chỗ
	        bookingRepository.save(booking);
	        return ResponseEntity.ok().build();
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found");
	    }
	}
	
	//Xem toàn bộ đơn hàng
	@GetMapping("/admin/bookings")
	public String checkBooking(Model model) {
	
		List<Booking> bookings = bookingRepository.findAll();
		model.addAttribute("bookings", bookings);
		return "adminbooking";
	}
	
}
