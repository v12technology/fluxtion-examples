/*
* Copyright (C) 2018 V12 Technology Ltd.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the Server Side Public License, version 1,
* as published by MongoDB, Inc.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* Server Side License for more details.
*
* You should have received a copy of the Server Side Public License
* along with this program.  If not, see
*
<http://www.mongodb.com/licensing/server-side-public-license>.
*/
package com.fluxtion.example.cookbook.nodefactory.generated;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.example.cookbook.nodefactory.event.MarketUpdate;
import com.fluxtion.example.cookbook.nodefactory.node.MarketDataCrossNode;
import com.fluxtion.example.cookbook.nodefactory.node.MarketDataNode;
import com.fluxtion.example.cookbook.nodefactory.node.MarketStatsCalculator;
import com.fluxtion.example.cookbook.nodefactory.node.SmoothedMarketRate;
import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.audit.NodeNameAuditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.input.EventFeed;
import com.fluxtion.runtime.input.SubscriptionManagerNode;
import com.fluxtion.runtime.node.MutableEventProcessorContext;
import com.fluxtion.runtime.node.MutableEventProcessorContext;
import com.fluxtion.runtime.time.Clock;
import com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent;
import com.fluxtion.runtime.time.FixedRateTrigger;
import java.util.Arrays;
import java.util.Map;

