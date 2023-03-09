package com.fluxtion.example.cookbook.nocleancode;

import com.fluxtion.example.cookbook.nocleancode.Shape.Circle;
import com.fluxtion.example.cookbook.nocleancode.Shape.Rectangle;
import com.fluxtion.example.cookbook.nocleancode.Shape.Square;
import com.fluxtion.example.cookbook.nocleancode.Shape.Triangle;
import com.fluxtion.example.cookbook.nocleancode.generated.ShapeEventProcessor;

public class Main {
    public static void main(String[] args) {
        ShapeProcessor shapeProcessor = new ShapeEventProcessor();
        shapeProcessor.init();
        shapeProcessor.updateAreaCalc(new Rectangle(3, 4));
        shapeProcessor.updateAreaCalc(new Circle(3));
        shapeProcessor.updateAreaCalc(new Triangle(4, 4));
        shapeProcessor.updateAreaCalc(new Square(4));
        shapeProcessor.stop();
    }

}
