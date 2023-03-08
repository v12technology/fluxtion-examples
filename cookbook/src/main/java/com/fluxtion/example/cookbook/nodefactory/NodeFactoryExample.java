package com.fluxtion.example.cookbook.nodefactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.compiler.RootNodeConfig;
import com.fluxtion.compiler.builder.factory.NodeFactoryRegistration;
import com.fluxtion.example.cookbook.nodefactory.config.MarketDataSupplierConfig;
import com.fluxtion.example.cookbook.nodefactory.config.MarketStatsCalculatorConfig;
import com.fluxtion.example.cookbook.nodefactory.config.SmoothedMarketRateConfig;
import com.fluxtion.example.cookbook.nodefactory.event.MarketUpdate;
import com.fluxtion.example.cookbook.nodefactory.factory.MarketDataNodeFactory;
import com.fluxtion.example.cookbook.nodefactory.factory.MarketStatsCalculatorFactory;
import com.fluxtion.example.cookbook.nodefactory.factory.SmoothedMarketRateFactory;
import com.fluxtion.example.cookbook.nodefactory.node.MarketStatsCalculator;
import com.fluxtion.runtime.EventProcessor;
import lombok.SneakyThrows;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Build an event processor using factories and config only. No imperative building of nodes, no calls to
 * {@link com.fluxtion.compiler.EventProcessorConfig#addNode(Object)}.
 *
 * Running the example builds the graph and sends market data for processing:
 * <ul>
 *     <li>MarketStatsCalculator prints out the stats for several smoothed mid price calculator</li>
 *     <li>A SmoothedMarketRate calculates a moving average for {@link MarketDataSupplier#midPrice()}</li>
 *     <li>A SmoothedMarketRate has a unique time window size and number of buckets for the calculation</li>
 *     <li>A MarketDataSupplier either listens to a market directly or is a cross calcualtion</li>
 *     <li>If the market cannot be subscribed to directly the {@link MarketDataNodeFactory} supplies a {@link com.fluxtion.example.cookbook.nodefactory.node.MarketDataCrossNode}</li>
 * </ul>
 *
 * Each node has an associated {@link com.fluxtion.compiler.builder.factory.NodeFactory} that builds the node. The factory
 * has access to other factories via {@link com.fluxtion.compiler.builder.factory.NodeRegistry#findOrCreateNode(Class, Map, String)}
 * to create dependencies needed to build the current node.
 *
 * The configuration is passed as a map to the root instance. Subsequent findOrCreateNode calls receive the map the currently
 * executing NodeFactory wants to provide.
 *
 */
public class NodeFactoryExample {

    private final static boolean programmaticConfig = false;
    private final static boolean interpret = true;
    private static EventProcessor<?> eventProcessor;
    public static String config = """
            {
              "smoothedMarketRateConfigList": [
                {
                  "publishRate": 1000,
                  "windowSize": 10,
                  "name": "smoothedEURUSD_1s",
                  "marketDataSupplier": {
                    "symbol": "EURUSD"
                  }
                },
                {
                  "publishRate": 5000,
                  "windowSize": 5,
                  "name": "smoothedEURUSD_5s",
                  "marketDataSupplier": {
                    "symbol": "EURUSD"
                  }
                },
                {
                  "publishRate": 1000,
                  "windowSize": 6,
                  "name": "smoothedGBPDKK_1s",
                  "marketDataSupplier": {
                    "symbol": "GBPDKK"
                  }
                }
              ],
              "reportingIntervalSeconds": 2
            }
            """;

    public static void main(String[] args) {
        if(interpret){
            eventProcessor = Fluxtion.interpret(c -> {
                c.setRootNodeConfig(builderConfig());
                c.setNodeFactoryRegistration(nodeFactories());
            });
        }else{
            eventProcessor = Fluxtion.compileAot(c -> {
                c.setRootNodeConfig(builderConfig());
                c.setNodeFactoryRegistration(nodeFactories());
            }, "com.fluxtion.example.cookbook.nodefactory.generated", "Processor");
        }
        eventProcessor.init();
        System.out.println("generated processor - sending market data");
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(NodeFactoryExample::publishMarketData, 0, 200, TimeUnit.MILLISECONDS);
    }

    private static NodeFactoryRegistration nodeFactories() {
        return new NodeFactoryRegistration(
                new SmoothedMarketRateFactory(),
                new MarketDataNodeFactory(),
                new MarketStatsCalculatorFactory());
    }

    @SneakyThrows
    public static RootNodeConfig builderConfig() {
        final MarketStatsCalculatorConfig statsCalculatorConfig;
        if(programmaticConfig){
            //various publishers
            SmoothedMarketRateConfig publisherConfig = new SmoothedMarketRateConfig(
                    1_000, 10, "smoothedEURUSD_1s", new MarketDataSupplierConfig("EURUSD"));
            SmoothedMarketRateConfig publisherConfig_EURUSD_5s = new SmoothedMarketRateConfig(
                    5_000, 5, "smoothedEURUSD_5s", new MarketDataSupplierConfig("EURUSD"));
            SmoothedMarketRateConfig publisherConfig_GBPDKK_1s = new SmoothedMarketRateConfig(
                    1_000, 6, "smoothedGBPDKK_1s", new MarketDataSupplierConfig("GBPDKK"));
            //root config
            statsCalculatorConfig = new MarketStatsCalculatorConfig(
                    List.of(publisherConfig, publisherConfig_EURUSD_5s, publisherConfig_GBPDKK_1s), 2);
        }else{
            ObjectMapper mapper = new ObjectMapper();
            statsCalculatorConfig =  mapper.readValue(config, MarketStatsCalculatorConfig.class);
        }
        Class<?> rootClass = MarketStatsCalculator.class;
        Map<String, Object> configMap = Map.of(MarketStatsCalculatorConfig.class.getCanonicalName(), statsCalculatorConfig);
        String processorName = "marketStatsPublisher";
        List<Object> nodes = Collections.emptyList();
        return new RootNodeConfig(processorName, rootClass, configMap, nodes);
    }

    private static void publishMarketData() {
        Random random = new Random();
        eventProcessor.onEvent(new MarketUpdate("EURGBP", 0.89 + random.nextInt(100) * 0.001));
        eventProcessor.onEvent(new MarketUpdate("GBPUSD", 1.18 + random.nextInt(100) * 0.001));
        eventProcessor.onEvent(new MarketUpdate("EURUSD", 1.05 + random.nextInt(100) * 0.001));
        eventProcessor.onEvent(new MarketUpdate("EURDKK", 7.44 + random.nextInt(100) * 0.001));
    }
}
