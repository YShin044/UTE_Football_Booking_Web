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
@Table(name="sans")
@NamedQuery(name = "San.findAll", query = "SELECT s FROM San s")
public class San implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="san_id")
    private Long san_id;

    @Column(name="san_type", columnDefinition = "nvarchar(200) NULL")
    private String san_type;

    @Column(name="san_name", columnDefinition = "nvarchar(200) NULL")
    private String sanName;  // Sửa tên thuộc tính ở đây
  
    public Long getSan_id() {
        return san_id;
    }

    public void setSan_id(Long san_id) {
        this.san_id = san_id;
    }

    public String getSan_type() {
        return san_type;
    }

    public void setSan_type(String san_type) {
        this.san_type = san_type;
    }

    public String getSanName() {  // Sửa getter và setter
        return sanName;
    }

    public void setSanName(String sanName) {  // Sửa setter
        this.sanName = sanName;
    }
}
