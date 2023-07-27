package com.fluxtion.example.cookbook.spring.node;

import com.fluxtion.example.cookbook.exportservice.service.DataStore;
import com.fluxtion.example.cookbook.spring.data.Transaction;
import com.fluxtion.example.cookbook.spring.service.BankingOperations;
import com.fluxtion.example.cookbook.spring.service.TransactionProcessor;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.callback.ExportFunctionNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class BankTransactionStore extends ExportFunctionNode implements @ExportService BankingOperations {
    private TransactionProcessor transactionSource;
    private boolean openForBusiness = false;

    @Override
    public boolean triggered() {
        Transaction transaction = transactionSource.currentTransactionRequest();
        if(transaction == null){
            return false;
        }
        if(openForBusiness){
            log.info("accepting transaction:{}", transaction);
            transactionSource.commitTransaction();
        }else{
            log.warn("rejecting transaction:{}", transaction);
            transactionSource.clearTransaction();
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
