package com.example.doancuoiki.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name="bookings")
@NamedQuery(name="Booking.findAll", query="SELECT b FROM Booking b")	
public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bookingid")
    private Long bookingid;

    @Column(name="sanid", columnDefinition = "nvarchar(200) NULL")
    private String sanid;

    @Column(name="userid", columnDefinition = "nvarchar(200) NULL")
    private String userid;

    @Column(name="date", columnDefinition = "nvarchar(200) NULL")
    private String date;

    @Column(name="time", columnDefinition = "nvarchar(200) NULL")
    private String time;

    @Column(name="Price", columnDefinition = "nvarchar(200) NULL")
    private String price;
    
	@Column(name = "confirm", columnDefinition = "BOOLEAN DEFAULT NULL")
    private Boolean confirm;

	
	
	public Boolean getConfirm() {
		return confirm;
	}

	public void setConfirm(Boolean confirm) {
		this.confirm = confirm;
	}

    // Getter và Setter
    public Long getBookingid() {
        return bookingid;
    }

    public void setBookingid(Long bookingid) {
        this.bookingid = bookingid;
    }

    public String getSanid() {
        return sanid;
    }

    public void setSanid(String sanid) {
        this.sanid = sanid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
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

    // Constructor không tham số
    public Booking() {
    }
}
