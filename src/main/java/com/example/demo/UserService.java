package com.example.demo;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.AdminController.Admindetail;
import com.example.demo.UserController.Userdetail;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(Userdetail innerUserController) {
        User user = new User();
        user.setUserName(innerUserController.getUserName());
        user.setPassword(passwordEncoder.encode(innerUserController.getPassword()) );
        Account userAccount = accountRepository.findById(innerUserController.getAccountId()).orElse(null);
        user.setUserAccount(userAccount);
        user.setRoles(Arrays.asList("USER"));
        userRepository.save(user);
        return user;
    }

    public User createAdmin(Admindetail user) {
        User admin = new User();
         admin.setUserName(user.getAdminName());
        admin.setPassword(passwordEncoder.encode(user.getPassword()) );
        Account userAccount = accountRepository.findById(user.getAccountId()).orElse(null);
        admin.setUserAccount(userAccount);
        admin.setRoles(Arrays.asList("ADMIN"));
        userRepository.save(admin);
        return admin;
     
    }
    public void saveUpdatedUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

  

}
