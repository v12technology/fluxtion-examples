package com.fluxtion.example.cookbook.inject.lookupinstance;

import com.fluxtion.compiler.Fluxtion;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

public class Main {

    public static void main(String[] args) {
        var stringProcessor = Fluxtion.interpret(c -> c.addNode(new StringProcessor()));
        stringProcessor.injectInstance(Main::title, Supplier.class);
        stringProcessor.injectNamedInstance(new ArrayList<>(), List.class, "upper");
        stringProcessor.injectNamedInstance(new ArrayList<>(), List.class, "lower");
        stringProcessor.injectNamedInstance(new ArrayList<>(), List.class, "mixed");
        //init
        stringProcessor.init();
        //send data
        stringProcessor.onEvent("test");
        stringProcessor.onEvent("Test");
        stringProcessor.onEvent("START");
        stringProcessor.onEvent("BILL");
        stringProcessor.onEvent("mixedCase");
        //teardown
        stringProcessor.tearDown();
    }

    private static String title(){
        return "StringSplit execution time: " + new Date();
    }
}
