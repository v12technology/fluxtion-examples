package com.fluxtion.example.cookbook.spring.node;

import com.fluxtion.example.cookbook.spring.data.Transaction;
import com.fluxtion.example.cookbook.spring.service.BankingOperations;
import com.fluxtion.example.cookbook.spring.service.DataStore;
import com.fluxtion.example.cookbook.spring.service.TransactionProcessor;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.NoPropagateFunction;
import com.fluxtion.runtime.annotations.Start;
import com.fluxtion.runtime.callback.ExportFunctionNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class CentralTransactionProcessor extends ExportFunctionNode implements @ExportService BankingOperations {
    private TransactionProcessor transactionSource;
    private boolean openForBusiness = false;
    private ResponsePublisher responsePublisher;
    private DataStore dataStore = new InMemoryDataStore();

    @Override
    public boolean propagateParentNotification() {
        Transaction transaction = transactionSource.currentTransactionRequest();
        if(openForBusiness){
            log.info("accept bank open");
            dataStore.commitTransaction(transaction);
            transactionSource.commitTransaction();
            responsePublisher.acceptTransaction(transaction);
        }else{
            log.warn("reject bank closed");
            transactionSource.rollbackTransaction();
            responsePublisher.rejectTransaction(transaction);
        }
        return openForBusiness;
    }

    @Override
    @NoPropagateFunction
    public void openForBusiness() {
        log.info("open accepting transactions");
        openForBusiness = true;
    }

    @Override
    @NoPropagateFunction
    public void closedForBusiness() {
        log.warn("closed rejecting all transactions");
        openForBusiness = false;
    }

    @Override
    @NoPropagateFunction
    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Start
    public void startProcessor(){
        if(dataStore == null){
            throw new RuntimeException("cannot start without a valid DataStore instance");
        }
    }
}
