package com.fluxtion.example.reference.dsl.frontpage_examples;

import com.fluxtion.compiler.EventProcessorConfig;
import com.fluxtion.compiler.FluxtionCompilerConfig;
import com.fluxtion.compiler.FluxtionGraphBuilder;
import com.fluxtion.compiler.builder.dataflow.DataFlow;
import com.fluxtion.example.reference.dsl.generated.FVGraphPricer;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.dataflow.DoubleFlowSupplier;
import com.fluxtion.runtime.dataflow.helpers.Mappers;
import lombok.Data;
import lombok.NoArgsConstructor;

public class GraphPricer {

    public static void main(String[] args) {
        var fvCalc = new FVGraphPricer();//Fluxtion.interpret(new FVGraphBuilder()::buildGraph);
        fvCalc.init();

        """
                //init test
                spot 1146
                dividends 3.47
                interestRate 0.057
                futuresPrice 1157
                daysToExpiry 78
                
                //S&P test
                daysToExpiry 0
                spot 5246.68
                dividends 8.75
                interestRate 0.0635
                daysToExpiry 37
                """
                .lines()
                .map(String::trim)
                .forEach(s -> {
                    String[] pair = s.split(" ");
                    if (s.startsWith("//")) {
                        System.out.println("\n" + s);
                    } else if (pair.length == 2) {
                        fvCalc.publishDoubleSignal(pair[0], Double.parseDouble(pair[1]));
                    }
                });
    }

    public static class FVGraphBuilder implements FluxtionGraphBuilder {

        @Override
        public void buildGraph(EventProcessorConfig eventProcessorConfig) {
            var cashPrice = DataFlow.subscribeToDoubleSignal("spot");

            var fvAbs = DataFlow.subscribeToNode(
                            new FairValueAbsolute(
                                    cashPrice.flowSupplier(),
                                    DataFlow.subscribeToDoubleSignal("dividends").flowSupplier(),
                                    DataFlow.subscribeToDoubleSignal("interestRate").flowSupplier(),
                                    DataFlow.subscribeToDoubleSignal("daysToExpiry").flowSupplier()
                            ))
                    .mapToDouble(FairValueAbsolute::getDerivedValue)
                    .map(GraphPricer::round2Dps)
                    .console("fvAbs = {}");

            cashPrice.mapBiFunction(Mappers::subtractDoubles, fvAbs)
                    .map(GraphPricer::round2Dps)
                    .console("spotPriceAdjustment:{}")
                    .mapBiFunction(Double::sum, cashPrice)
                    .console("spotPriceFV:{}");

            DataFlow.subscribeToDoubleSignal("futuresPrice")
                    .mapBiFunction(Mappers::subtractDoubles, fvAbs)
                    .map(GraphPricer::round2Dps)
                    .console("futurePriceAdjustment:{}");
        }

        @Override
        public void configureGeneration(FluxtionCompilerConfig compilerConfig) {
            compilerConfig.setClassName("FVGraphPricer");
            compilerConfig.setPackageName("com.fluxtion.example.reference.dsl.generated");
        }
    }

    @Data
    @NoArgsConstructor
    public static class FairValueAbsolute {
        private DoubleFlowSupplier cashPrice;
        private DoubleFlowSupplier dividends;
        private DoubleFlowSupplier interestRate;
        private DoubleFlowSupplier daysToExpiry;
        private transient double derivedValue;

        public FairValueAbsolute(DoubleFlowSupplier cashPrice,
                                 DoubleFlowSupplier dividends,
                                 DoubleFlowSupplier interestRate,
                                 DoubleFlowSupplier daysToExpiry) {
            this.cashPrice = cashPrice;
            this.dividends = dividends;
            this.interestRate = interestRate;
            this.daysToExpiry = daysToExpiry;
        }

        @Initialise
        public void init() {
            derivedValue = 0;
        }

        @OnTrigger
        public boolean calculate() {
            double spot = cashPrice.getAsDouble();
            double dividend = dividends.getAsDouble();
            double interest = interestRate.getAsDouble();
            double days = daysToExpiry.getAsDouble();
            if (spot != 0 && dividend != 0 && interest != 0 & days != 0) {
                derivedValue = spot * (1 + interest * (days / 365.0)) - dividend;
                System.out.println("theoreticalFairValue:" + round2Dps(derivedValue) +
                        ", cashPrice:" + spot +
                        ", dividends:" + dividend +
                        ", daysToExpiry:" + days +
                        ", interestRate:" + interest);
                return true;
            }
            if(derivedValue != 0) {
                derivedValue = 0;
                return true;
            }
            return false;
        }
    }

    public static double round2Dps(double d) {
        return ((int) Math.round(d * 100)) / 100.0;
    }
}
