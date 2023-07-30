package com.fluxtion.example.cookbook.spring.node;

import com.fluxtion.example.cookbook.spring.data.Transaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponsePublisher {

    public void rejectTransaction(Transaction transaction){
        log.info("response reject:{}", transaction);
    }

    public void acceptTransaction(Transaction transaction){
        log.info("response accept:{}", transaction);
    }

    public void publishBalance(int accountNumber, double balance){
        log.info("account:{}, balance:{}", accountNumber, balance);
    }
}
