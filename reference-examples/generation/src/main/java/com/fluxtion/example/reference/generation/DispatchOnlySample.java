package com.fluxtion.example.reference.generation;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import lombok.Data;

import java.io.StringWriter;

public class DispatchOnlySample {

    public static void main(String[] args) {
        MyStringHandler myStringHandler = new MyStringHandler();

        StringWriter stringWriter = new StringWriter();
        var processor = Fluxtion.compileDispatcher(c -> c.addNode(myStringHandler), stringWriter);
        processor.init();

        String separator = "-".repeat(60);
        System.out.printf("GENERATION START \n%s \n%s\n%1$s\nGENERATION END \n%1$s\n\n", separator, stringWriter);

        myStringHandler.setPrefix("RECEIVED -> ");
        processor.onEvent("hello");
    }

    @Data
    public static class MyStringHandler{
        private String string;
        private String prefix;

        @OnEventHandler
        public boolean onString(String string){
            this.string = string;
            System.out.println(prefix + string);
            return true;
        }
    }
}
