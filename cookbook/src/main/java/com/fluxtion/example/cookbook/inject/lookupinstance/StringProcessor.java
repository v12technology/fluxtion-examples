package com.fluxtion.example.cookbook.inject.lookupinstance;

import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.TearDown;
import com.fluxtion.runtime.annotations.builder.Inject;

import java.util.List;
import java.util.function.Supplier;

public class StringProcessor {
    @Inject
    public EventProcessorContext context;
    private List<String> upperCaseStrings;
    private List<String> lowerCaseStrings;
    private List<String> mixedCaseStrings;
    private Supplier<String> titleSupplier;


    @OnEventHandler
    public boolean onString(String in) {
        if (in.toUpperCase().equals(in)) {
            upperCaseStrings.add(in);
        } else if (in.toLowerCase().equals(in)) {
            lowerCaseStrings.add(in);
        } else {
            mixedCaseStrings.add(in);
        }
        return true;
    }

    @Initialise
    @SuppressWarnings("unchecked")
    public void init() {
        titleSupplier = context.getInjectedInstance(Supplier.class);
        upperCaseStrings = context.getInjectedInstance(List.class, "upper");
        lowerCaseStrings = context.getInjectedInstance(List.class, "lower");
        mixedCaseStrings = context.getInjectedInstance(List.class, "mixed");
    }

    @TearDown
    public void tearDown() {
        System.out.printf("""
                
                %s
                ---------------------------------------------------------------------
                upper count: %d
                lower count: %d
                mixed count: %d
                """.formatted(
                titleSupplier.get(),
                upperCaseStrings.size(),
                lowerCaseStrings.size(),
                mixedCaseStrings.size()
        ));
    }
}
