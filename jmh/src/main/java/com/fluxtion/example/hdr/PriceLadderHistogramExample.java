package com.fluxtion.example.hdr;

import com.fluxtion.example.jmh.pricer.DemoPriceCalculatorMain;
import com.fluxtion.example.jmh.pricer.PriceLadder;
import com.fluxtion.example.jmh.pricer.generated.PriceLadderProcessorNoBranching;
import com.fluxtion.example.jmh.pricer.node.PriceDistributor;
import org.HdrHistogram.Histogram;

public class PriceLadderHistogramExample {
    // A Histogram covering the range from 1 nsec to 1 hour with 3 decimal point resolution:
    static Histogram histogram = new Histogram(3600000000000L, 3);

    static public volatile PriceLadderProcessorNoBranching priceProcessor;

    static long WARMUP_TIME_MSEC = 5000;
    static long RUN_TIME_MSEC = 20000;
    static PriceLadder priceLadder;


    static void processPriceLadderUpdate(PriceLadder priceLadder) {
        long startTime = System.nanoTime();
        priceProcessor.newPriceLadder(priceLadder);
        long endTime = System.nanoTime();
        histogram.recordValue(endTime - startTime);
    }

    public static void main(final String[] args) {
        long startTime = System.currentTimeMillis();
        long now;

        final int ladderCount = 10_000;
        int ladderPointer = 0;
        PriceLadder[] priceLadders = DemoPriceCalculatorMain.generateRandomPriceLadders(ladderCount);
        PriceDistributor priceDistributor = new PriceDistributor();
        //create processor
        priceProcessor = new PriceLadderProcessorNoBranching();
        priceProcessor.init();
        priceProcessor.setPriceDistributor(priceDistributor);

        do {
            processPriceLadderUpdate(priceLadders[ladderPointer++ % ladderCount]);
            now = System.currentTimeMillis();
            //stop any dead code elimination
            priceLadder = priceDistributor.getPriceLadder();
        } while (now - startTime < WARMUP_TIME_MSEC);

        histogram.reset();

        do {
            processPriceLadderUpdate(priceLadders[ladderPointer++ % ladderCount]);
            now = System.currentTimeMillis();
            //stop any dead code elimination
            priceLadder = priceDistributor.getPriceLadder();
        } while (now - startTime < RUN_TIME_MSEC);

        System.out.println("Recorded latencies [in usec] for processing PriceLadders with PriceLadderProcessorNoBranching:");

        histogram.outputPercentileDistribution(System.out, 1000.0);
    }
}
