package com.fluxtion.example.reference.generation;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

public class DispatchOnlySample {

    public static void main(String[] args) {
        Map<String, String> lookupMap = new HashMap<>();
        MyStringHandler myStringHandler = new MyStringHandler();
        myStringHandler.setLookupMap(lookupMap);
        myStringHandler.prefix = "default prefix";

        var processor = Fluxtion.compileDispatcher(myStringHandler);
        processor.init();

        //no lookup match for input string
        myStringHandler.setPrefix("RECEIVED -> ");
        processor.onEvent("hello");

        //set a lookup to match
        lookupMap.put("hello", "good morning!!");
        processor.onEvent("hello");
    }

    @Data
    public static class MyStringHandler{
        private String prefix;
        private Map<String, String> lookupMap;

        @OnEventHandler
        public boolean onString(String string){
            String lookup = lookupMap.getOrDefault(string, "NO LOOKUP VALUE");
            System.out.printf("%s '%s' lookup : '%s'%n", prefix, string, lookup);
            return true;
        }
    }
}
