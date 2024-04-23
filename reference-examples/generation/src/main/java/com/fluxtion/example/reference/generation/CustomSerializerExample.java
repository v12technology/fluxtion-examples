package com.fluxtion.example.reference.generation;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.generation.serialiser.FieldContext;
import com.fluxtion.runtime.annotations.OnEventHandler;

public class CustomSerializerExample {

    public static void main(String[] args) {
        Fluxtion.compile(
                cfg -> {
                    cfg.addNode(new StringHandler(MyThing.newThing("my instance param")));
                    cfg.addClassSerializer(MyThing.class, CustomSerializerExample::customSerialiser);
                },
                compilerConfig -> {
                    compilerConfig.setClassName("CustomSerializerExampleProcessor");
                    compilerConfig.setPackageName("com.fluxtion.example.reference.generation.genoutput");
                });
    }

    public static String customSerialiser(FieldContext<MyThing> fieldContext) {
        fieldContext.getImportList().add(MyThing.class);
        MyThing myThing = fieldContext.getInstanceToMap();
        return "MyThing.newThing(\"" + myThing.getID() + "\")";
    }

    public static class MyThing {
        private final String iD;

        private MyThing(String iD) {
            this.iD = iD;
        }

        public String getID() {
            return iD;
        }

        public static MyThing newThing(String in) {
            return new MyThing(in);
        }
    }


    public static class StringHandler {
        private final Object delegate;

        public StringHandler(Object delegate) {
            this.delegate = delegate;
        }

        @OnEventHandler
        public boolean onEvent(MyThing event) {
            return false;
        }

    }
}
