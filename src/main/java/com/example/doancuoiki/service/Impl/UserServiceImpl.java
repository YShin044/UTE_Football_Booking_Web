package com.example.doancuoiki.service.Impl;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import com.example.doancuoiki.model.UserModel;
import com.example.doancuoiki.respository.UserRepository;
import com.example.doancuoiki.service.IUserServices;

@Service
public class UserServiceImpl implements IUserServices {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JavaMailSender mailsender;
    
    
    public void sendmail(String tomail,
    					String subject,
    					String body) {
    	SimpleMailMessage ms = new SimpleMailMessage();
    	ms.setFrom("xuxutruong123@gmail.com");
    	ms.setTo(tomail);
    	ms.setText(body);
    	ms.setSubject(subject);
    	mailsender.send(ms);
    }

    @Override
    public UserModel login(String username, String password) {
        Optional<UserModel> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && password.equals(userOpt.get().getPassword())) {
            return userOpt.get();
        }
        return null;
    }

    @Override
    public UserModel FindByUserName(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public void insert(UserModel user) {
        // Lưu mật khẩu trực tiếp, không mã hóa
        userRepository.save(user);
    }

    @Override
    public boolean register(String username, String password,String email,String fullname, String phone) {
        if (checkExistEmail(email) || checkExistUsername(username) || checkExistPhone(phone)) {
            return false;
        }

        UserModel user = new UserModel();
        user.setUsername(username);
        user.setPassword(password);  // Nên mã hóa mật khẩu trước khi lưu
        user.setEmail(email);
        user.setPhone(phone);
        user.setFullname(fullname);

        // Lưu người dùng vào cơ sở dữ liệu
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean checkExistPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public String generateResetToken(String email) {
        UserModel user = FindByEmail(email);
        if (user == null) {
            return null;
        }

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);
        return token;
    }

    @Override
    public boolean isValidToken(String token) {
        return userRepository.existsByResetToken(token);
    }

    @Override
    public UserModel findById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public void update(UserModel user) {
        userRepository.save(user);
    }

    @Override
    public UserModel FindByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
    
    @Override
	public void updatePassword(String email, String newPassword) {
		UserModel us = FindByEmail(email);
	    if (us != null) { // Kiểm tra nếu người dùng tồn tại
	        us.setPassword(newPassword);
	        userRepository.save(us); // Lưu thông tin người dùng đã được cập nhật vào cơ sở dữ liệu
	    }
	}
    
    @Override
	public List<UserModel> getalluser() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
		
	}

	@Override
	public void deleteuser(int id) {
		userRepository.deleteById(id);
	}

	@Override
	public void updateuser(String name, String phone, String email,int id,int status) {
		UserModel us = findById(id);
		us.setEmail(email);
		us.setFullname(name);
		us.setPhone(phone);
		us.setStatus(status);
		userRepository.save(us);
	}

	@Override
	public void changepassword(String Password,UserModel a) {
		
		a.setPassword(Password);
        userRepository.save(a);
	}

	@Override
	public void saveimg(String fileName, UserModel user) {
		
        user.setImages(fileName);
        userRepository.save(user);
		
	}

	@Override
	public boolean addUser(String username, String password, String email, String fullname, String phone,
			int status) {
		 if (checkExistEmail(email) || checkExistUsername(username) || checkExistPhone(phone)) {
	            return false;
	        }

	        UserModel user = new UserModel();
	        user.setUsername(username);
	        user.setPassword(password);  // Nên mã hóa mật khẩu trước khi lưu
	        user.setEmail(email);
	        user.setPhone(phone);
	        user.setFullname(fullname);
	        user.setStatus(status);
	        // Lưu người dùng vào cơ sở dữ liệu
	        userRepository.save(user);
	        return true;
	}

	

	
	
	
    
}
