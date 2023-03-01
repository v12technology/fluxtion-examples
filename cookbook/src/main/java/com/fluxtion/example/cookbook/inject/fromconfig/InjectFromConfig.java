package com.fluxtion.example.cookbook.inject.fromconfig;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.annotations.builder.AssignToField;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.event.Signal.IntSignal;
import com.fluxtion.runtime.node.NamedNode;

public class InjectFromConfig {

    public static void main(String[] args) {
        var processor = Fluxtion.interpret(c -> {
            c.addNode(
                    Radiator.build("lounge_rad1", "lounge_sensor"),
                    Radiator.build("lounge_rad2", "lounge_sensor"),
                    Radiator.build("lounge_rad3", "lounge_sensor"),
                    Radiator.build("kitchen_rad1", "kitchen_sensor")
            );
            c.registerInjectable("lounge_sensor", new RoomSensor("lounge_temp_sensor"))
                    .registerInjectable("shed_sensor", new RoomSensor("shed_temp_sensor"))
                    .registerInjectable("bedroom_sensor", new RoomSensor("bedroom_temp_sensor"))
                    .registerInjectable("kitchen_sensor", new RoomSensor("kitchen_temp_sensor"));
        });

        processor.init();
        processor.publishIntSignal("lounge_temp_sensor", 10);
        System.out.println();
        processor.publishIntSignal("lounge_temp_sensor", 22);
        System.out.println();
        processor.publishIntSignal("lounge_temp_sensor", 23);
        System.out.println();
        processor.publishIntSignal("lounge_temp_sensor", 15);
        System.out.println();
        processor.publishIntSignal("kitchen_temp_sensor", 19);
        System.out.println();
        processor.publishIntSignal("kitchen_temp_sensor", 26);
    }

    public interface Sensor {
        boolean isOn();
    }

    public static class Radiator implements NamedNode {
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
            if(roomSensor.isOn()){
                System.out.println("Radiator ON:" + radiatorName);
            }else{
                System.out.println("Radiator OFF:" + radiatorName);
            }
            return false;
        }

        @Override
        public String getName() {
            return radiatorName;
        }

        public boolean isOn() {
            return roomSensor.on;
        }
    }

    public static class RoomSensor implements Sensor {
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
            if(changeState){
                System.out.println(name + " reading:" + intSignal.getValue() + " switch heating:" + (on?"ON":"OFF"));
            }else{
                System.out.println(name + " reading:" + intSignal.getValue() + " do nothing");
            }
            return changeState;
        }

        public boolean isOn() {
            return on;
        }
    }
}
