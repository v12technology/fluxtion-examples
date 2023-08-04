package com.fluxtion.example.cookbook.spring.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fluxtion.example.cookbook.spring.data.Transaction;
import com.fluxtion.example.cookbook.spring.service.DataStore;
import lombok.SneakyThrows;

import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class FileDataStore implements DataStore {

    private final transient SequenceWriter transactionJsonWriter;
    private final Path transactionStore;

    @SneakyThrows
    public FileDataStore(Path rootStoreDir) {
        transactionStore = rootStoreDir.resolve("transaction.jsonl");
        Files.createDirectories(rootStoreDir);
        Writer transactionWriter = new FileWriter(transactionStore.toFile(), true);
        if(Files.size(transactionStore) != 0){
            transactionWriter.write("\n");
        }
        ObjectMapper mapper = new ObjectMapper();
        transactionJsonWriter = mapper.writer().withRootValueSeparator("\n").writeValues(transactionWriter);
    }

    @SneakyThrows
    public void replay(Consumer<Transaction> replaySink){
        ObjectMapper mapper = new ObjectMapper();
        Files.lines(transactionStore)
                .map(s -> {
                    try {
                        return mapper.readValue(s, Transaction.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .forEach(replaySink);
    }

    @SneakyThrows
    @Override
    public void commitTransaction(Transaction transaction) {
        transactionJsonWriter.write(transaction);
        transactionJsonWriter.flush();
    }

}
