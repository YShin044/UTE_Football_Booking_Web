package com.example.doancuoiki.service;


import java.util.List;

import com.example.doancuoiki.model.UserModel;

public interface IUserServices {

	UserModel login (String username, String password);
	
	
	UserModel FindByUserName(String username);
	
	void insert(UserModel user);
	
	boolean register(String username, String password,String email,  String fullname, String phone);
	
	boolean checkExistEmail(String email);
	
	boolean checkExistUsername(String username);
	
	boolean checkExistPhone(String phone);

	String generateResetToken(String email);

	boolean isValidToken(String token);

	UserModel findById(int userId);

	void update(UserModel user);

	UserModel FindByEmail(String email);

	void updatePassword(String email, String newPassword);
	
	public void sendmail(String tomail,String subject,String body);


	void deleteuser(int id);


	void updateuser(String name, String phone, String email, int id, int status);

	List<UserModel> getalluser();
	
	void changepassword(String Password, UserModel a);


	void saveimg(String fileName, UserModel user);


	boolean addUser(String username, String password, String email, String fullname, String phone, int status);


	
}
