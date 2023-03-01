package com.fluxtion.example.cookbook.inject.fromconfig;

import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.node.NamedNode;

public class Radiator implements NamedNode {
    private final String radiatorName;
    @Inject(factoryVariableName = "sensorName")
    private final RoomSensor roomSensor;

    private transient String sensorName;

    public Radiator(String radiatorName, RoomSensor roomSensor) {
        this.radiatorName = radiatorName;
        this.roomSensor = roomSensor;
    }

    public static Radiator build(String radiatorName, String sensorName) {
        Radiator radiator = new Radiator(radiatorName, null);
        radiator.sensorName = sensorName;
        return radiator;
    }

    @OnTrigger
    public boolean triggerRadiator() {
        if (roomSensor.isOn()) {
            System.out.println("Radiator ON:" + radiatorName);
        } else {
            System.out.println("Radiator OFF:" + radiatorName);
        }
        return false;
    }

    @Override
    public String getName() {
        return radiatorName;
    }

    public boolean isOn() {
        return roomSensor.isOn();
    }
}
