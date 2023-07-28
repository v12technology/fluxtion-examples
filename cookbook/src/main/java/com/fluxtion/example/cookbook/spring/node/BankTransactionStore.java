package com.fluxtion.example.cookbook.spring.node;

import com.fluxtion.example.cookbook.spring.data.Transaction;
import com.fluxtion.example.cookbook.spring.service.BankingOperations;
import com.fluxtion.example.cookbook.spring.service.TransactionProcessor;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.callback.ExportFunctionNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class BankTransactionStore extends ExportFunctionNode implements @ExportService BankingOperations {
    private TransactionProcessor transactionSource;
    private boolean openForBusiness = false;
    private TransactionResponsePublisher transactionResponsePublisher;

    @Override
    public boolean propagateParentNotification() {
        Transaction transaction = transactionSource.currentTransactionRequest();
        if(openForBusiness){
            log.info("OPEN accept:{}", transaction);
            transactionSource.commitTransaction();
            transactionResponsePublisher.acceptTransaction(transaction);
        }else{
            log.warn("CLOSED reject:{}", transaction);
            transactionSource.clearTransaction();
            transactionResponsePublisher.rejectTransaction(transaction);
        }
        return openForBusiness;
    }

    @Override
    public void openForBusiness() {
        log.info("open for business");
        openForBusiness = true;
    }

    @Override
    public void closedForBusiness() {
        log.warn("closed for business");
        openForBusiness = false;
    }
}
