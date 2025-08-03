package com.example.doancuoiki.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id // Đánh dấu đây là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Database sẽ tự động tăng giá trị này
    private Long bookingid;

    private Long sanid;
    private Long userid;
    private String date;
    private String time;
    private String price;
    private Boolean confirm;

    // Constructors (hàm khởi tạo)
    public Booking() {
        // Constructor rỗng cần thiết cho JPA
    }

    public Booking(Long sanid, Long userid, String date, String time, String price, Boolean confirm) {
        this.sanid = sanid;
        this.userid = userid;
        this.date = date;
        this.time = time;
        this.price = price;
        this.confirm = confirm;
    }


    // Getters and Setters (Tường minh để đảm bảo trình biên dịch thấy chúng)

    public Long getBookingid() {
        return bookingid;
    }

    public void setBookingid(Long bookingid) {
        this.bookingid = bookingid;
    }

    public Long getSanid() {
        return sanid;
    }

    public void setSanid(Long sanid) {
        this.sanid = sanid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Boolean getConfirm() {
        return confirm;
    }

    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
    }
}