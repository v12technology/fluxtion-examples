package com.fluxtion.example.cookbook.spring.node;

import com.fluxtion.example.cookbook.spring.data.Transaction;
import com.fluxtion.example.cookbook.spring.service.CreditCheck;
import com.fluxtion.example.cookbook.spring.service.TransactionProcessor;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.NoPropagateFunction;
import com.fluxtion.runtime.annotations.OnTrigger;
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

    @Override
    @NoPropagateFunction
    public void blackListAccount(int accountNumber) {
        blackListedAccounts.add(accountNumber);
    }

    @Override
    @NoPropagateFunction
    public void whiteListAccount(int accountNumber) {
        blackListedAccounts.remove(accountNumber);
    }

    public boolean triggered(){
        Transaction transaction = transactionSource.currentTransactionRequest();
        if(transaction == null){
            return false;
        }
        int accountNumber = transaction.accountNumber();
        if(blackListedAccounts.contains(accountNumber)){
            log.warn("FAILED CREDIT CHECK - {}", transaction);
            transactionSource.clearTransaction();
            return false;
        }
        log.info("passed credit check:{}", transaction);
        return true;
    }

    @Override
    public Transaction currentTransactionRequest() {
        return transactionSource.currentTransactionRequest();
    }

    @Override
    public void clearTransaction() {
        transactionSource.clearTransaction();
    }
}
