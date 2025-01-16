package com.fluxtion.example.cookbook.tempmonitoring;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.dataflow.groupby.GroupBy;
import com.fluxtion.runtime.dataflow.helpers.Aggregates;
import lombok.Data;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Monitors each machine for an average temperature breach in a sliding window of 4 seconds with a bucket size of 1 second
 * readings are produced randomly every 10 millis the aggregation handles all combining values within a window and dropping
 * values that have expired.<br>
 * <br>
 * Notifies a support contact in the correct location where the breach has occurred. The contact lookup is built up
 * through events:
 * <ul>
 *     <li>MachineLocation = Machine id -> location</li>
 *     <li>SupportContact = location -> contact details</li>
 * </ul>
 * <br>
 * Running the app should produce an output similar to below
 *
 * <pre>
 *  Application started - wait four seconds for first machine readings
 *
 *  11:37:20.115 'server_TKM@USA_EAST_2', temp:'48.88' contact:'tandy@fluxtion.com'
 *  11:37:20.115 'server_MSFT@USA_EAST_2', temp:'52.88' contact:'tandy@fluxtion.com'
 *  11:37:20.115 'server_AMZN@USA_EAST_1', temp:'49.60' contact:'jean@fluxtion.com'
 *  ----------------------------------------------------------------------
 *
 *  11:37:21.114 'server_TKM@USA_EAST_2', temp:'49.92' contact:'tandy@fluxtion.com'
 *  11:37:21.114 'server_MSFT@USA_EAST_2', temp:'53.67' contact:'tandy@fluxtion.com'
 *  ----------------------------------------------------------------------
 *
 *  11:37:22.115 'server_TKM@USA_EAST_2', temp:'48.61' contact:'tandy@fluxtion.com'
 *  11:37:22.115 'server_MSFT@USA_EAST_2', temp:'53.98' contact:'tandy@fluxtion.com'
 *  ----------------------------------------------------------------------
 * </pre>
 */
public class IndividualMachineMonitoring {

    private static final String[] MACHINE_IDS = new String[]{"server_GOOG", "server_AMZN", "server_MSFT", "server_TKM"};

    public enum Locations {USA_EAST_1, USA_EAST_2}

    public record MachineReadings(String id, double temp) {}

    public record MachineLocation(String id, Locations locationCode) {}

    public record SupportContact(String name, Locations locationCode, String contactDetails) {}

    public static void main(String[] args) {
        //use a compiled version, Fluxtion.interpret will work and handles in-line lambdas
        var machineMonitoring = Fluxtion.compile(IndividualMachineMonitoring::buildGraph);
        machineMonitoring.init();

        //set up machine locations
        machineMonitoring.onEvent(new MachineLocation("server_GOOG", Locations.USA_EAST_1));
        machineMonitoring.onEvent(new MachineLocation("server_AMZN", Locations.USA_EAST_1));
        machineMonitoring.onEvent(new MachineLocation("server_MSFT", Locations.USA_EAST_2));
        machineMonitoring.onEvent(new MachineLocation("server_TKM", Locations.USA_EAST_2));

        //set up support contacts
        machineMonitoring.onEvent(new SupportContact("Jean", Locations.USA_EAST_1, "jean@fluxtion.com"));
        machineMonitoring.onEvent(new SupportContact("Tandy", Locations.USA_EAST_2, "tandy@fluxtion.com"));

        Random random = new Random();
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                    String machineId = MACHINE_IDS[random.nextInt(MACHINE_IDS.length)];
                    double temperatureReading = random.nextDouble() * 100;
                    machineMonitoring.onEvent(new MachineReadings(machineId, temperatureReading));
                },
                100, 10, TimeUnit.MILLISECONDS);

        System.out.println("Application started - wait four seconds for first machine readings\n");
    }

    public static void buildGraph(EventProcessorConfig processorConfig) {
        var workScheduler = new WorkScheduler();

        //4 second sliding window with bucket size of 1 second, calculates average temp in window
        //group by on machine id so each machine has its own average temp state
        DataFlow.subscribe(MachineReadings.class)
                //4 sec window with 1000 milli bucket size
                .groupBySliding(MachineReadings::id, MachineReadings::temp, Aggregates.doubleAverageFactory(), 1000, 4)
                .filterValues(IndividualMachineMonitoring::tempBreachCheck)//inline lambda cant be compiled - use method reference
                .map(GroupBy::toMap)
                .push(workScheduler::investigateMachine);

        //machine location map
        DataFlow.groupBy(MachineLocation::id)
                .map(GroupBy::toMap)
                .push(workScheduler::setMachineLocationMap);

        //support contact location map
        DataFlow.groupBy(SupportContact::locationCode)
                .map(GroupBy::toMap)
                .push(workScheduler::setSupportContactnMap);
    }

    public static Boolean tempBreachCheck(Double inputTemp) {
        return inputTemp > 48;
    }

    @Data
    public static class WorkScheduler {
        private transient Map<String, MachineLocation> machineLocationMap = new HashMap<>();
        private transient Map<Locations, SupportContact> supportContactnMap = new HashMap<>();
        private final DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

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
                    contactDetails = contact == null ? contactDetails : "contact:'" + contact.contactDetails + "'";
                }
                System.out.printf("%s '%s@%s', temp:'%.2f' %s%n", now, machineId, location, temp, contactDetails);
            });
            System.out.println("----------------------------------------------------------------------\n");
        }
    }
}
