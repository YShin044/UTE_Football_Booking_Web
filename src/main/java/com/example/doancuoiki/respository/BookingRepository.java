package com.example.doancuoiki.respository;

import com.example.doancuoiki.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // --- SỬA DÒNG NÀY ---
    // Tìm kiếm booking theo userid, bây giờ userid là kiểu Long
    Optional<Booking> findByUserid(Long userId);

    // Tìm kiếm booking theo bookingid, đã đúng kiểu Long
    Optional<Booking> findByBookingid(Long bookingId);

    // Tìm theo ngày và sanid, đã đúng kiểu Long
    List<Booking> findByDateAndSanid(String date, Long sanid);

    // Tìm theo ngày, sanid và thời gian, đã đúng kiểu Long
    List<Booking> findByDateAndSanidAndTime(String date, Long sanid, String time);

    List<Booking> findByUseridOrderByDateDesc(Long userId);

    // Giữ nguyên
    List<Booking> findAll();

    // --- SỬA DÒNG NÀY ---
    // Đếm các đơn chưa xác nhận (trường 'confirm' là Boolean)
    long countByConfirmIsFalse();

    List<Booking> findByConfirmIsNull();
}