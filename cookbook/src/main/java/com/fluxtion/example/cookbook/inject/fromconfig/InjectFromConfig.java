package com.fluxtion.example.cookbook.inject.fromconfig;

import com.fluxtion.compiler.Fluxtion;

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

}
