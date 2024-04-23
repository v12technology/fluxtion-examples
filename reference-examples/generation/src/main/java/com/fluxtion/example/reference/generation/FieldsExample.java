package com.fluxtion.example.reference.generation;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.builder.AssignToField;
import com.fluxtion.runtime.node.NamedNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class FieldsExample {

    public enum SampleEnum {VAL1, VAL2, VALX}

    public static void main(String[] args) {
        Fluxtion.compileAot("com.fluxtion.example.reference.generation.genoutput", "FieldsExampleProcessor",
                BasicTypeHolder.builder()
                        .cId("cid")
                        .name("holder")
                        .myChar('$')
                        .longVal(2334L)
                        .intVal(12)
                        .shortVal((short) 45)
                        .byteVal((byte) 12)
                        .doubleVal(35.8)
                        .doubleVal2(Double.NaN)
                        .floatVal(898.24f)
                        .boolean1Val(true)
                        .boolean2Val(false)
                        .classVal(String.class)
                        .enumVal(SampleEnum.VAL2)
                        .build());
    }

    @Data
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class BasicTypeHolder implements NamedNode {
        private String name;
        @AssignToField("cId")
        private final String cId;
        private char myChar;
        private long longVal;
        private int intVal;
        private short shortVal;
        private byte byteVal;
        private double doubleVal;
        private double doubleVal2;
        private float floatVal;
        private boolean boolean1Val;
        private boolean boolean2Val;
        private Class<?> classVal;
        private SampleEnum enumVal;
    }
}
