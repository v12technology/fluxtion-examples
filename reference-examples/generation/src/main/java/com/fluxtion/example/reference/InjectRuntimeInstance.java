package com.fluxtion.example.reference;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.node.InstanceSupplier;

import java.util.Date;

public class InjectRuntimeInstance {

    public static class MyNode{
        @Inject(instanceName = "startData")
        public InstanceSupplier<Date> myDate;

        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("runtime injected:" + myDate.get());
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new MyNode());
        processor.injectNamedInstance(new Date(1000000), "startData");

        processor.init();
        processor.onEvent("TEST");

        processor.injectNamedInstance(new Date(999000000), "startData");
        processor.onEvent("TEST");
    }

}
