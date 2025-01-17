package com.fluxtion.example.cookbook.performance;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.annotations.AfterEvent;
import com.fluxtion.runtime.dataflow.aggregate.function.primitive.DoubleAverageFlowFunction;
import com.fluxtion.runtime.dataflow.groupby.GroupBy;
import com.fluxtion.runtime.time.FixedRateTrigger;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Monitors each machine for an average or current temperature breach in a sliding window of 4 seconds with a bucket size of 1 second
 * readings are produced randomly every 10 millis the aggregation handles all combining values within a window and dropping
 * values that have expired.<br>
 * <br>
 * Alarm status is published on any change to the alarm state, i.e. new alarms or cleared old alarms<br>
 * <br>
 * Each machine can have its own temperature alarm profile updated by event MachineProfile<br>
 * <br>
 * Notifies a support contact in the correct location where the breach has occurred. The contact lookup is built up
 * through events:
 * <ul>
 *     <li>MachineLocation = Machine id -> location</li>
 *     <li>SupportContact = location -> contact details</li>
 * </ul>
 * <br>
 * <br>
 *
 * A sink is available for the host application to consume the alarm output, in this case a pretty print consumer<br>
 * <br>
 *
 * Running the app should produce an output similar to below:
 *
 * <pre>
 *  Application started - wait four seconds for first machine readings
 *
 *  ALARM UPDATE 14:31:30.785
 *  New alarms: ['server_GOOG@USA_EAST_1',  temp:'49.16', avgTemp:'52.05' SupportContact[name=Jean, locationCode=USA_EAST_1, contactDetails=jean@fluxtion.com], 'server_TKM@USA_EAST_2',  temp:'86.47', avgTemp:'52.37' SupportContact[name=Tandy, locationCode=USA_EAST_2, contactDetails=tandy@fluxtion.com], 'server_AMZN@USA_EAST_1',  temp:'71.48', avgTemp:'54.25' SupportContact[name=Jean, locationCode=USA_EAST_1, contactDetails=jean@fluxtion.com], 'server_MSFT@USA_EAST_2',  temp:'31.70', avgTemp:'52.53' SupportContact[name=Tandy, locationCode=USA_EAST_2, contactDetails=tandy@fluxtion.com]]
 *  Alarms to clear[]
 *  Current alarms[server_GOOG, server_TKM, server_AMZN, server_MSFT]
 *  ------------------------------------
 *
 *  ALARM UPDATE 14:31:32.778
 *  New alarms: []
 *  Alarms to clear[server_TKM]
 *  Current alarms[server_GOOG, server_AMZN, server_MSFT]
 *  ------------------------------------
 *
 *  ALARM UPDATE 14:31:33.768
 *  New alarms: ['server_TKM@USA_EAST_2',  temp:'98.33', avgTemp:'49.95' SupportContact[name=Tandy, locationCode=USA_EAST_2, contactDetails=tandy@fluxtion.com]]
 *  Alarms to clear[]
 *  Current alarms[server_GOOG, server_TKM, server_AMZN, server_MSFT]
 *  ------------------------------------
 *
 *  ALARM UPDATE 14:31:37.777
 *  New alarms: []
 *  Alarms to clear[server_AMZN]
 *  Current alarms[server_GOOG, server_TKM, server_MSFT]
 *  ------------------------------------
 * </pre>
 */
public class CustomisableMachineMonitoring {

    private static final String[] MACHINE_IDS = new String[]{"server_GOOG", "server_AMZN", "server_MSFT", "server_TKM"};
    private static final DateTimeFormatter timeColonFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    public enum LocationCode {USA_EAST_1, USA_EAST_2}

    //INPUT EVENTS
    public record MachineReading(String id, double temp) { }
    public record MachineProfile(String id, LocationCode locationCode, double maxTempAlarm, double maxAvgTempAlarm) { }
    public record SupportContact(String name, LocationCode locationCode, String contactDetails) { }

    public static int counter = 0;
    public static int fastCounter = 0;
    public static long loopCounter = 0;
    public static long sumDelay = 0;

