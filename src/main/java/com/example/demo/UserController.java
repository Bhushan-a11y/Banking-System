package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AccountRepository accountRepository;
   @Autowired
   UserRepository userRepository;
    @Data@NoArgsConstructor
    @AllArgsConstructor
    public static class InnerUserController {
        Long accno;      
    }
    @Data
    @NoArgsConstructor@AllArgsConstructor
    public static class Userdetail{
        String userName;
        String password;
        Long accountId;
    }

    @GetMapping("/users/{accountNo}")
    public ResponseEntity<?> getUser(@PathVariable Long accountNo){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName);


        Account account = accountRepository.findByAccno(accountNo).orElse(null);
        if(account!=null){
           Boolean bool= user.getUserAccount().equals(account);
           if(bool){
            return new ResponseEntity<>(user,HttpStatus.OK);
           }
           else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
           }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
    @PostMapping("/users/create-a-user")
    public ResponseEntity<?> createUser(@RequestBody Userdetail innerUserController){
        User user = userService.createUser(innerUserController);
        if (user!=null) {
            return new ResponseEntity<>(user,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    

}
