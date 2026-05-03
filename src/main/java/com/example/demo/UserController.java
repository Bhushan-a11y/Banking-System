package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers()
    {
        List<User> allusers = userService.getAllUsers();
        if(allusers!=null&&!allusers.isEmpty()){
            return new ResponseEntity<>(allusers,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Data@NoArgsConstructor
    @AllArgsConstructor
    public static class InnerUserController {
        String userName;
        Long userAccountId;
        String password;
    
        
    }
    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody InnerUserController innerUserController){
        User user = userService.createUser(innerUserController);
        if (user!=null) {
            return new ResponseEntity<>(user,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
