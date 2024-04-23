package com.fluxtion.example.reference.generation;

import com.fluxtion.compiler.extern.spring.FluxtionSpring;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringConfigAdd {

    public static class MyNode {
        @OnEventHandler
        public boolean handleStringEvent(String stringToProcess) {
            System.out.println("MyNode::received:" + stringToProcess);
            return true;
        }
    }

    public static class Root1 {
        private final MyNode myNode;

        public Root1(MyNode myNode) {
            this.myNode = myNode;
        }

        @OnTrigger
        public boolean trigger() {
            System.out.println("Root1::triggered");
            return true;
        }
    }

    public static void main(String[] args) {
        FluxtionSpring.compileAot(
                new ClassPathXmlApplicationContext("com/fluxtion/example/reference/spring-example.xml"),
                fluxtionCompilerConfig -> {
                    fluxtionCompilerConfig.setClassName("SpringExampleProcessor");
                    fluxtionCompilerConfig.setPackageName("com.fluxtion.example.reference.generation.genoutput");
                });
    }
}
