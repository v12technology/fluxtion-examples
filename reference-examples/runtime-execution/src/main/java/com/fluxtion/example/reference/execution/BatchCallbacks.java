package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnBatchEnd;
import com.fluxtion.runtime.annotations.OnBatchPause;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.lifecycle.BatchHandler;

public class BatchCallbacks {

    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("MyNode event received:" + stringToProcess);
            return true;
        }

        @OnBatchPause
        public void batchPause(){
            System.out.println("MyNode::batchPause");
        }

        @OnBatchEnd
        public void batchEnd(){
            System.out.println("MyNode::batchEnd");
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new MyNode());
        processor.init();

        processor.onEvent("test");

        //use BatchHandler service
        BatchHandler batchHandler = (BatchHandler)processor;
        batchHandler.batchPause();
        batchHandler.batchEnd();
    }
}
