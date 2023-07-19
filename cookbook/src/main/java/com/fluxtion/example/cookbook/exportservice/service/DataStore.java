package com.fluxtion.example.cookbook.exportservice.service;

import com.fluxtion.example.cookbook.exportservice.data.CategoryUpdate;
import com.fluxtion.example.cookbook.exportservice.data.Transaction;

public interface DataStore {
    void commitTransaction(Transaction transaction);

    void commitCategoryUpdate(CategoryUpdate transaction);

    void snapshotCategory();
}
