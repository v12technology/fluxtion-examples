package com.fluxtion.example.cookbook.exportservice.node;

import com.fluxtion.example.cookbook.exportservice.data.CategoryUpdate;
import com.fluxtion.example.cookbook.exportservice.service.BankAccount;
import com.fluxtion.example.cookbook.exportservice.data.Transaction;
import com.fluxtion.example.cookbook.exportservice.service.DataStore;
import com.fluxtion.example.cookbook.exportservice.service.ReplaySink;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.NoPropagateFunction;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.callback.ExportFunctionNode;
import com.fluxtion.runtime.output.SinkPublisher;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BankAccountNode
        extends ExportFunctionNode
        implements
        @ExportService BankAccount,
        @ExportService ReplaySink {

    @Inject(instanceName = "responseSink")
    public SinkPublisher<String> sinkPublisher;
    private DataStore dataStore;


    @Override
    public void deposit(Transaction deposit) {
        log.info("deposit:{}", deposit);
        dataStore.commitTransaction(deposit);
    }

    @Override
    public void debit(Transaction debit) {
        log.info("debit:{}", debit);
        dataStore.commitTransaction(debit);
//        sinkPublisher.publish(debit.toString());
    }

    @Override
    public void setSpendingLimit(double amount) {

    }

    @Override
    @NoPropagateFunction
    public void replayStarted() {
    }

    @Override
    @NoPropagateFunction
    public void replayComplete() {

    }

    @Override
    @NoPropagateFunction
    public void categoryUpdate(CategoryUpdate categoryUpdate) {

    }

    @Override
    @NoPropagateFunction
    public void transactionUpdate(Transaction transaction) {

    }

    public void setDataStore(DataStore dataStore){
        this.dataStore = dataStore;
    }
}
