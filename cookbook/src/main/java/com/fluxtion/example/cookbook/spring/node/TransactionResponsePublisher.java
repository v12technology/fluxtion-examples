package com.fluxtion.example.cookbook.spring.node;

import com.fluxtion.example.cookbook.spring.data.Transaction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionResponsePublisher {

    public void rejectTransaction(Transaction transaction){
        log.info("PUBLISH REJECT - {}", transaction);
    }

    public void acceptTransaction(Transaction transaction){
        log.info("PUBLISH ACCEPT - {}", transaction);
    }
}
