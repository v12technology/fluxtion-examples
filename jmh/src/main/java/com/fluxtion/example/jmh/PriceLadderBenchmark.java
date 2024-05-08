package com.fluxtion.example.jmh;

import com.fluxtion.example.jmh.pricer.DemoPriceCalculatorMain;
import com.fluxtion.example.jmh.pricer.PriceLadder;
import com.fluxtion.example.jmh.pricer.generated.PriceLadderProcessor;
import com.fluxtion.example.jmh.pricer.generated.PriceLadderProcessorNoBranching;
import com.fluxtion.example.jmh.pricer.node.PriceDistributor;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

public class PriceLadderBenchmark {


    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 2)
    @BenchmarkMode(Mode.AverageTime)
    @Measurement(iterations = 2)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void avgTime_BranchingProcessor(PriceLadderState priceLadderState, Blackhole blackhole) throws InterruptedException {
        priceLadderState.priceProcessorNoBranch.newPriceLadder(priceLadderState.nextPriceLadder());
        blackhole.consume(priceLadderState.priceDistributor.getPriceLadder());
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 2)
    @BenchmarkMode(Mode.Throughput)
    @Measurement(iterations = 2)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void throughPut_No_BranchingProcessor(PriceLadderState priceLadderState, Blackhole blackhole) throws InterruptedException {
        priceLadderState.priceProcessorNoBranch.newPriceLadder(priceLadderState.nextPriceLadder());
        blackhole.consume(priceLadderState.priceDistributor.getPriceLadder());
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 2)
    @BenchmarkMode(Mode.AverageTime)
    @Measurement(iterations = 2)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public void avgTime_No_BranchingProcessor(PriceLadderState priceLadderState, Blackhole blackhole) throws InterruptedException {
        priceLadderState.priceProcessor.newPriceLadder(priceLadderState.nextPriceLadder());
        blackhole.consume(priceLadderState.priceDistributor.getPriceLadder());
    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 2)
    @BenchmarkMode(Mode.Throughput)
    @Measurement(iterations = 2)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void throughPut_BranchingProcessor(PriceLadderState priceLadderState, Blackhole blackhole) throws InterruptedException {
        priceLadderState.priceProcessor.newPriceLadder(priceLadderState.nextPriceLadder());
        blackhole.consume(priceLadderState.priceDistributor.getPriceLadder());
    }

    @State(Scope.Thread)
    public static class PriceLadderState {

        private PriceLadderProcessor priceProcessor;
        private PriceLadderProcessorNoBranching priceProcessorNoBranch;
        private PriceDistributor priceDistributor;
        private PriceLadder[] priceLadders;
        private final int ladderCount = 10_000;
        private int ladderPointer = 0;

        @Setup(Level.Trial)
        public void doSetup() {
            System.out.println("Do Setup");
            priceLadders = DemoPriceCalculatorMain.generateRandomPriceLadders(ladderCount);
            priceDistributor = new PriceDistributor();

            //branching processor
            priceProcessor = new PriceLadderProcessor();
            priceProcessor.init();
            priceProcessor.setPriceDistributor(priceDistributor);

            //No branching
            priceProcessorNoBranch = new PriceLadderProcessorNoBranching();
            priceProcessorNoBranch.init();
            priceProcessorNoBranch.setPriceDistributor(priceDistributor);
        }

        public PriceLadder nextPriceLadder() {
            int pointer = ladderPointer++ % ladderCount;
            return priceLadders[pointer];
        }

        @TearDown(Level.Iteration)
        public void doTearDown() {
            System.out.println("TearDown - generate new random price ladders");
            priceLadders = DemoPriceCalculatorMain.generateRandomPriceLadders(ladderCount);
        }

    }
}
