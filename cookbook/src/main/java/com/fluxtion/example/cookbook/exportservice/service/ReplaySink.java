package com.fluxtion.example.cookbook.exportservice.service;

import com.fluxtion.example.cookbook.exportservice.data.CategoryUpdate;
import com.fluxtion.example.cookbook.exportservice.data.Transaction;
import com.fluxtion.runtime.annotations.NoPropagateFunction;

public interface ReplaySink {

    @NoPropagateFunction
    void replayStarted();

    @NoPropagateFunction
    void replayComplete();

    @NoPropagateFunction
    void categoryUpdate(CategoryUpdate categoryUpdate);

    @NoPropagateFunction
    void transactionUpdate(Transaction transaction);
}
