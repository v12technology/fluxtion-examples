package com.fluxtion.example;

import com.fluxtion.compiler.EventProcessorConfig.DISPATCH_STRATEGY;
import com.fluxtion.compiler.Fluxtion;

public class GenerateAot {

    public static void main(String[] args) {
        Fluxtion.compileAot(c -> {
                    c.addNode(new AreaCalculator());
                    c.addInterfaceImplementation(ShapeProcessor.class);
                    c.setDispatchStrategy(DISPATCH_STRATEGY.INSTANCE_OF);
                    c.javaTargetRelease("17");
                },
                "com.fluxtion.example.cookbook.nocleancode.generated",
                "ShapeEventProcessorInstanceOf");

        Fluxtion.compileAot(c -> {
                    c.addNode(new AreaCalculator());
                    c.addInterfaceImplementation(ShapeProcessor.class);
                    c.setDispatchStrategy(DISPATCH_STRATEGY.CLASS_NAME);
                    c.javaTargetRelease("17");
                },
                "com.fluxtion.example.cookbook.nocleancode.generated",
                "ShapeEventProcessorClassNameSwitch");

        Fluxtion.compileAot(c -> {
                    c.addNode(new AreaCalculator());
                    c.addInterfaceImplementation(ShapeProcessor.class);
                    c.setDispatchStrategy(DISPATCH_STRATEGY.PATTERN_MATCH);
                    c.javaTargetRelease("17");
                },
                "com.fluxtion.example.cookbook.nocleancode.generated",
                "ShapeEventProcessorPatternMatchSwitch");
    }
}
