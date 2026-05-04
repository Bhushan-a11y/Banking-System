package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service

public class UserDetailsServiceImplementation implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if(user==null){
            throw new UsernameNotFoundException("UserName not found");
        }
        return org.springframework.security.core.userdetails.User.builder()
        .password(user.getPassword())
        .username(user.getUserName())
        .roles(user.getRoles().toArray(new String[0]))
        .build();
        
    }

}