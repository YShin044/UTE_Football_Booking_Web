package com.example.doancuoiki.service.Impl;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.doancuoiki.entity.Booking;
import com.example.doancuoiki.respository.BookingRepository;
import com.example.doancuoiki.service.IBookingService;



@Service
public class BookingServiceImpl implements IBookingService {
	 
	 @Autowired
	    private BookingRepository bookingRepository;

	
	 
	 
}
