package com.example.demo;


import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@CrossOrigin(origins = "http://localhost:3000")

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
    @Data
    @NoArgsConstructor
    @AllArgsConstructor

      public static class UserdetailWithoutPassword{
        String userName;        
        Account account;
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
            UserdetailWithoutPassword userdetailWithoutPassword = new UserdetailWithoutPassword();
            userdetailWithoutPassword.setUserName(user.getUserName());
            userdetailWithoutPassword.setAccount(user.getUserAccount());
            return new ResponseEntity<>(userdetailWithoutPassword,HttpStatus.OK);
           }
           else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
           }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
    
    @GetMapping("/users/get-a-user")
    public ResponseEntity<?> getUsers(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userRepository.findByUserName(userName);
        if(user!=null){
        HashMap<String, Object> map = new HashMap<>();
        Account account = user.getUserAccount();
        map.put("balance", account.getBalance());
        map.put("accountNo", account.getAccno());
        map.put("userName", user.getUserName());
        map.put("accId", account.getAccid());
        return new ResponseEntity<>(map,HttpStatus.OK);
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
    @GetMapping("/users/verify-login")
    public ResponseEntity<?> verifyUserLogin() {
        // If Spring Security lets them reach this line, they are legit!
        return new ResponseEntity<>(HttpStatus.OK);
    }
    

}
