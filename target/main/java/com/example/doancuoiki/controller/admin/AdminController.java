package com.example.doancuoiki.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.doancuoiki.model.UserModel;
import com.example.doancuoiki.service.IUserServices;
import com.example.doancuoiki.utils.Constant;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

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
		addUserToModel(session, model); // Gọi phương thức để thêm thông tin người dùng vào model
		return "adminhome"; // Trả về trang home.html
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
	public String useredit(@RequestParam("id") int id, @RequestParam("fullname") String name,
			@RequestParam("email") String email, @RequestParam("phone") String phone, HttpSession session,
			Model model) {
		addUserToModel(session, model);
		userService.updateuser(name, phone, email, id);
		return "redirect:/admin/user";
	}

}
