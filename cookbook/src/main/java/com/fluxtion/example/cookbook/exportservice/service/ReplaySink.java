package com.fluxtion.example.cookbook.exportservice.service;

import com.fluxtion.example.cookbook.exportservice.data.CategoryUpdate;
import com.fluxtion.example.cookbook.exportservice.data.Transaction;
import com.fluxtion.runtime.annotations.NoPropagateFunction;

public interface ReplaySink {

    void replayStarted();

    void replayComplete();

    void categoryUpdate(CategoryUpdate categoryUpdate);

    void transactionUpdate(Transaction transaction);
}
