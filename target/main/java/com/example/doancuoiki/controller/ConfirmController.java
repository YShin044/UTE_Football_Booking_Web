package com.example.doancuoiki.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.doancuoiki.entity.Booking;
import com.example.doancuoiki.entity.San;

import com.example.doancuoiki.respository.BookingRepository;

import com.example.doancuoiki.service.ISanService;

import com.example.doancuoiki.utils.Constant;

import jakarta.servlet.http.HttpSession;
@Controller
public class ConfirmController { 
	@Autowired
    private ISanService sanService;
	 @Autowired
	    private BookingRepository bookingRepository;
	 @GetMapping("/confirm")
	 public String bookNow( @RequestParam(value = "fieldName", required = false) String fieldName,
	                       @RequestParam(value ="bookingDate", required = false) String bookingDate,
	                       HttpSession session,
	                       Model model) {
	   
		 
		  if (fieldName == null) {
		        fieldName = (String) session.getAttribute(Constant.SESSION_FIELDNAME);
		    }
		    if (bookingDate == null) {
		        bookingDate = (String) session.getAttribute(Constant.SESSION_BOOKINGDATE);
		    }
		  
	     
	     // Tìm sân theo tên
	     San san = sanService.FindBySanName(fieldName);
	     Long sanID = san.getSan_id();
	     String san_id = sanID.toString();
	     model.addAttribute("san_id", san_id);
	     session.setAttribute(Constant.SESSION_FIELDNAME, fieldName);
	     
	     
	     model.addAttribute("fieldName", fieldName);
	     model.addAttribute("bookingDate", bookingDate);
	     List<Booking> bookingsOnDate = bookingRepository.findByDateAndSanid(bookingDate, san_id);

	     // Tạo danh sách các thời gian đã được đặt
	     List<String> disabledTimes = bookingsOnDate.stream()
	             .map(Booking::getTime)
	             .collect(Collectors.toList());

	     model.addAttribute("disabledTimes", disabledTimes);     

	     // Lấy thông tin người dùng từ session
	     String fullname = (String) session.getAttribute(Constant.SESSION_FULLNAME);
	     String user_id = (String) session.getAttribute(Constant.SESSION_USERID);
	     model.addAttribute("fullname", fullname);
	     model.addAttribute("user_id", user_id);

//	     // Truyền thông tin sân và ngày đặt vào model
//	     model.addAttribute("fieldName", fieldName);
//	     model.addAttribute("bookingDate", bookingDate);

	     // Lấy danh sách Booking trùng ngày
	 

	     // Xử lý hình ảnh sân
	     if ("Sân 5-1".equals(fieldName)) {
	         model.addAttribute("fieldImage", "/images/anh1.jpg");
	         model.addAttribute("momoImage", "/images/momo.jpg");
	     } else if ("Sân 5-2".equals(fieldName)) {
	         model.addAttribute("fieldImage", "/images/anh6.jpg");
	     } else if ("Sân 7".equals(fieldName)) {
	         model.addAttribute("fieldImage", "/images/anh3.jpg");
	     }

	     return "confirmation"; // Trả về trang xác nhận
	 }

	 @PostMapping("/confirma")
	 public String bookingsave(
	         HttpSession session,
	         San san,
	         Booking booking,
	         @RequestParam("bookingDate") String bookingDate,
	         @RequestParam("bookingTime") String time,
	         Model model
	 ) {
		 
		 
		  String fieldName = (String) session.getAttribute(Constant.SESSION_FIELDNAME);
	     // Retrieve the user ID from the session
	     String userid = (String) session.getAttribute(Constant.SESSION_USERID);
	     
	     
	     
	     

	     // Fetch the field information using the field name
	     san = sanService.FindBySanName(fieldName);
	     Long sanID = san.getSan_id();
	     String sanid = sanID.toString();

	     // Set booking details
	     booking.setDate(bookingDate);
	     booking.setSanid(sanid);
	     booking.setTime(time);
	     booking.setUserid(userid);
	     booking.setPrice("100k"); // Set the price (can be dynamically calculated based on field or time)

	     // Save the booking to the database
	     bookingRepository.save(booking);
	     session.setAttribute(Constant.SESSION_FIELDNAME, fieldName);
	     session.setAttribute(Constant.SESSION_BOOKINGDATE, bookingDate);

	     // Add booking details to the model for confirmation or review
	     model.addAttribute("booking", booking);

	     // Optionally, you can pass a success message to the model to display on the confirmation page
	     model.addAttribute("message", "Booking successfully saved!");

	     // Redirect to a confirmation page or the page where the user can view the booking details
	     return "redirect:/confirm"; // Replace "confirmation" with your actual confirmation view name
	 }

    
    
}
