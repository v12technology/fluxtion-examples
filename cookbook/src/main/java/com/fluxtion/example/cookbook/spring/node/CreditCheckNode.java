package com.fluxtion.example.cookbook.spring.node;

import com.fluxtion.example.cookbook.spring.data.Transaction;
import com.fluxtion.example.cookbook.spring.service.CreditCheck;
import com.fluxtion.example.cookbook.spring.service.TransactionProcessor;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.NoPropagateFunction;
import com.fluxtion.runtime.callback.ExportFunctionNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Data
@Slf4j
public class CreditCheckNode extends ExportFunctionNode implements @ExportService CreditCheck, TransactionProcessor {

    private transient Set<Integer> blackListedAccounts = new HashSet<>();
    private TransactionProcessor transactionSource;
    private ResponsePublisher responsePublisher;

    @Override
    @NoPropagateFunction
    public void blackListAccount(int accountNumber) {
        log.info("credit check blacklisted:{}", accountNumber);
        blackListedAccounts.add(accountNumber);
    }

    @Override
    @NoPropagateFunction
    public void whiteListAccount(int accountNumber) {
        log.info("credit check whitelisted:{}", accountNumber);
        blackListedAccounts.remove(accountNumber);
    }

    public boolean propagateParentNotification(){
        Transaction transaction = transactionSource.currentTransactionRequest();
        int accountNumber = transaction.accountNumber();
        if(blackListedAccounts.contains(accountNumber)){
            log.warn("credit check failed");
            transactionSource.rollbackTransaction();
            responsePublisher.rejectTransaction(transaction);
            return false;
        }
        log.info("credit check passed");
        return true;
    }

    @Override
    public Transaction currentTransactionRequest() {
        return transactionSource.currentTransactionRequest();
    }

    @Override
    public void rollbackTransaction() {
        transactionSource.rollbackTransaction();
    }

    public void commitTransaction(){
        transactionSource.commitTransaction();
    }
}
