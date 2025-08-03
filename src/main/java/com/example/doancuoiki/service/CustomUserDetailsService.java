package com.example.doancuoiki.service;

import com.example.doancuoiki.entity.User;
import com.example.doancuoiki.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        List<GrantedAuthority> authorities = new ArrayList<>();
        // Logic phân quyền: Nếu bạn có cột 'role' trong User entity
        // Ví dụ: if("ADMIN".equals(user.getRole())) {
        //            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        //        } else {
        //            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        //        }
        
        // Mặc định tất cả đều là USER
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}