    public static void main(String[] args) {
        //use a compiled version, Fluxtion.interpret will work and handles in-line lambdas
        var tempMonitor = Fluxtion.compile(CustomisableMachineMonitoring::buildGraph);
        tempMonitor.init();
        tempMonitor.addSink("alarmPublisher", CustomisableMachineMonitoring::prettyPrintAlarms);

        //set up machine locations
        tempMonitor.onEvent(new MachineProfile("server_GOOG", LocationCode.USA_EAST_1, 105, 48));
        tempMonitor.onEvent(new MachineProfile("server_AMZN", LocationCode.USA_EAST_1, 105, 50));
        tempMonitor.onEvent(new MachineProfile("server_MSFT", LocationCode.USA_EAST_2,195, 50));
        tempMonitor.onEvent(new MachineProfile("server_TKM", LocationCode.USA_EAST_2,195, 50));

        //set up support contacts
        tempMonitor.onEvent(new SupportContact("Jean", LocationCode.USA_EAST_1, "jean@fluxtion.com"));
        tempMonitor.onEvent(new SupportContact("Tandy", LocationCode.USA_EAST_2, "tandy@fluxtion.com"));

        Random random = new Random();

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                    String machineId = MACHINE_IDS[random.nextInt(MACHINE_IDS.length)];
                    double temperatureReading = random.nextDouble() * 100;
                    long now = System.nanoTime();
                    tempMonitor.onEvent(new MachineReading(machineId, temperatureReading));

                    long delay = System.nanoTime() - now;
                    if(loopCounter == 20_000){
                        sumDelay = delay * 20_000;
                    }
                    loopCounter++;
                    sumDelay += delay;
                    if(delay < 2_000){
                        fastCounter++;
                    }
                    if(delay > 55_000 & loopCounter > 20_000) {
                        counter++;
                        long avgLatency = sumDelay / loopCounter;
                        System.out.println("DELAY COUNT:" + counter + " loopCounter:" + loopCounter
                        + " fastCounter:" + fastCounter + " avgLatency:" + avgLatency);
                    }
                },
                100, 400, TimeUnit.NANOSECONDS);

        System.out.println("Application started - wait four seconds for first machine readings\n");
    }

    public static void buildGraph(EventProcessorConfig processorConfig) {
        var currentMachineTemp = DataFlow.groupBy(MachineReading::id, MachineReading::temp);

        var avgMachineTemp = DataFlow.subscribe(MachineReading.class)
                .groupBySliding(MachineReading::id, MachineReading::temp, DoubleAverageFlowFunction::new, 1000, 4);

        DataFlow.groupBy(MachineProfile::id)
                .mapValues(MachineState::new)
                .mapBiFlowFunction(CustomisableMachineMonitoring::addContact, DataFlow.groupBy(SupportContact::locationCode))
                .innerJoin(currentMachineTemp, MachineState::setCurrentTemperature)
                .innerJoin(avgMachineTemp, MachineState::setAvgTemperature)
                .publishTriggerOverride(FixedRateTrigger.atMillis(1_000))
                .filterValues(MachineState::outsideOperatingTemp)
                .map(GroupBy::toMap)
                .map(new AlarmMonitor()::activeAlarms)
                .filter(AlarmMonitor::isChanged)
                .sink("alarmPublisher");
    }

    public static GroupBy<String, MachineState> addContact(
            GroupBy<String, MachineState> machineStates,
            GroupBy<LocationCode, SupportContact> supportContacts) {
        machineStates.toMap().forEach(
                (id, state) -> state.setSupportContact(supportContacts.toMap().get(state.locationCode))
        );
        return machineStates;
    }

    @Data
    @Accessors(fluent = false, chain = true)
    public static class MachineState {

        private String id;
        private LocationCode locationCode;
        private double currentTemperature = Double.NaN;
        private double avgTemperature = Double.NaN;
        private double maxAvgTemperature;
        private double maxCurrentTemperature;
        private SupportContact supportContact;

        public MachineState(MachineProfile machineProfile) {
            this.id = machineProfile.id;
            this.locationCode = machineProfile.locationCode;
            this.maxCurrentTemperature = machineProfile.maxTempAlarm;
            this.maxAvgTemperature = machineProfile.maxAvgTempAlarm;
        }

        public Boolean outsideOperatingTemp() {
            return currentTemperature > maxCurrentTemperature || avgTemperature > maxAvgTemperature;
        }
    }

    @Getter
    @ToString
    public static class AlarmMonitor {

        private transient final Set<String> alarmsToClear = new HashSet<>();
        private transient final Map<String, MachineState> activeAlarms = new HashMap<>();
        private transient final Map<String, MachineState> newAlarms = new HashMap<>();
        private boolean changed;

        public AlarmMonitor activeAlarms(Map<String, MachineState> updatedActiveAlarms) {
            changed = !activeAlarms.keySet().equals(updatedActiveAlarms.keySet());

            alarmsToClear.clear();
            alarmsToClear.addAll(activeAlarms.keySet());
            alarmsToClear.removeAll(updatedActiveAlarms.keySet());

            newAlarms.clear();
            newAlarms.putAll(updatedActiveAlarms);
            activeAlarms.keySet().forEach(newAlarms::remove);

            activeAlarms.clear();
            activeAlarms.putAll(updatedActiveAlarms);
            return this;
        }

        @AfterEvent
        public void purgerNewAlarms() {
            alarmsToClear.clear();
            newAlarms.clear();
            changed = false;
        }
    }

    public static void prettyPrintAlarms(AlarmMonitor alarmMonitor) {
        String newAlarms = alarmMonitor.getNewAlarms().values().stream()
                .map(m -> String.format("'%s@%s',  temp:'%.2f', avgTemp:'%.2f' %s", m.getId(), m.getLocationCode(), m.getCurrentTemperature(), m.getAvgTemperature(), m.getSupportContact()))
                .collect(Collectors.joining(", ", "New alarms: [", "]"));

        String alarmsToClear = alarmMonitor.getAlarmsToClear().stream().collect(Collectors.joining(", ", "Alarms to clear[", "]"));
        String existingToClear = alarmMonitor.getActiveAlarms().keySet().stream().collect(Collectors.joining(", ", "Current alarms[", "]"));
        System.out.println("ALARM UPDATE " + timeColonFormatter.format(LocalTime.now()));
        System.out.println(newAlarms);
        System.out.println(alarmsToClear);
        System.out.println(existingToClear);
        System.out.println("------------------------------------\n");
    }
}