import java.util.IdentityHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/*
 *
 * <pre>
 * generation time                 : 2023-03-09T12:10:53.492424
 * eventProcessorGenerator version : 8.0.5
 * api version                     : 8.0.5
 * </pre>
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public class Processor
    implements EventProcessor<Processor>,
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle {

  //Node declarations
  private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  public final Clock clock = new Clock();
  private final FixedRateTrigger fixedRateTrigger_1 = new FixedRateTrigger(clock, 1000);
  private final FixedRateTrigger fixedRateTrigger_2 = new FixedRateTrigger(clock, 5000);
  private final FixedRateTrigger fixedRateTrigger_3 = new FixedRateTrigger(clock, 1000);
  private final FixedRateTrigger fixedRateTrigger_4 = new FixedRateTrigger(clock, 2000);
  private final MarketDataNode marketDataNode_EURDKK = new MarketDataNode("EURDKK");
  private final MarketDataNode marketDataNode_EURGBP = new MarketDataNode("EURGBP");
  private final MarketDataCrossNode crossMarketDataNode_GBPDKK =
      new MarketDataCrossNode("GBPDKK", marketDataNode_EURGBP, marketDataNode_EURDKK);
  private final MarketDataNode marketDataNode_EURUSD = new MarketDataNode("EURUSD");
  public final NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final SmoothedMarketRate smoothedEURUSD_1s =
      new SmoothedMarketRate("smoothedEURUSD_1s", fixedRateTrigger_1, marketDataNode_EURUSD, 10);
  private final SmoothedMarketRate smoothedEURUSD_5s =
      new SmoothedMarketRate("smoothedEURUSD_5s", fixedRateTrigger_2, marketDataNode_EURUSD, 5);
  private final SmoothedMarketRate smoothedGBPDKK_1s =
      new SmoothedMarketRate(
          "smoothedGBPDKK_1s", fixedRateTrigger_3, crossMarketDataNode_GBPDKK, 6);
  public final MarketStatsCalculator marketStatsPublisher =
      new MarketStatsCalculator(
          Arrays.asList(smoothedEURUSD_1s, smoothedEURUSD_5s, smoothedGBPDKK_1s),
          fixedRateTrigger_4);
  private final SubscriptionManagerNode subscriptionManager = new SubscriptionManagerNode();
  private final MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  //Dirty flags
  private boolean processing = false;
  private boolean buffering = false;
  private final IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(6);
  private final IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(6);

  private boolean isDirty_fixedRateTrigger_1 = false;
  private boolean isDirty_fixedRateTrigger_2 = false;
  private boolean isDirty_fixedRateTrigger_3 = false;
  private boolean isDirty_fixedRateTrigger_4 = false;
  private boolean isDirty_marketDataNode_EURDKK = false;
  private boolean isDirty_marketDataNode_EURGBP = false;
  //Filter constants

  public Processor(Map<Object, Object> contextMap) {
    context.replaceMappings(contextMap);
    //node auditors
    initialiseAuditor(clock);
    initialiseAuditor(nodeNameLookup);
    subscriptionManager.setSubscribingEventProcessor(this);
    context.setEventProcessorCallback(this);
  }

  public Processor() {
    this(null);
  }

  @Override
  public void setContextParameterMap(Map<Object, Object> newContextMapping) {
    context.replaceMappings(newContextMapping);
  }

  @Override
  public void addContextParameter(Object key, Object value) {
    context.addMapping(key, value);
  }

  @Override
  public void onEvent(Object event) {
    if (buffering) {
      triggerCalculation();
    }
    if (processing) {
      callbackDispatcher.processReentrantEvent(event);
    } else {
      processing = true;
      onEventInternal(event);
      callbackDispatcher.dispatchQueuedCallbacks();
      processing = false;
    }
  }

  public void onEventInternal(Object event) {
    switch (event.getClass().getName()) {
      case ("com.fluxtion.example.cookbook.nodefactory.event.MarketUpdate"):
        {
          MarketUpdate typedEvent = (MarketUpdate) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.runtime.time.ClockStrategy$ClockStrategyEvent"):
        {
          ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
          handleEvent(typedEvent);
          break;
        }
      case ("java.lang.Object"):
        {
          Object typedEvent = (Object) event;
          handleEvent(typedEvent);
          break;
        }
      default:
        {
          handleEvent(event);
        }
    }
  }

  public void handleEvent(MarketUpdate typedEvent) {
    auditEvent(typedEvent);
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.example.cookbook.nodefactory.event.MarketUpdate] filterString:[EURDKK]
      case ("EURDKK"):
        isDirty_marketDataNode_EURDKK = marketDataNode_EURDKK.marketUpdate(typedEvent);
        if (guardCheck_crossMarketDataNode_GBPDKK()) {
          crossMarketDataNode_GBPDKK.calculateCrossRate();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.example.cookbook.nodefactory.event.MarketUpdate] filterString:[EURGBP]
      case ("EURGBP"):
        isDirty_marketDataNode_EURGBP = marketDataNode_EURGBP.marketUpdate(typedEvent);
        if (guardCheck_crossMarketDataNode_GBPDKK()) {
          crossMarketDataNode_GBPDKK.calculateCrossRate();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.example.cookbook.nodefactory.event.MarketUpdate] filterString:[EURUSD]
      case ("EURUSD"):
        marketDataNode_EURUSD.marketUpdate(typedEvent);
        afterEvent();
        return;
    }
    //Default, no filter methods
    isDirty_fixedRateTrigger_1 = fixedRateTrigger_1.hasExpired(typedEvent);
    isDirty_fixedRateTrigger_2 = fixedRateTrigger_2.hasExpired(typedEvent);
    isDirty_fixedRateTrigger_3 = fixedRateTrigger_3.hasExpired(typedEvent);
    isDirty_fixedRateTrigger_4 = fixedRateTrigger_4.hasExpired(typedEvent);
    if (guardCheck_smoothedEURUSD_1s()) {
      smoothedEURUSD_1s.calculateSmoothedRate();
    }
    if (guardCheck_smoothedEURUSD_5s()) {
      smoothedEURUSD_5s.calculateSmoothedRate();
    }
    if (guardCheck_smoothedGBPDKK_1s()) {
      smoothedGBPDKK_1s.calculateSmoothedRate();
    }
    if (guardCheck_marketStatsPublisher()) {
      marketStatsPublisher.calculateMarketStats();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ClockStrategyEvent typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    clock.setClockStrategy(typedEvent);
    isDirty_fixedRateTrigger_1 = fixedRateTrigger_1.hasExpired(typedEvent);
    isDirty_fixedRateTrigger_1 = fixedRateTrigger_1.setClockStrategy(typedEvent);
    isDirty_fixedRateTrigger_2 = fixedRateTrigger_2.hasExpired(typedEvent);
    isDirty_fixedRateTrigger_2 = fixedRateTrigger_2.setClockStrategy(typedEvent);
    isDirty_fixedRateTrigger_3 = fixedRateTrigger_3.hasExpired(typedEvent);
    isDirty_fixedRateTrigger_3 = fixedRateTrigger_3.setClockStrategy(typedEvent);
    isDirty_fixedRateTrigger_4 = fixedRateTrigger_4.hasExpired(typedEvent);
    isDirty_fixedRateTrigger_4 = fixedRateTrigger_4.setClockStrategy(typedEvent);
    if (guardCheck_smoothedEURUSD_1s()) {
      smoothedEURUSD_1s.calculateSmoothedRate();
    }
    if (guardCheck_smoothedEURUSD_5s()) {
      smoothedEURUSD_5s.calculateSmoothedRate();
    }
    if (guardCheck_smoothedGBPDKK_1s()) {
      smoothedGBPDKK_1s.calculateSmoothedRate();
    }
    if (guardCheck_marketStatsPublisher()) {
      marketStatsPublisher.calculateMarketStats();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(Object typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_fixedRateTrigger_1 = fixedRateTrigger_1.hasExpired(typedEvent);
    isDirty_fixedRateTrigger_2 = fixedRateTrigger_2.hasExpired(typedEvent);
    isDirty_fixedRateTrigger_3 = fixedRateTrigger_3.hasExpired(typedEvent);
    isDirty_fixedRateTrigger_4 = fixedRateTrigger_4.hasExpired(typedEvent);
    if (guardCheck_smoothedEURUSD_1s()) {
      smoothedEURUSD_1s.calculateSmoothedRate();
    }
    if (guardCheck_smoothedEURUSD_5s()) {
      smoothedEURUSD_5s.calculateSmoothedRate();
    }
    if (guardCheck_smoothedGBPDKK_1s()) {
      smoothedGBPDKK_1s.calculateSmoothedRate();
    }
    if (guardCheck_marketStatsPublisher()) {
      marketStatsPublisher.calculateMarketStats();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void bufferEvent(Object event) {
    buffering = true;
    switch (event.getClass().getName()) {
      case ("com.fluxtion.runtime.time.ClockStrategy$ClockStrategyEvent"):
        {
          ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
          auditEvent(typedEvent);
          clock.setClockStrategy(typedEvent);
          isDirty_fixedRateTrigger_1 = fixedRateTrigger_1.hasExpired(typedEvent);
          isDirty_fixedRateTrigger_1 = fixedRateTrigger_1.setClockStrategy(typedEvent);
          isDirty_fixedRateTrigger_2 = fixedRateTrigger_2.hasExpired(typedEvent);
          isDirty_fixedRateTrigger_2 = fixedRateTrigger_2.setClockStrategy(typedEvent);
          isDirty_fixedRateTrigger_3 = fixedRateTrigger_3.hasExpired(typedEvent);
          isDirty_fixedRateTrigger_3 = fixedRateTrigger_3.setClockStrategy(typedEvent);
          isDirty_fixedRateTrigger_4 = fixedRateTrigger_4.hasExpired(typedEvent);
          isDirty_fixedRateTrigger_4 = fixedRateTrigger_4.setClockStrategy(typedEvent);
          //event stack unwind callbacks
          break;
        }
      case ("java.lang.Object"):
        {
          Object typedEvent = (Object) event;
          auditEvent(typedEvent);
          isDirty_fixedRateTrigger_1 = fixedRateTrigger_1.hasExpired(typedEvent);
          isDirty_fixedRateTrigger_2 = fixedRateTrigger_2.hasExpired(typedEvent);
          isDirty_fixedRateTrigger_3 = fixedRateTrigger_3.hasExpired(typedEvent);
          isDirty_fixedRateTrigger_4 = fixedRateTrigger_4.hasExpired(typedEvent);
          //event stack unwind callbacks
          break;
        }
      case ("com.fluxtion.example.cookbook.nodefactory.event.MarketUpdate"):
        {
          MarketUpdate typedEvent = (MarketUpdate) event;
          auditEvent(typedEvent);
          switch (typedEvent.filterString()) {
              //Event Class:[com.fluxtion.example.cookbook.nodefactory.event.MarketUpdate] filterString:[EURDKK]
            case ("EURDKK"):
              isDirty_marketDataNode_EURDKK = marketDataNode_EURDKK.marketUpdate(typedEvent);
              //event stack unwind callbacks
              afterEvent();
              return;
              //Event Class:[com.fluxtion.example.cookbook.nodefactory.event.MarketUpdate] filterString:[EURGBP]
            case ("EURGBP"):
              isDirty_marketDataNode_EURGBP = marketDataNode_EURGBP.marketUpdate(typedEvent);
              //event stack unwind callbacks
              afterEvent();
              return;
              //Event Class:[com.fluxtion.example.cookbook.nodefactory.event.MarketUpdate] filterString:[EURUSD]
            case ("EURUSD"):
              marketDataNode_EURUSD.marketUpdate(typedEvent);
              //event stack unwind callbacks
              afterEvent();
              return;
          }
          isDirty_fixedRateTrigger_1 = fixedRateTrigger_1.hasExpired(typedEvent);
          isDirty_fixedRateTrigger_2 = fixedRateTrigger_2.hasExpired(typedEvent);
          isDirty_fixedRateTrigger_3 = fixedRateTrigger_3.hasExpired(typedEvent);
          isDirty_fixedRateTrigger_4 = fixedRateTrigger_4.hasExpired(typedEvent);
          //event stack unwind callbacks
          break;
        }
    }
  }

  public void triggerCalculation() {
    buffering = false;
    String typedEvent = "No event information - buffered dispatch";
    if (guardCheck_crossMarketDataNode_GBPDKK()) {
      crossMarketDataNode_GBPDKK.calculateCrossRate();
    }
    if (guardCheck_smoothedEURUSD_1s()) {
      smoothedEURUSD_1s.calculateSmoothedRate();
    }
    if (guardCheck_smoothedEURUSD_5s()) {
      smoothedEURUSD_5s.calculateSmoothedRate();
    }
    if (guardCheck_smoothedGBPDKK_1s()) {
      smoothedGBPDKK_1s.calculateSmoothedRate();
    }
    if (guardCheck_marketStatsPublisher()) {
      marketStatsPublisher.calculateMarketStats();
    }
    afterEvent();
  }

  private void auditEvent(Object typedEvent) {
    clock.eventReceived(typedEvent);
    nodeNameLookup.eventReceived(typedEvent);
  }

  private void auditEvent(Event typedEvent) {
    clock.eventReceived(typedEvent);
    nodeNameLookup.eventReceived(typedEvent);
  }

  private void initialiseAuditor(Auditor auditor) {
    auditor.init();
    auditor.nodeRegistered(crossMarketDataNode_GBPDKK, "crossMarketDataNode_GBPDKK");
    auditor.nodeRegistered(marketDataNode_EURDKK, "marketDataNode_EURDKK");
    auditor.nodeRegistered(marketDataNode_EURGBP, "marketDataNode_EURGBP");
    auditor.nodeRegistered(marketDataNode_EURUSD, "marketDataNode_EURUSD");
    auditor.nodeRegistered(marketStatsPublisher, "marketStatsPublisher");
    auditor.nodeRegistered(smoothedEURUSD_1s, "smoothedEURUSD_1s");
    auditor.nodeRegistered(smoothedEURUSD_5s, "smoothedEURUSD_5s");
    auditor.nodeRegistered(smoothedGBPDKK_1s, "smoothedGBPDKK_1s");
    auditor.nodeRegistered(callbackDispatcher, "callbackDispatcher");
    auditor.nodeRegistered(subscriptionManager, "subscriptionManager");
    auditor.nodeRegistered(context, "context");
    auditor.nodeRegistered(fixedRateTrigger_1, "fixedRateTrigger_1");
    auditor.nodeRegistered(fixedRateTrigger_2, "fixedRateTrigger_2");
    auditor.nodeRegistered(fixedRateTrigger_3, "fixedRateTrigger_3");
    auditor.nodeRegistered(fixedRateTrigger_4, "fixedRateTrigger_4");
  }

  private void afterEvent() {
    clock.processingComplete();
    nodeNameLookup.processingComplete();
    isDirty_fixedRateTrigger_1 = false;
    isDirty_fixedRateTrigger_2 = false;
    isDirty_fixedRateTrigger_3 = false;
    isDirty_fixedRateTrigger_4 = false;
    isDirty_marketDataNode_EURDKK = false;
    isDirty_marketDataNode_EURGBP = false;
  }

  @Override
  public void init() {
    //initialise dirty lookup map
    isDirty("test");
    clock.init();
    fixedRateTrigger_1.init();
    fixedRateTrigger_2.init();
    fixedRateTrigger_3.init();
    fixedRateTrigger_4.init();
    crossMarketDataNode_GBPDKK.init();
    smoothedEURUSD_1s.init();
    smoothedEURUSD_5s.init();
    smoothedGBPDKK_1s.init();
  }

  @Override
  public void tearDown() {
    nodeNameLookup.tearDown();
    clock.tearDown();
    subscriptionManager.tearDown();
  }

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}

  @Override
  public boolean isDirty(Object node) {
    if (dirtyFlagSupplierMap.isEmpty()) {
      dirtyFlagSupplierMap.put(fixedRateTrigger_1, () -> isDirty_fixedRateTrigger_1);
      dirtyFlagSupplierMap.put(fixedRateTrigger_2, () -> isDirty_fixedRateTrigger_2);
      dirtyFlagSupplierMap.put(fixedRateTrigger_3, () -> isDirty_fixedRateTrigger_3);
      dirtyFlagSupplierMap.put(fixedRateTrigger_4, () -> isDirty_fixedRateTrigger_4);
      dirtyFlagSupplierMap.put(marketDataNode_EURDKK, () -> isDirty_marketDataNode_EURDKK);
      dirtyFlagSupplierMap.put(marketDataNode_EURGBP, () -> isDirty_marketDataNode_EURGBP);
    }
    return dirtyFlagSupplierMap
        .getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE)
        .getAsBoolean();
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(fixedRateTrigger_1, (b) -> isDirty_fixedRateTrigger_1 = b);
      dirtyFlagUpdateMap.put(fixedRateTrigger_2, (b) -> isDirty_fixedRateTrigger_2 = b);
      dirtyFlagUpdateMap.put(fixedRateTrigger_3, (b) -> isDirty_fixedRateTrigger_3 = b);
      dirtyFlagUpdateMap.put(fixedRateTrigger_4, (b) -> isDirty_fixedRateTrigger_4 = b);
      dirtyFlagUpdateMap.put(marketDataNode_EURDKK, (b) -> isDirty_marketDataNode_EURDKK = b);
      dirtyFlagUpdateMap.put(marketDataNode_EURGBP, (b) -> isDirty_marketDataNode_EURGBP = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_crossMarketDataNode_GBPDKK() {
    return isDirty_marketDataNode_EURDKK | isDirty_marketDataNode_EURGBP;
  }

  private boolean guardCheck_marketStatsPublisher() {
    return isDirty_fixedRateTrigger_4;
  }

  private boolean guardCheck_smoothedEURUSD_1s() {
    return isDirty_fixedRateTrigger_1;
  }

  private boolean guardCheck_smoothedEURUSD_5s() {
    return isDirty_fixedRateTrigger_2;
  }

  private boolean guardCheck_smoothedGBPDKK_1s() {
    return isDirty_fixedRateTrigger_3;
  }

  @Override
  public <T> T getNodeById(String id) throws NoSuchFieldException {
    return nodeNameLookup.getInstanceById(id);
  }

  @Override
  public void addEventFeed(EventFeed eventProcessorFeed) {
    subscriptionManager.addEventProcessorFeed(eventProcessorFeed);
  }

  @Override
  public void removeEventFeed(EventFeed eventProcessorFeed) {
    subscriptionManager.removeEventProcessorFeed(eventProcessorFeed);
  }

  @Override
  public Processor newInstance() {
    return new Processor();
  }

  public Processor newInstance(Map<Object, Object> contextMap) {
    return new Processor();
  }
}
