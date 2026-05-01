package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    public List<Transaction> findBySenderAccountOrReceiverAccount(Account sender,Account reciever);

}
