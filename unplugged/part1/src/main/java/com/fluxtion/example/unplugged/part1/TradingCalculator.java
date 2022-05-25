package com.fluxtion.example.unplugged.part1;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.SEPConfig;
import com.fluxtion.example.unplugged.part1.Trade.AssetPrice;
import com.fluxtion.example.unplugged.part1.Trade.TradeLeg;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.event.Signal;
import com.fluxtion.runtime.stream.groupby.GroupBy;
import com.fluxtion.runtime.stream.groupby.GroupBy.KeyValue;
import com.fluxtion.runtime.stream.groupby.GroupByStreamed;
import com.fluxtion.runtime.stream.helpers.Aggregates;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.fluxtion.compiler.builder.stream.EventFlow.subscribe;
import static com.fluxtion.compiler.builder.stream.EventFlow.subscribeToSignal;

public class TradingCalculator {

    private final EventProcessor streamProcessor;

    public TradingCalculator() {
        streamProcessor = Fluxtion.interpret(this::buildProcessor);
        streamProcessor.init();
    }

    public static KeyValue<String, Double> markToMarket(KeyValue<String, Double> assetPosition, Map<String, Double> assetPriceMap) {
        if (assetPosition == null) {
            return null;
        }
        Double price = assetPriceMap.getOrDefault(assetPosition.getKey(), Double.NaN);
        return new KeyValue<>(assetPosition.getKey(), price * assetPosition.getValue());
    }

    public void processTrade(Trade trade) {
        streamProcessor.onEvent(trade);
        streamProcessor.publishSignal("publish");
    }

    public void priceUpdate(PairPrice price) {
        streamProcessor.onEvent(price);
        streamProcessor.publishSignal("publish");
    }

    public void reset() {
        streamProcessor.publishSignal("reset");
        streamProcessor.publishSignal("publish");
    }

    public void markToMarketListener(Consumer<Map<String, Double>> listener) {
        streamProcessor.addSink("mtm", listener);
    }

    public void positionsListener(Consumer<Map<String, Double>> listener) {
        streamProcessor.addSink("positions", listener);
    }

    private void buildProcessor(SEPConfig config) {
        var resetTrigger = subscribeToSignal("reset");
        var publishTrigger = subscribeToSignal("publish");

        var assetPosition = subscribe(Trade.class)
                .flatMap(Trade::tradeLegs)
                .groupBy(TradeLeg::id, TradeLeg::amount, Aggregates.doubleSum())
                .resetTrigger(resetTrigger);

        var assetPriceMap = subscribe(PairPrice.class)
                .flatMap(new ConvertToBasePrice("USD")::toCrossRate)
                .groupBy(Trade.AssetPrice::id, Trade.AssetPrice::price, Aggregates.identity())
                .resetTrigger(resetTrigger);

        var posDrivenMtmStream = assetPosition.map(GroupByStreamed::keyValue)
                .map(TradingCalculator::markToMarket, assetPriceMap.map(GroupBy::map));

        var priceDrivenMtMStream = assetPriceMap.map(GroupByStreamed::keyValue)
                .map(TradingCalculator::markToMarket, assetPosition.map(GroupBy::map)).updateTrigger(assetPriceMap);

        //Mark to market to sink as a map
        posDrivenMtmStream.merge(priceDrivenMtMStream)
                .groupBy(KeyValue::getKey, KeyValue::getValueAsDouble, Aggregates.identity())
                .resetTrigger(resetTrigger)
                .map(GroupBy::map)
                .defaultValue(Collections::emptyMap)
                .updateTrigger(publishTrigger)
                .sink("mtm");

        //Positions to sink as a map
        assetPosition.map(GroupBy::map)
                .defaultValue(Collections::emptyMap)
                .updateTrigger(publishTrigger)
                .sink("positions");
    }

    @EqualsAndHashCode
    public static class ConvertToBasePrice {
        private String baseCurrency;
        private transient boolean hasPublished = false;

        public ConvertToBasePrice() {
            this("USD");
        }

        public ConvertToBasePrice(String baseCurrency) {
            this.baseCurrency = baseCurrency;
        }

        public List<AssetPrice> toCrossRate(PairPrice pairPrice) {
            List<AssetPrice> list = new ArrayList<>();
            if (!hasPublished) {
                list.add(new AssetPrice(baseCurrency, 1.0));
            }
            if (pairPrice.id().startsWith(baseCurrency)) {
                list.add(new AssetPrice(pairPrice.id().substring(3), 1.0 / pairPrice.price()));
            } else if (pairPrice.id().contains(baseCurrency)) {
                list.add(new AssetPrice(pairPrice.id().substring(0, 3), pairPrice.price()));
            }
            hasPublished = true;
            return list;
        }

        @OnEventHandler(filterString = "reset")
        public void reset(Signal<Object> reset) {
            hasPublished = false;
        }

        @OnEventHandler(filterString = "baseCurrency")
        public void updateBaseCurrency(Signal<String> signal) {
            this.baseCurrency = signal.getValue();
        }
    }
}
