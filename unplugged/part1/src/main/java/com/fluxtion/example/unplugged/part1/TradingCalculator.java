package com.fluxtion.example.unplugged.part1;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.builder.stream.EventStreamBuilder;
import com.fluxtion.example.unplugged.part1.Trade.AssetPrice;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.stream.aggregate.functions.AggregateDoubleSum;
import com.fluxtion.runtime.stream.groupby.GroupBy;
import com.fluxtion.runtime.stream.groupby.GroupBy.KeyValue;
import com.fluxtion.runtime.stream.groupby.GroupByStreamed;
import com.fluxtion.runtime.stream.helpers.Aggregates;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import static com.fluxtion.compiler.builder.stream.EventFlow.subscribe;

public class TradingCalculator {

    private final EventProcessor streamProcessor;

    public void processTrade(Trade trade) {
        streamProcessor.onEvent(trade);
        streamProcessor.onEvent("publish");
    }

    public void priceUpdate(PairPrice price) {
        streamProcessor.onEvent(price);
        streamProcessor.onEvent("publish");
    }

    public void reset() {
        streamProcessor.onEvent("reset");
        streamProcessor.onEvent("publish");
    }

    public void markToMarketListener(Consumer<Map<String, Double>> listener) {
        streamProcessor.addSink("mtm", listener);
    }

    public void positionsListener(Consumer<Map<String, Double>> listener) {
        streamProcessor.addSink("positions", listener);
    }

    public TradingCalculator() {
        streamProcessor = Fluxtion.interpret(c -> {
            EventStreamBuilder<GroupByStreamed<String, Double>> assetPosition = subscribe(Trade.class)
                    .flatMap(Trade::tradeLegs)
                    .groupBy(Trade.AssetAmountTraded::getId, Trade.AssetAmountTraded::getAmount, AggregateDoubleSum::new)
                    .resetTrigger(subscribe(String.class).filter("reset"::equalsIgnoreCase));

            EventStreamBuilder<GroupByStreamed<String, Double>> assetPriceMap = subscribe(PairPrice.class)
                    .flatMap(new ConvertToBasePrice("USD")::toCrossRate)
                    .groupBy(Trade.AssetPrice::getId, Trade.AssetPrice::getPrice, AggregateDoubleSum::new)
                    .resetTrigger(subscribe(String.class).filter("reset"::equalsIgnoreCase));

            EventStreamBuilder<KeyValue<String, Double>> posDrivenMtmStream = assetPosition.map(GroupByStreamed::keyValue)
                    .map(TradingCalculator::markToMarket, assetPriceMap.map(GroupBy::map));

            EventStreamBuilder<KeyValue<String, Double>> priceDrivenMtMStream = assetPriceMap.map(GroupByStreamed::keyValue)
                    .map(TradingCalculator::markToMarket, assetPosition.map(GroupBy::map)).updateTrigger(assetPriceMap);

            //Mark to market
            posDrivenMtmStream.merge(priceDrivenMtMStream)
                    .groupBy(KeyValue::getKey, KeyValue::getValueAsDouble, Aggregates.doubleIdentity())
                    .resetTrigger(subscribe(String.class).filter("reset"::equalsIgnoreCase))
                    .map(GroupBy::map)
                    .defaultValue(Collections::emptyMap)
                    .updateTrigger(subscribe(String.class).filter("publish"::equalsIgnoreCase))
                    .sink("mtm");

            //Positions
            assetPosition.map(GroupBy::map)
                    .defaultValue(Collections::emptyMap)
                    .updateTrigger(subscribe(String.class).filter("publish"::equalsIgnoreCase))
                    .filter(Objects::nonNull)
                    .sink("positions");
        });
        streamProcessor.init();
    }

    public static KeyValue<String, Double> markToMarket(KeyValue<String, Double> assetPosition, Map<String, Double> assetPriceMap) {
        Double price = assetPriceMap.getOrDefault(assetPosition.getKey(), Double.NaN);
        return new KeyValue<>(assetPosition.getKey(), price * assetPosition.getValue());
    }

    @EqualsAndHashCode
    public static class ConvertToBasePrice {
        private final String baseCurrency;
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
            if (pairPrice.getId().startsWith(baseCurrency)) {
                list.add(new AssetPrice(pairPrice.getId().substring(3), 1.0 / pairPrice.getPrice()));
            } else if (pairPrice.getId().contains(baseCurrency)) {
                list.add(new AssetPrice(pairPrice.getId().substring(0, 3), pairPrice.getPrice()));
            }
            hasPublished = true;
            return list;
        }
    }
}
