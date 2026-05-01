package com.example.demo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TransactionService {
    @Autowired 
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    public String transferAmount(Long senderId,Long receiverId,BigDecimal amount){
        Account sender  = accountRepository.findById(senderId).orElse(null);
        Account reciver = accountRepository.findById(receiverId).orElse(null);
        if(sender.getBalance().compareTo(amount)<0){
            throw new RuntimeException("Insufficient Bank Balance");
        }
        sender.setBalance(sender.getBalance().subtract(amount));
        reciver.setBalance(reciver.getBalance().add(amount));
        Transaction transaction = new Transaction();
        transaction.setTransAmount(amount);
        transaction.setReceiverAccount(reciver);
        transaction.setSenderAccount(sender);
        transaction.setMadeAT(LocalDateTime.now());
        
        accountRepository.save(sender);
        accountRepository.save(reciver);
        
        transactionRepository.save(transaction);

        return "Transaction Successful";




    }
    public List<Transaction> getHistory(Long accId){
        Account account = accountRepository.findById(accId).orElse(null);
        return transactionRepository.findBySenderAccountOrReceiverAccount(account, account);
    }

}
