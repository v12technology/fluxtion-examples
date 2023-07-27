package com.fluxtion.example.cookbook.exportservice.node;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fluxtion.example.cookbook.exportservice.data.CategoryUpdate;
import com.fluxtion.example.cookbook.exportservice.data.Transaction;
import com.fluxtion.example.cookbook.exportservice.service.DataStore;
import lombok.SneakyThrows;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDataStore implements DataStore {

    private transient Writer transactionWriter;
    private transient Writer categoryWriter;
    private transient SequenceWriter transactionJsonWriter;
    private transient SequenceWriter categoryJsonWriter;

    @SneakyThrows
    public FileDataStore(Path storageDirectory) {
        Path rootStoreDir = Paths.get("src/main/resources/exportservice");
        Path transactionStore = rootStoreDir.resolve("transaction.jsonl");
        Path categoryStore = rootStoreDir.resolve("category.jsonl");
        Files.createDirectories(rootStoreDir);

        Writer transactionWriter = new FileWriter(transactionStore.toFile(), true);
        Writer categoryWriter = new FileWriter(categoryStore.toFile(), true);
        ObjectMapper mapper = new ObjectMapper();
        transactionJsonWriter = mapper.writer().withRootValueSeparator("\n").writeValues(transactionWriter);
        categoryJsonWriter = mapper.writer().withRootValueSeparator("\n").writeValues(categoryWriter);
    }

    @SneakyThrows
    @Override
    public void commitTransaction(Transaction transaction) {
        transactionJsonWriter.write(transaction).flush();
    }

    @SneakyThrows
    @Override
    public void commitCategoryUpdate(CategoryUpdate transaction) {
        categoryJsonWriter.write(transaction);
    }

    @Override
    public void snapshotCategory() {

    }
}
