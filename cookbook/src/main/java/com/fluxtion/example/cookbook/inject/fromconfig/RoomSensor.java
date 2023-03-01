package com.fluxtion.example.cookbook.inject.fromconfig;

import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.builder.AssignToField;
import com.fluxtion.runtime.event.Signal.IntSignal;

public class RoomSensor implements Sensor {
    private final String name;
    private boolean on = false;

    public RoomSensor(@AssignToField("name") String name) {
        this.name = name;
    }

    @OnEventHandler(filterVariable = "name")
    public boolean tempSignal(IntSignal intSignal) {
        boolean oldControl = on;
        on = intSignal.getValue() < 20;
        boolean changeState = on != oldControl;
        if (changeState) {
            System.out.println(name + " reading:" + intSignal.getValue() + " switch heating:" + (on ? "ON" : "OFF"));
        } else {
            System.out.println(name + " reading:" + intSignal.getValue() + " do nothing");
        }
        return changeState;
    }

    public boolean isOn() {
        return on;
    }
}
