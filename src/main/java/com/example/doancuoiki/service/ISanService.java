package com.example.doancuoiki.service;

import org.springframework.stereotype.Service;

import com.example.doancuoiki.entity.San;
@Service
public interface ISanService {

	San FindBySanName(String sanName);

}
