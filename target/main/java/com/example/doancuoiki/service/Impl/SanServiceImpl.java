package com.example.doancuoiki.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.doancuoiki.entity.San;

import com.example.doancuoiki.respository.SanRepository;
import com.example.doancuoiki.service.ISanService;

@Service
public class SanServiceImpl implements ISanService {

    @Autowired
    private SanRepository sanRepository;

    // Phương thức trả về id của sân qua tên sân
    @Override
    public San FindBySanName(String sanName) {
        return sanRepository.findBySanName(sanName);
    }
    
}