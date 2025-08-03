package com.example.doancuoiki.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.doancuoiki.entity.San;

@Repository
public interface SanRepository extends JpaRepository<San, Long> {
    // Tìm sân theo tên
	  San findBySanName(String sanName);
}