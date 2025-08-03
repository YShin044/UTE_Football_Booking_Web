package com.example.doancuoiki.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.doancuoiki.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking ,Long> {
	  // Find booking by user_id
    Optional<Booking> findByuserid(String userId);

    // Find booking by booking_id
    Optional<Booking> findBybookingid(Long bookingId); 

	List<Booking> findByDateAndSanid(String bookingDate, String fieldName);
}
