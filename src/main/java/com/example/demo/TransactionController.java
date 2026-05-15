package com.example.demo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Transfer{//DTO-> Data Transfer Object
    Long recieverId;
    BigDecimal amount;
    
}
@PostMapping("/transaction")
public ResponseEntity<?> transferAmount(@RequestBody Transfer transfer){
    try {
        transactionService.transferAmount(transfer.getRecieverId(),transfer.getAmount());
        return new ResponseEntity<>(HttpStatus.OK);
        
    } catch (Exception e) {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }
    
}
@Data
@AllArgsConstructor@NoArgsConstructor
public static class WD{
    BigDecimal amount;
}
@PostMapping("/transaction/deposit")
public ResponseEntity<?> depositAmount(@RequestBody WD wd){

    try{
        transactionService.deposit(wd.getAmount());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    catch(Exception e){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

@PostMapping("/transaction/withdrawal")
public ResponseEntity<?> withdrawAmount(@RequestBody WD wd){

    try{
        transactionService.withdrawal(wd.getAmount());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    catch(Exception e){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}


@GetMapping("/transaction/{accId}")
public ResponseEntity<?> getTransactionHistory(@PathVariable Long accId){
    List<Transaction> trans = transactionService.getHistory(accId);
    if(trans!=null&&!trans.isEmpty()){
        return new ResponseEntity<>(trans,HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}
}