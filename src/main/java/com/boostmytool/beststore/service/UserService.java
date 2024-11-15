package com.boostmytool.beststore.service;

import com.boostmytool.beststore.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public User findByUsername(String username);
    public void save(User user);

}
