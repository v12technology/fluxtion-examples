package com.fluxtion.example.cookbook.nocleancode;

import com.fluxtion.runtime.lifecycle.Lifecycle;

public interface ShapeProcessor extends Lifecycle {

    void handleEvent(Shape typedEvent);

    default void updateAreaCalc(Shape shape){
        handleEvent(shape);
    }
}
