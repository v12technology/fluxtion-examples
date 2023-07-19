package com.fluxtion.example.cookbook.exportservice.node;

import com.fluxtion.example.cookbook.exportservice.data.CategoryUpdate;
import com.fluxtion.example.cookbook.exportservice.service.SpendingMonitor;
import com.fluxtion.example.cookbook.exportservice.data.Transaction;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.callback.ExportFunctionNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpendingMonitorNode extends ExportFunctionNode implements @ExportService SpendingMonitor {
    @Inject(singleton = true)
    public TransactionStore transactionStore;

    @Override
    public void addBucket(String name) {

    }

    @Override
    public void assignToBucket(String userAccountName, String accountToGroup, String category) {
        transactionStore.commitCategoryUpdate(new CategoryUpdate(category, userAccountName, accountToGroup, false));
    }

    @Override
    public void resetBucket(String bucketName) {

    }

    @Override
    public void debit(Transaction debit) {
        log.info("debit:{}", debit);
    }
}
