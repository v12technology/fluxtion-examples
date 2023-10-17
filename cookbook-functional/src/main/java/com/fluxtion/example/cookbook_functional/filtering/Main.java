package com.fluxtion.example.cookbook_functional.filtering;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.dataflow.helpers.Mappers;

public class Main {

    public static void main(String[] args) {
        //compile the EOP
        var eop = Fluxtion.interpret(c -> {
            var centigradeFlow = DataFlow.subscribe(TempReading.class)
                    .map(Main::convertFtoC);
            var sensor1Flow = centigradeFlow
                    .filter(t -> t.id().equals("sensor1"))
                    .map(TempReading::reading);
            var sensor2Flow = centigradeFlow
                    .filter(t -> t.id().equals("sensor2"))
                    .map(TempReading::reading);
            DataFlow.mapBiFunction(Mappers::subtractInts, sensor1Flow, sensor2Flow)
                    .map(Math::abs)
                    .filter(new DeltaFilter(10)::breachesDelta)
                    .console("warning the delta between sensors > {}");
        });
        eop.init();

        //dispatching event stimuli to the EOP
        eop.onEvent(new TempReading("sensor1", 95));
        eop.onEvent(new TempReading("sensor1", 101));
        eop.onEvent(new TempReading("sensor2", 85));

        //dispatching a service based stimulus
        MaxDeltaMonitor breachFilter = eop.getExportedService();
        breachFilter.setMaxDelta(5);

        //dispatching event stimuli to the EOP
        eop.onEvent(new TempReading("sensor2", 86));
        eop.onEvent(new TempReading("sensor1", 58));
        eop.onEvent(new TempReading("sensorXX", 500));
    }

    public record TempReading(String id, int reading){}

    public static TempReading convertFtoC(TempReading sensorReading){
        int centigrade = (int) (5.0/9.0 * (sensorReading.reading()-32));
        return new TempReading(sensorReading.id(), centigrade);
    }

    public interface MaxDeltaMonitor{
        void setMaxDelta(int delta);
    }

    public static class DeltaFilter implements
            @ExportService MaxDeltaMonitor{

        private int barrier;

        public DeltaFilter(int barrier) {
            this.barrier = barrier;
        }

        @Override
        public void setMaxDelta(int delta) {
            barrier = delta;
        }

        public boolean breachesDelta(int input){
            return input > barrier;
        }
    }
}
