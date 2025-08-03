package com.example.doancuoiki.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.doancuoiki.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {

	
	Optional<UserModel> findByUsername(String username);

    Optional<UserModel> findByEmail(String email);
    
    
    List<UserModel> findAll();
    
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByPhone(String phone);

    boolean existsByResetToken(String resetToken);
	
	
}
