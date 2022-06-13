package com.fluxtion.example.aot.imperative;

import com.fluxtion.compiler.builder.factory.NodeFactory;
import com.fluxtion.compiler.builder.factory.NodeRegistry;
import com.fluxtion.compiler.builder.stream.EventFlow;
import com.fluxtion.runtime.stream.helpers.Mappers;
import com.fluxtion.runtime.stream.helpers.Predicates;
import com.google.auto.service.AutoService;

import java.util.Map;

@AutoService(NodeFactory.class)
public class MyRootFactory implements NodeFactory<MyRootClass> {
    @Override
    public MyRootClass createNode(Map<String, Object> config, NodeRegistry registry) {
        System.out.println("returning an instance of MyRootClass");
        MyRootClass myRootClass = new MyRootClass();
        myRootClass.intEventSupplier = EventFlow.subscribe(String.class)
                .filter(Predicates::isInteger)
                .mapToInt(Mappers::parseInt)
                .map(Mappers.cumSumInt())
                .intStream();
        return myRootClass;
    }
}
