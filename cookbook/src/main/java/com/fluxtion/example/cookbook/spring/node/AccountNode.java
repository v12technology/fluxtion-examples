package com.fluxtion.example.cookbook.spring.node;

import com.fluxtion.example.cookbook.spring.data.Transaction;
import com.fluxtion.example.cookbook.spring.service.Account;
import com.fluxtion.example.cookbook.spring.service.TransactionProcessor;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.callback.ExportFunctionNode;
import lombok.ToString;

@ToString
public class AccountNode extends ExportFunctionNode implements @ExportService Account, TransactionProcessor {

    private Transaction currentTransaction;
    private TransactionResponsePublisher transactionResponsePublisher;

    @Override
    public void debit(int accountNumber, double debitAmount) {
        currentTransaction = new Transaction(accountNumber, debitAmount, true);
    }

    @Override
    public void credit(int accountNumber, double creditAmount) {
        currentTransaction = new Transaction(accountNumber, creditAmount, true);
    }

    @Override
    public Transaction currentTransactionRequest() {
        return currentTransaction;
    }

    public void clearTransaction() {
        currentTransaction = null;
    }

    public void commitTransaction(){
        //add to the account
        currentTransaction = null;
    }

    public TransactionResponsePublisher getTransactionResponsePublisher() {
        return transactionResponsePublisher;
    }

    public void setTransactionResponsePublisher(TransactionResponsePublisher transactionResponsePublisher) {
        this.transactionResponsePublisher = transactionResponsePublisher;
    }
}
