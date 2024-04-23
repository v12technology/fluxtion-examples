package com.fluxtion.example.reference.binding;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;

public class FunctionalBindInstanceFunction {

    public static class PrefixString{
        private final String prefix;

        public PrefixString(String prefix) {
            this.prefix = prefix;
        }

        public String addPrefix(String input){
            return prefix + input;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(cfg -> {
            DataFlow.subscribe(String.class)
                    .console("input: '{}'")
                    .map(new PrefixString("XXXX")::addPrefix)
                    .console("transformed: '{}'");
        });

        processor.init();
        processor.onEvent("hello world");
    }
}
