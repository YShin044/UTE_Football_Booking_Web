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

    @Override
    public San FindBySanName(String sanName) {
        // 1. Kiểm tra nếu tên sân truyền vào bị null thì trả về null luôn, tránh lỗi
        if (sanName == null) {
            return null;
        }

        // 2. Dùng .trim() để tự động cắt bỏ các khoảng trắng thừa ở đầu và cuối chuỗi
        String trimmedSanName = sanName.trim();

        // 3. In ra để debug (bạn có thể xóa dòng này sau khi đã chạy thành công)
        System.out.println("SERVICE LAYER: Đang tìm kiếm sân với tên đã được làm sạch: '" + trimmedSanName + "'");

        // 4. Gọi repository với tên đã được làm sạch
        return sanRepository.findBySanName(trimmedSanName);
    }
    
}