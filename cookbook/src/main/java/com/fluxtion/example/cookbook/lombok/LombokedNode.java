package com.fluxtion.example.cookbook.lombok;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.builder.AssignToField;
import lombok.Value;

@Value
public class LombokedNode {

    @AssignToField("id")
    String id;
    @AssignToField("anotherId")
    String anotherId;

    public static void main(String[] args) {
        EventProcessor eventProcessor = Fluxtion.interpret(c -> {
            c.addNode(new LombokedNode("testId", "anotherThing"));
        });
        eventProcessor.init();
        eventProcessor.onEvent("hello world");
    }

    @OnEventHandler
    public boolean MyMarketDataEvent(String input) {
        System.out.println("received:" + input + " id:" + id + " anotherId:" + anotherId);
        return true;
    }
}
