package com.fluxtion.example;

import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.Stop;

public class AreaCalculator {
    private static int area;
    @OnEventHandler(filterId = 4)
    public static boolean rectangle(Shape shape) {
        area += shape.getWidth() * shape.getHeight();
        return false;
    }

    @OnEventHandler(filterId = 3)
    public static boolean triangle(Shape shape) {
        area += 0.5 * shape.getWidth() * shape.getHeight();
        return false;
    }

    @OnEventHandler(filterId = 0)
    public static boolean circle(Shape shape) {
        area += 3.1459 * shape.getWidth() * shape.getHeight();
        return false;
    }

    @Stop
    public void finishCalc(){
        System.out.println("area:" + area);
    }
}
