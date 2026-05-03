package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.UserController.InnerUserController;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(InnerUserController innerUserController) {
        User user = new User();
        user.setUserName(innerUserController.getUserName());
        user.setPassword(innerUserController.getPassword());
        Account userAccount = accountRepository.findById(innerUserController.getUserAccountId()).orElse(null);
        user.setUserAccount(userAccount);
        userRepository.save(user);
        return user;
    }

  

}
