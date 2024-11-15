package com.boostmytool.beststore.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import com.boostmytool.beststore.dao.UserRepository;
import com.boostmytool.beststore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService  {

    private UserRepository userRes;
    @Autowired
    public UserServiceImpl(UserRepository userRes) {
        super();
        this.userRes = userRes;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRes.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails
                .User(user.getUsername(), user.getPassword(),rolesToAuthorities(user.getRole()));
    }

    @Override
    public User findByUsername(String username) {
        return userRes.findByUsername(username);
    }

    @Override
    public void save(User user) {
        userRes.save(user);
    }

    private Collection<? extends GrantedAuthority> rolesToAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role));

        return authorities;
    }
}