package com.fluxtion.example.cookbook.nocleancode;

import com.fluxtion.compiler.Fluxtion;

public class GenerateAot {

    public static void main(String[] args) {
        Fluxtion.compileAot(c -> {
                    c.addNode(new AreaCalculator());
                    c.addInterfaceImplementation(ShapeProcessor.class);
                },
                "com.fluxtion.example.cookbook.nocleancode.generated",
                "ShapeEventProcessor");
    }
}
