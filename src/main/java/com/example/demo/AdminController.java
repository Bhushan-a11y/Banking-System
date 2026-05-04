package com.example.demo;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.UserController.InnerUserController;
import com.example.demo.UserController.Userdetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
public class AdminController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @PostMapping("/admin/accounts/create-account")
public ResponseEntity<?>createAccount(@RequestBody Account account){
    boolean bool = accountService.createAccount(account);
    if(bool){
        return new ResponseEntity<>(account,HttpStatus.CREATED);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}
@GetMapping("/admin/accounts/get-all-accounts")
public ResponseEntity<?>getAllAccounts(){
    List<Account> all = accountService.getAllAccounts();
    if(all!=null && !all.isEmpty()){
        return new ResponseEntity<>(all,HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    
    
}
@PostMapping("admin/users/create-a-user")
    public ResponseEntity<?> createUser(@RequestBody Userdetail innerUserController){
        User user = userService.createUser(innerUserController);
        if (user!=null) {
            return new ResponseEntity<>(user,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
     @GetMapping("admin/get-all-users")
    public ResponseEntity<?> getAllUsers()
    {
        List<User> allusers = userService.getAllUsers();
        if(allusers!=null&&!allusers.isEmpty()){
            return new ResponseEntity<>(allusers,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @Data@AllArgsConstructor@NoArgsConstructor
     public static class Admindetail{
        String adminName;
        String password;
        Long accountId;
    }

    @PostMapping("/admin/create-admin")
        public ResponseEntity<?> createAdmin(@RequestBody Admindetail user){
            User ADMIN = userService.createAdmin(user);
            if (ADMIN!=null) {
            return new ResponseEntity<>(ADMIN,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        }
    }


