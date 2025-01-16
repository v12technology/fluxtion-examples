package com.fluxtion.example.cookbook.tempmonitoring;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.groupby.GroupBy;
import com.fluxtion.runtime.dataflow.helpers.Aggregates;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class IndividualMachineMonitoring {

    private static final String[] MACHINE_IDS = new String[]{"machine_GOOG", "machine_AMZN", "machine_MSFT", "machine_TKM"};
    public enum Locations{USA_EAST_1, USA_EAST_2};

    public static void buildGraph(EventProcessorConfig processorConfig) {
        var workScheduler = new WorkScheduler();

        //machine monitoring rolling window
        DataFlow.subscribe(MachineReadings.class)
                .groupBySliding(MachineReadings::id, MachineReadings::temp, Aggregates.doubleAverageFactory(), 1000, 4)
                .filterValues(temp -> temp > 48)
                .map(GroupBy::toMap)
                .push(workScheduler::investigateMachine);

        //machine location map
        DataFlow.groupBy(MachineLocation::id)
                .map(GroupBy::toMap)
                .push(workScheduler::machineLocationMap);

        //support contact location map
        DataFlow.groupBy(SupportContact::locationCode)
                .map(GroupBy::toMap)
                .push(workScheduler::supportContactnMap);

    }

    public static void main(String[] args) {
        var machineMonitoring = Fluxtion.interpret(IndividualMachineMonitoring::buildGraph);
        machineMonitoring.init();

        //set up machine locations
        machineMonitoring.onEvent(new MachineLocation("machine_GOOG", Locations.USA_EAST_1));
        machineMonitoring.onEvent(new MachineLocation("machine_AMZN", Locations.USA_EAST_1));
        machineMonitoring.onEvent(new MachineLocation("machine_MSFT", Locations.USA_EAST_2));
        machineMonitoring.onEvent(new MachineLocation("machine_TKM", Locations.USA_EAST_2));

        //set up support contacts
        machineMonitoring.onEvent(new SupportContact("Jean", Locations.USA_EAST_1, "jean@fluxtion.com"));
        machineMonitoring.onEvent(new SupportContact("Tandy", Locations.USA_EAST_2, "tandy@fluxtion.com"));

        Timer timer = new Timer("readingsThread", false);
        Random random = new Random();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String machineId = MACHINE_IDS[random.nextInt(MACHINE_IDS.length)];
                double temperatureReading = random.nextDouble() * 100;
                machineMonitoring.onEvent(new MachineReadings(machineId, temperatureReading));

            }
        }, 100, 10);

    }


    public record MachineReadings(String id, double temp) { }

    public record MachineLocation(String id, Locations locationCode) { }

    public record SupportContact(String name, Locations locationCode, String contactDetails) { }


    @Data
    @Accessors(fluent = true)
    public static class WorkScheduler {
        private Map<String, MachineLocation> machineLocationMap = new HashMap<>();
        private Map<Locations, SupportContact> supportContactnMap = new HashMap<>();
        private static final SupportContact emptyContact = new SupportContact("none", Locations.USA_EAST_1, "no contact");
        private final DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

        public void investigateMachine(Map<String, Double> stringDoubleMap) {

            if(stringDoubleMap.isEmpty()){
                return;
            }
            String now = timeColonFormatter.format(LocalTime.now());
                stringDoubleMap.forEach((machineId, temp) -> {
                    MachineLocation machineLocation = machineLocationMap.get(machineId);
                    String contactDetails = "No contact found";
                    String location = "unknown";
                    if(machineLocation != null) {
                        location = machineLocation.locationCode.name();
                        SupportContact contact = supportContactnMap.get(machineLocation.locationCode());
                        contactDetails = contact == null ? contactDetails : "contact: " + contact.contactDetails;
                    }
                    System.out.printf("%s machineId: %s, temp: %s location: %s %s%n", now, machineId, temp, location, contactDetails);
                });
            System.out.println("----------------------------------------------------------------------\n");
        }

    }
}
