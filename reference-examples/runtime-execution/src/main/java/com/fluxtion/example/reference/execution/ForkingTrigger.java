package com.fluxtion.example.reference.execution;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class ForkingTrigger {

    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.printf("%s MyNode::handleStringEvent %n", Thread.currentThread().getName());
            return true;
        }
    }

    public static class ForkedChild {
        private final MyNode myNode;
        private final int id;

        public ForkedChild(MyNode myNode, int id) {
            this.myNode = myNode;
            this.id = id;
        }

        @OnTrigger(parallelExecution = true)
        public boolean triggered() {
            int millisSleep = new Random(id).nextInt(25, 200);
            String threadName = Thread.currentThread().getName();
            System.out.printf("%s ForkedChild[%d]::triggered - sleep:%d %n", threadName, id, millisSleep);
            try {
                Thread.sleep(millisSleep);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("%s ForkedChild[%d]::complete %n", threadName, id);
            return true;
        }
    }

    public static class ResultJoiner {
        private final ForkedChild[] forkedTasks;

        public ResultJoiner(ForkedChild[] forkedTasks) {
            this.forkedTasks = forkedTasks;
        }

        public ResultJoiner(int forkTaskNumber){
            MyNode myNode = new MyNode();
            forkedTasks = new ForkedChild[forkTaskNumber];
            for (int i = 0; i < forkTaskNumber; i++) {
                forkedTasks[i] = new ForkedChild(myNode, i);
            }
        }

        @OnTrigger
        public boolean complete(){
            System.out.printf("%s ResultJoiner:complete %n%n", Thread.currentThread().getName());
            return true;
        }
    }

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(new ResultJoiner(5));
        processor.init();

        Instant start = Instant.now();
        processor.onEvent("test");

        System.out.printf("duration: %d milliseconds%n", Duration.between(start, Instant.now()).toMillis());
    }
}
