package com.example.demo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;
@GetMapping("/accounts/get-all-accounts")
public ResponseEntity<?>getAllAccounts(){
    List<Account> all = accountService.getAllAccounts();
    if(all!=null && !all.isEmpty()){
        return new ResponseEntity<>(all,HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    
    
}
@PostMapping("/accounts/create-account")
public ResponseEntity<?>createAccount(@RequestBody Account account){
    boolean bool = accountService.createAccount(account);
    if(bool){
        return new ResponseEntity<>(account,HttpStatus.CREATED);
    }
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}
}