package com.example.doancuoiki.controller;

import com.example.doancuoiki.entity.Booking;
import com.example.doancuoiki.entity.San;
import com.example.doancuoiki.respository.BookingRepository;
import com.example.doancuoiki.service.ISanService;
import com.example.doancuoiki.utils.Constant;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ConfirmController {

    @Autowired
    private ISanService sanService;

    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping("/pre-confirm")
    public String saveToSessionAndRedirect(
            @RequestParam String fieldName,
            @RequestParam String bookingDate,
            HttpSession session
    ) {
        // --- DEBUG ---: KIỂM TRA DỮ LIỆU NHẬN TỪ FORM
        System.out.println("--- BƯỚC 1: BÊN TRONG /pre-confirm ---");
        System.out.println("Nhận được fieldName từ form: '" + fieldName + "'");
        System.out.println("Nhận được bookingDate từ form: '" + bookingDate + "'");
        // --- HẾT DEBUG ---

        session.setAttribute(Constant.SESSION_FIELDNAME, fieldName);
        session.setAttribute(Constant.SESSION_BOOKINGDATE, bookingDate);
        return "redirect:/confirm";
    }

    @GetMapping("/confirm")
    public String confirmPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // --- DEBUG ---: KIỂM TRA DỮ LIỆU LẤY TỪ SESSION
        System.out.println("\n--- BƯỚC 2: BÊN TRONG GET /confirm ---");
        String fieldName = (String) session.getAttribute(Constant.SESSION_FIELDNAME);
        String bookingDate = (String) session.getAttribute(Constant.SESSION_BOOKINGDATE);

        System.out.println("Lấy từ session fieldName: '" + fieldName + "'");
        System.out.println("Lấy từ session bookingDate: '" + bookingDate + "'");
        // --- HẾT DEBUG ---

        // Kiểm tra điều kiện 1: Dữ liệu session có tồn tại không?
        if (fieldName == null || bookingDate == null) {
            // --- DEBUG ---
            System.out.println(">>> KẾT QUẢ: LỖI - Dữ liệu session bị null. Chuyển hướng về /booknow.");
            // --- HẾT DEBUG ---
            return "redirect:/booknow";
        }

        // --- DEBUG ---
        System.out.println("Đang tìm kiếm sân trong database với tên: '" + fieldName + "'");
        // --- HẾT DEBUG ---
        San san = sanService.FindBySanName(fieldName);

        // Kiểm tra điều kiện 2: Tên sân có khớp với database không?
        if (san == null) {
            // --- DEBUG ---
            System.out.println(">>> KẾT QUẢ: LỖI - Không tìm thấy sân trong database. Chuyển hướng về /booknow.");
            // --- HẾT DEBUG ---
            redirectAttributes.addFlashAttribute("errorMessage", "Sân với tên '" + fieldName + "' không tồn tại trong CSDL.");
            return "redirect:/booknow";
        }
        
        // --- DEBUG ---
        System.out.println(">>> KẾT QUẢ: THÀNH CÔNG - Tìm thấy sân. Đang chuẩn bị hiển thị trang confirmation.");
        // --- HẾT DEBUG ---

        Long sanID = san.getSan_id();
        List<Booking> bookingsOnDate = bookingRepository.findByDateAndSanid(bookingDate, sanID);
        List<String> disabledTimes = bookingsOnDate.stream()
                .map(booking -> booking.getTime())
                .collect(Collectors.toList());

        model.addAttribute("fieldName", fieldName);
        model.addAttribute("bookingDate", bookingDate);
        String fullname = (String) session.getAttribute(Constant.SESSION_FULLNAME);
        model.addAttribute("fullname", fullname);
        model.addAttribute("user_id", session.getAttribute(Constant.SESSION_USERID));
        model.addAttribute("san_id", sanID);
        model.addAttribute("disabledTimes", disabledTimes);
        model.addAttribute("selectedSan", san);

        if ("Sân 5-1".equals(fieldName)) {
            model.addAttribute("fieldImage", "/images/anh1.jpg");
        } else if ("Sân 5-2".equals(fieldName)) {
            model.addAttribute("fieldImage", "/images/anh6.jpg");
        } else if ("Sân 7".equals(fieldName)) {
            model.addAttribute("fieldImage", "/images/anh3.jpg");
        }

        return "confirmation";
    }

    /**
     * Xử lý việc lưu thông tin đặt sân vào cơ sở dữ liệu.
     * Nên đổi tên từ `/confirma` thành `/confirm` và dùng phương thức POST để rõ ràng hơn.
     */
    @PostMapping("/confirm")
    public String processBooking(
            HttpSession session,
            @RequestParam("bookingTime") String time,
            RedirectAttributes redirectAttributes
    ) {
        String fieldName = (String) session.getAttribute(Constant.SESSION_FIELDNAME);
        String bookingDate = (String) session.getAttribute(Constant.SESSION_BOOKINGDATE);
        String userid = (String) session.getAttribute(Constant.SESSION_USERID);

        // Kiểm tra dữ liệu hợp lệ
        if (fieldName == null || userid == null || bookingDate == null || !isValidDate(bookingDate) || !ALLOWED_TIMES.contains(time)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Thông tin đặt sân không hợp lệ. Vui lòng thử lại.");
            return "redirect:/booknow";
        }

        // Tìm lại sân, vẫn cần kiểm tra null ở đây để đảm bảo an toàn
        San san = sanService.FindBySanName(fieldName);
        if (san == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sân. Vui lòng chọn lại.");
            return "redirect:/booknow";
        }
        
        // **QUAN TRỌNG**: Kiểm tra xem khung giờ này đã được đặt chưa trước khi lưu
        List<Booking> existingBookings = bookingRepository.findByDateAndSanidAndTime(bookingDate, san.getSan_id(), time);
        if (!existingBookings.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Khung giờ này vừa có người khác đặt. Vui lòng chọn giờ khác.");
            return "redirect:/confirm"; // Quay lại trang confirm để người dùng chọn lại giờ
        }

        // Tạo đối tượng Booking mới
        Booking booking = new Booking();
        booking.setDate(bookingDate);
        booking.setSanid(san.getSan_id()); // Sử dụng trực tiếp Long ID
        booking.setTime(time);
        booking.setUserid(Long.parseLong(userid)); // Giả định userid là số
        booking.setPrice("100k"); // Ghi chú: Nên lấy giá từ đối tượng Sân thay vì hardcode

        bookingRepository.save(booking);

        // Gửi thông báo thành công về trang kết quả
        redirectAttributes.addFlashAttribute("successMessage", "Bạn đã đặt sân '" + fieldName + "' thành công vào lúc " + time + " ngày " + bookingDate + "!");
        return "redirect:/booking-success"; // Chuyển đến một trang thông báo thành công
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private static final List<String> ALLOWED_TIMES = List.of(
            "06h-07h", "07h-08h", "08h-09h", "09h-10h",
            "10h-11h", "13h-14h", "14h-15h", "15h-16h",
            "16h-17h", "17h-18h", "18h-19h", "19h-20h",
            "20h-21h", "21h-22h", "22h-23h"
    );

    // Bạn cần tạo thêm một Controller và một trang HTML cho "/booking-success"
    @GetMapping("/booking-success")
    public String bookingSuccessPage() {
        return "booking-success"; // Trả về trang booking-success.html
    }
}