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

/**
 * Monitors each machine for an average temperature breach in a sliding window of 4 seconds with a bucket size of 1 second
 * readings are produced randomly every 10 millis the aggregation handles all combining values within a window and dropping
 * values that have expired <br>
 * <br>
 * Running the app should produce an output similar to below
 *
 * <pre>
 *
 *     08:24:58 711 machineId: machine_AMZN, temp: 52.23570663362154 location: USA_EAST_1 contact: jean@fluxtion.com
 *     08:24:58 711 machineId: machine_GOOG, temp: 48.46758458121178 location: USA_EAST_1 contact: jean@fluxtion.com
 *     ----------------------------------------------------------------------
 *
 *     08:24:59 721 machineId: machine_AMZN, temp: 55.728445586631835 location: USA_EAST_1 contact: jean@fluxtion.com
 *     ----------------------------------------------------------------------
 *
 *     08:25:00 722 machineId: machine_AMZN, temp: 57.88833058099956 location: USA_EAST_1 contact: jean@fluxtion.com
 *     08:25:00 722 machineId: machine_MSFT, temp: 48.939382869208174 location: USA_EAST_2 contact: tandy@fluxtion.com
 *     ----------------------------------------------------------------------
 * </pre>
 */
public class IndividualMachineMonitoring {

    private static final String[] MACHINE_IDS = new String[]{"machine_GOOG", "machine_AMZN", "machine_MSFT", "machine_TKM"};
    public enum Locations {USA_EAST_1, USA_EAST_2}

    public static void buildGraph(EventProcessorConfig processorConfig) {
        var workScheduler = new WorkScheduler();

        //4 second sliding window with bucket size of 1 second, calculates average temp in window
        //group by on machine id so each machine has its own average temp state
        DataFlow.subscribe(MachineReadings.class)
                //4 sec window with 1000 milli bucket size
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

        System.out.println("Application started - wait four seconds for first machine readings\n");

    }


    public record MachineReadings(String id, double temp) { }

    public record MachineLocation(String id, Locations locationCode) { }

    public record SupportContact(String name, Locations locationCode, String contactDetails) { }


    @Data
    @Accessors(fluent = true)
    public static class WorkScheduler {
        private Map<String, MachineLocation> machineLocationMap = new HashMap<>();
        private Map<Locations, SupportContact> supportContactnMap = new HashMap<>();
        private final DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

        public void investigateMachine(Map<String, Double> machineWarningTempMap) {

            if (machineWarningTempMap.isEmpty()) {
                return;
            }
            String now = timeColonFormatter.format(LocalTime.now());
            machineWarningTempMap.forEach((machineId, temp) -> {
                MachineLocation machineLocation = machineLocationMap.get(machineId);
                String contactDetails = "No contact found";
                String location = "unknown";
                if (machineLocation != null) {
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
