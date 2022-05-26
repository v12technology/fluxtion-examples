package com.fluxtion.example.unplugged.part1;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.SEPConfig;
import com.fluxtion.example.unplugged.part1.Trade.AssetPrice;
import com.fluxtion.example.unplugged.part1.Trade.TradeLeg;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.stream.groupby.GroupBy;
import com.fluxtion.runtime.stream.groupby.GroupBy.KeyValue;
import com.fluxtion.runtime.stream.groupby.GroupByStreamed;
import com.fluxtion.runtime.stream.helpers.Aggregates;
import com.fluxtion.runtime.stream.helpers.Predicates;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

import static com.fluxtion.compiler.builder.stream.EventFlow.subscribe;
import static com.fluxtion.compiler.builder.stream.EventFlow.subscribeToSignal;

/**
 * Provides a Trading calculator service interface. An internal event processor is built that supports all the
 * trading calculations. A service interface transforms method calls into events to make the processor easier
 * to work with for clients.
 *
 * functionality:
 * <ul>
 *     <li>Process trades and calculation positions for individual asset</li>
 *     <li>An FX trade has two legs, so one trade will create two position entries</li>
 *     <li>Process market rates and calculate the value of an assets positions</li>
 *     <li>The value is with reference to a base currency of USD</li>
 *     <li>Listeners can register for positions, mark to market for each position and a global net profit aggregate</li>
 *     <li>Only publish updates to a listener if a there is a change to the relevant subscription</li>
 *     <li>Provide a reset control action that resets positions and market data</li>
 *     <li></li>
 * </ul>
 */
public class TradingCalculator {

    private final EventProcessor streamProcessor;
    private static final String baseCurrency = "USD";

    public TradingCalculator() {
        streamProcessor = Fluxtion.interpret(this::buildProcessor);
        streamProcessor.init();
    }

    public void processTrade(Trade trade) {
        System.out.println("\nrcvd trade -> " + trade);
        streamProcessor.onEvent(trade);
        streamProcessor.publishSignal("publish");
    }

    public void processTrades(Trade trade, Trade... trades) {
        System.out.println("\nTrade batch - start\n\trcvd trade -> " + trade);
        streamProcessor.onEvent(trade);
        for (Trade batchTrade: trades) {
            System.out.println("\trcvd trade -> " + batchTrade);
            streamProcessor.onEvent(batchTrade);
        }
        System.out.println("Trade batch - complete");
        streamProcessor.publishSignal("publish");
    }

    public void priceUpdate(PairPrice price) {
        System.out.println("\nrcvd price -> " + price);
        streamProcessor.onEvent(price);
        streamProcessor.publishSignal("publish");
    }

    public void reset() {
        System.out.println("\nreset");
        streamProcessor.publishSignal("reset");
        streamProcessor.publishSignal("publish");
    }

    public void markToMarketConsumer(Consumer<Map<String, Double>> listener) {
        streamProcessor.addSink("mtm", listener);
    }

    public void positionsConsumer(Consumer<Map<String, Double>> listener) {
        streamProcessor.addSink("positions", listener);
    }

    public void profitConsumer(DoubleConsumer listener) {
        streamProcessor.addSink("profit", listener);
    }

    /**
     * Build the actual processing graph
     *
     */
    private void buildProcessor(SEPConfig config) {
        var resetTrigger = subscribeToSignal("reset");
        var publishTrigger = subscribeToSignal("publish");

        var assetPosition = subscribe(Trade.class)
                .flatMap(Trade::tradeLegs)
                .groupBy(TradeLeg::id, TradeLeg::amount, Aggregates.doubleSum())
                .resetTrigger(resetTrigger);

        var assetPriceMap = subscribe(PairPrice.class)
                .map(TradingCalculator::toCrossRate)
                .groupBy(Trade.AssetPrice::id, Trade.AssetPrice::price)
                .resetTrigger(resetTrigger);

        var posDrivenMtmStream = assetPosition.map(GroupByStreamed::keyValue)
                .map(TradingCalculator::markToMarketPosition, assetPriceMap.map(GroupBy::map))
                .updateTrigger(assetPosition);

        var priceDrivenMtMStream = assetPriceMap.map(GroupByStreamed::keyValue)
                .map(TradingCalculator::markToMarketPrice, assetPosition.map(GroupBy::map))
                .updateTrigger(assetPriceMap);

        //Mark to market to sink as a map
        var mtm = posDrivenMtmStream.merge(priceDrivenMtMStream)
                .groupBy(KeyValue::getKey, KeyValue::getValueAsDouble)
                .resetTrigger(resetTrigger)
                .map(GroupBy::map)
                .updateTrigger(publishTrigger)
                .filter(Predicates.hasMapChanged())
                .sink("mtm");

        //Positions to sink as a map
        assetPosition.map(GroupBy::map)
                .updateTrigger(publishTrigger)
                .filter(Predicates.hasMapChanged())
                .sink("positions");

        //sum of mtm is profit
        mtm.mapToDouble(TradingCalculator::totalProfit)
                .filter(Predicates.hasDoubleChanged())
                .sink("profit");
    }

    //Helper functions used during event processing
    public static KeyValue<String, Double> markToMarketPrice(
            KeyValue<String, Double> assetPrice, Map<String, Double> assetPositionMap) {
        if (assetPrice == null || assetPositionMap.get(assetPrice.getKey()) == null) {
            return null;
        }
        return new KeyValue<>(assetPrice.getKey(), assetPositionMap.get(assetPrice.getKey()) * assetPrice.getValue());
    }

    public static KeyValue<String, Double> markToMarketPosition(
            KeyValue<String, Double> assetPosition, Map<String, Double> assetPriceMap) {
        if (assetPosition == null) {
            return null;
        }
        if (assetPosition.getKey().equals(baseCurrency)) {
            return new KeyValue<>(assetPosition.getKey(), assetPosition.getValue());
        }
        if(assetPriceMap == null){
            return new KeyValue<>(assetPosition.getKey(), Double.NaN);
        }
        return new KeyValue<>(
                assetPosition.getKey(),
                assetPriceMap.getOrDefault(assetPosition.getKey(), Double.NaN) * assetPosition.getValue());
    }

    public static double totalProfit(Map<String, Double> m) {
        return m.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public static AssetPrice toCrossRate(PairPrice pairPrice) {
        if (pairPrice.id().startsWith(baseCurrency)) {
            return (new AssetPrice(pairPrice.id().substring(3), 1.0 / pairPrice.price()));
        }
        return (new AssetPrice(pairPrice.id().substring(0, 3), pairPrice.price()));
    }
}
