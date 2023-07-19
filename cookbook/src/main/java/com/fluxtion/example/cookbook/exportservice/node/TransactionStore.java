package com.fluxtion.example.cookbook.exportservice.node;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fluxtion.example.cookbook.exportservice.data.CategoryUpdate;
import com.fluxtion.example.cookbook.exportservice.data.Transaction;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.TearDown;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.node.InstanceSupplier;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.Writer;

@Slf4j
public class TransactionStore {

    public static final String CATEGORY_WRITER = "categoryWriter";
    public static final String TRANSACTION_WRITER = "transactionWriter";
    @Inject(instanceName = TRANSACTION_WRITER)
    public InstanceSupplier<Writer> transactionWriterSupplier;
    @Inject(instanceName = CATEGORY_WRITER)
    public InstanceSupplier<Writer> categoryWriterSupplier;
    private transient Writer transactionWriter;
    private transient Writer categoryWriter;
    private transient SequenceWriter transactionJsonWriter;
    private transient SequenceWriter categoryJsonWriter;

    @SneakyThrows
    @Initialise
    public void init() {
        transactionWriter = transactionWriterSupplier.get();
        categoryWriter = categoryWriterSupplier.get();
        ObjectMapper mapper = new ObjectMapper();
        transactionJsonWriter = mapper.writer().withRootValueSeparator("\n").writeValues(transactionWriter);
        categoryJsonWriter = mapper.writer().withRootValueSeparator("\n").writeValues(categoryWriter);
    }

    @SneakyThrows
    @TearDown
    public void tearDown(){
        transactionJsonWriter.flush();
        transactionWriter.write('\n');
        transactionWriter.flush();
        categoryJsonWriter.flush();
        categoryWriter.write('\n');
        categoryWriter.flush();
    }

    @SneakyThrows
    public void commitTransaction(Transaction transaction){
        log.info("commitTransaction:{}", transaction);
        transactionJsonWriter.write(transaction).flush();
    }

    @SneakyThrows
    public void commitCategoryUpdate(CategoryUpdate transaction){
        log.info("commitCategoryUpdate:{}", transaction);
        categoryJsonWriter.write(transaction).flush();
    }

}
