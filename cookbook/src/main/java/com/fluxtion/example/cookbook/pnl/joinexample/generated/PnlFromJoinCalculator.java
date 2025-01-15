/*
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
package com.fluxtion.example.cookbook.pnl.joinexample.generated;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.example.cookbook.pnl.calculator.InstrumentPosMtm;
import com.fluxtion.example.cookbook.pnl.calculator.MtMRateCalculator;
import com.fluxtion.example.cookbook.pnl.calculator.PnlSummaryCalc;
import com.fluxtion.example.cookbook.pnl.calculator.TradeToPositionAggregate;
import com.fluxtion.example.cookbook.pnl.events.MidPrice;
import com.fluxtion.example.cookbook.pnl.events.MtmInstrument;
import com.fluxtion.example.cookbook.pnl.events.Trade;
import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.audit.EventLogManager;
import com.fluxtion.runtime.audit.NodeNameAuditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.callback.ExportFunctionAuditEvent;
import com.fluxtion.runtime.dataflow.function.BinaryMapFlowFunction.BinaryMapToRefFlowFunction;
import com.fluxtion.runtime.dataflow.function.MapFlowFunction.MapRef2RefFlowFunction;
import com.fluxtion.runtime.dataflow.function.PeekFlowFunction;
import com.fluxtion.runtime.dataflow.groupby.GroupBy.EmptyGroupBy;
import com.fluxtion.runtime.dataflow.groupby.GroupByFlowFunctionWrapper;
import com.fluxtion.runtime.dataflow.groupby.GroupByMapFlowFunction;
import com.fluxtion.runtime.dataflow.groupby.OuterJoin;
import com.fluxtion.runtime.dataflow.helpers.Mappers;
import com.fluxtion.runtime.dataflow.helpers.Peekers.TemplateMessage;
import com.fluxtion.runtime.dataflow.helpers.Tuples.MapTuple;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.event.Signal;
import com.fluxtion.runtime.input.EventFeed;
import com.fluxtion.runtime.input.SubscriptionManager;
import com.fluxtion.runtime.input.SubscriptionManagerNode;
import com.fluxtion.runtime.node.DefaultEventHandlerNode;
import com.fluxtion.runtime.node.ForkedTriggerTask;
import com.fluxtion.runtime.node.MutableEventProcessorContext;
import com.fluxtion.runtime.service.ServiceListener;
import com.fluxtion.runtime.service.ServiceRegistryNode;
import com.fluxtion.runtime.time.Clock;
import com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent;
import java.io.File;
import java.util.Map;

import java.util.IdentityHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 *
 *
 * <pre>
 * generation time                 : Not available
 * eventProcessorGenerator version : 9.7.3
 * api version                     : 9.7.3
 * </pre>
 *
 * Event classes supported:
 *
 * <ul>
 *   <li>com.fluxtion.compiler.generation.model.ExportFunctionMarker
 *   <li>com.fluxtion.example.cookbook.pnl.events.MidPrice
 *   <li>com.fluxtion.example.cookbook.pnl.events.MtmInstrument
 *   <li>com.fluxtion.example.cookbook.pnl.events.Trade
 *   <li>com.fluxtion.runtime.event.Signal
 *   <li>com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class PnlFromJoinCalculator
    implements EventProcessor<PnlFromJoinCalculator>,
        /*--- @ExportService start ---*/
        @ExportService ServiceListener,
        /*--- @ExportService end ---*/
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle {

  // Node declarations
  private final transient CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  public final transient Clock clock = new Clock();
  private final transient GroupByFlowFunctionWrapper groupByFlowFunctionWrapper_0 =
      new GroupByFlowFunctionWrapper<>(
          Trade::dealtInstrument, Mappers::identity, TradeToPositionAggregate::aggregateDealt);
  private final transient GroupByFlowFunctionWrapper groupByFlowFunctionWrapper_2 =
      new GroupByFlowFunctionWrapper<>(
          Trade::contraInstrument, Mappers::identity, TradeToPositionAggregate::aggregateContra);
  private final transient MapTuple mapTuple_47 = new MapTuple<>(InstrumentPosMtm::merge);
  private final transient GroupByMapFlowFunction groupByMapFlowFunction_6 =
      new GroupByMapFlowFunction(mapTuple_47::mapTuple);
  private final transient MtMRateCalculator mtMRateCalculator_45 = new MtMRateCalculator();
  private final transient GroupByMapFlowFunction groupByMapFlowFunction_8 =
      new GroupByMapFlowFunction(mtMRateCalculator_45::calculateInstrumentPosMtm);
  public final transient NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final transient OuterJoin outerJoin_4 = new OuterJoin();
  private final transient PnlSummaryCalc pnlSummaryCalc_10 =
      new PnlSummaryCalc(mtMRateCalculator_45);
  private final transient SubscriptionManagerNode subscriptionManager =
      new SubscriptionManagerNode();
  private final transient MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  private final transient DefaultEventHandlerNode handlerTrade =
      new DefaultEventHandlerNode<>(
          2147483647,
          "",
          com.fluxtion.example.cookbook.pnl.events.Trade.class,
          "handlerTrade",
          context);
  private final transient MapRef2RefFlowFunction mapRef2RefFlowFunction_1 =
      new MapRef2RefFlowFunction<>(handlerTrade, groupByFlowFunctionWrapper_0::aggregate);
  private final transient MapRef2RefFlowFunction mapRef2RefFlowFunction_3 =
      new MapRef2RefFlowFunction<>(handlerTrade, groupByFlowFunctionWrapper_2::aggregate);
  private final transient BinaryMapToRefFlowFunction binaryMapToRefFlowFunction_5 =
      new BinaryMapToRefFlowFunction<>(
          mapRef2RefFlowFunction_1, mapRef2RefFlowFunction_3, outerJoin_4::join);
  private final transient MapRef2RefFlowFunction mapRef2RefFlowFunction_7 =
      new MapRef2RefFlowFunction<>(
          binaryMapToRefFlowFunction_5, groupByMapFlowFunction_6::mapValues);
  private final transient MapRef2RefFlowFunction mapRef2RefFlowFunction_9 =
      new MapRef2RefFlowFunction<>(mapRef2RefFlowFunction_7, groupByMapFlowFunction_8::mapValues);
  private final transient MapRef2RefFlowFunction mapRef2RefFlowFunction_11 =
      new MapRef2RefFlowFunction<>(mapRef2RefFlowFunction_9, pnlSummaryCalc_10::updateSummary);
  public final transient ServiceRegistryNode serviceRegistry = new ServiceRegistryNode();
  private final transient TemplateMessage templateMessage_12 = new TemplateMessage<>("{}");
  private final transient PeekFlowFunction peekFlowFunction_13 =
      new PeekFlowFunction<>(
          mapRef2RefFlowFunction_11, templateMessage_12::templateAndLogToConsole);
  private final transient ExportFunctionAuditEvent functionAudit = new ExportFunctionAuditEvent();
  // Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final transient IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(10);
  private final transient IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(10);

  private boolean isDirty_binaryMapToRefFlowFunction_5 = false;
  private boolean isDirty_clock = false;
  private boolean isDirty_handlerTrade = false;
  private boolean isDirty_mapRef2RefFlowFunction_1 = false;
  private boolean isDirty_mapRef2RefFlowFunction_3 = false;
  private boolean isDirty_mapRef2RefFlowFunction_7 = false;
  private boolean isDirty_mapRef2RefFlowFunction_9 = false;
  private boolean isDirty_mapRef2RefFlowFunction_11 = false;
  private boolean isDirty_mtMRateCalculator_45 = false;
  private boolean isDirty_pnlSummaryCalc_10 = false;

  // Forked declarations

  // Filter constants

  // unknown event handler
  private Consumer unKnownEventHandler = (e) -> {};

  public PnlFromJoinCalculator(Map<Object, Object> contextMap) {
    if (context != null) {
      context.replaceMappings(contextMap);
    }
    binaryMapToRefFlowFunction_5.setDefaultValue(new EmptyGroupBy());
    binaryMapToRefFlowFunction_5.setEventProcessorContext(context);
    mapRef2RefFlowFunction_1.setDefaultValue(new EmptyGroupBy());
    mapRef2RefFlowFunction_1.setEventProcessorContext(context);
    mapRef2RefFlowFunction_3.setDefaultValue(new EmptyGroupBy());
    mapRef2RefFlowFunction_3.setEventProcessorContext(context);
    mapRef2RefFlowFunction_7.setEventProcessorContext(context);
    mapRef2RefFlowFunction_7.setPublishTriggerNode(mtMRateCalculator_45);
    mapRef2RefFlowFunction_9.setEventProcessorContext(context);
    mapRef2RefFlowFunction_11.setEventProcessorContext(context);
    peekFlowFunction_13.setEventProcessorContext(context);
    templateMessage_12.clock = clock;
    context.setClock(clock);
    serviceRegistry.setEventProcessorContext(context);
    // node auditors
    initialiseAuditor(clock);
    initialiseAuditor(nodeNameLookup);
    initialiseAuditor(serviceRegistry);
    if (subscriptionManager != null) {
      subscriptionManager.setSubscribingEventProcessor(this);
    }
    if (context != null) {
      context.setEventProcessorCallback(this);
    }
  }

  public PnlFromJoinCalculator() {
    this(null);
  }

  @Override
  public void init() {
    initCalled = true;
    auditEvent(Lifecycle.LifecycleEvent.Init);
    // initialise dirty lookup map
    isDirty("test");
    clock.init();
    handlerTrade.init();
    mapRef2RefFlowFunction_1.initialiseEventStream();
    mapRef2RefFlowFunction_3.initialiseEventStream();
    binaryMapToRefFlowFunction_5.initialiseEventStream();
    mapRef2RefFlowFunction_7.initialiseEventStream();
    mapRef2RefFlowFunction_9.initialiseEventStream();
    mapRef2RefFlowFunction_11.initialiseEventStream();
    templateMessage_12.initialise();
    peekFlowFunction_13.initialiseEventStream();
    afterEvent();
  }

  @Override
  public void start() {
    if (!initCalled) {
      throw new RuntimeException("init() must be called before start()");
    }
    processing = true;
    auditEvent(Lifecycle.LifecycleEvent.Start);
    templateMessage_12.start();
    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
  }

  @Override
  public void startComplete() {
    if (!initCalled) {
      throw new RuntimeException("init() must be called before startComplete()");
    }
    processing = true;
    auditEvent(Lifecycle.LifecycleEvent.StartComplete);

    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
  }

  @Override
  public void stop() {
    if (!initCalled) {
      throw new RuntimeException("init() must be called before stop()");
    }
    processing = true;
    auditEvent(Lifecycle.LifecycleEvent.Stop);

    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
  }

  @Override
  public void tearDown() {
    initCalled = false;
    auditEvent(Lifecycle.LifecycleEvent.TearDown);
    serviceRegistry.tearDown();
    nodeNameLookup.tearDown();
    clock.tearDown();
    handlerTrade.tearDown();
    subscriptionManager.tearDown();
    afterEvent();
  }

  @Override
  public void setContextParameterMap(Map<Object, Object> newContextMapping) {
    context.replaceMappings(newContextMapping);
  }

  @Override
  public void addContextParameter(Object key, Object value) {
    context.addMapping(key, value);
  }

  // EVENT DISPATCH - START
  @Override
  @OnEventHandler(failBuildIfMissingBooleanReturn = false)
  public void onEvent(Object event) {
    if (buffering) {
      triggerCalculation();
    }
    if (processing) {
      callbackDispatcher.queueReentrantEvent(event);
    } else {
      processing = true;
      onEventInternal(event);
      callbackDispatcher.dispatchQueuedCallbacks();
      processing = false;
    }
  }

  @Override
  public void onEventInternal(Object event) {
    if (event instanceof com.fluxtion.example.cookbook.pnl.events.MidPrice) {
      MidPrice typedEvent = (MidPrice) event;
      handleEvent(typedEvent);
    } else if (event instanceof com.fluxtion.example.cookbook.pnl.events.MtmInstrument) {
      MtmInstrument typedEvent = (MtmInstrument) event;
      handleEvent(typedEvent);
    } else if (event instanceof com.fluxtion.example.cookbook.pnl.events.Trade) {
      Trade typedEvent = (Trade) event;
      handleEvent(typedEvent);
    } else if (event instanceof com.fluxtion.runtime.event.Signal) {
      Signal typedEvent = (Signal) event;
      handleEvent(typedEvent);
    } else if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      handleEvent(typedEvent);
    } else {
      unKnownEventHandler(event);
    }
  }

  public void handleEvent(MidPrice typedEvent) {
    auditEvent(typedEvent);
    // Default, no filter methods
    isDirty_mtMRateCalculator_45 = mtMRateCalculator_45.midRate(typedEvent);
    if (isDirty_mtMRateCalculator_45) {
      mapRef2RefFlowFunction_7.publishTriggerNodeUpdated(mtMRateCalculator_45);
    }
    if (guardCheck_pnlSummaryCalc_10()) {
      isDirty_pnlSummaryCalc_10 = pnlSummaryCalc_10.trigger();
    }
    if (guardCheck_mapRef2RefFlowFunction_7()) {
      isDirty_mapRef2RefFlowFunction_7 = mapRef2RefFlowFunction_7.map();
      if (isDirty_mapRef2RefFlowFunction_7) {
        mapRef2RefFlowFunction_9.inputUpdated(mapRef2RefFlowFunction_7);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_9()) {
      isDirty_mapRef2RefFlowFunction_9 = mapRef2RefFlowFunction_9.map();
      if (isDirty_mapRef2RefFlowFunction_9) {
        mapRef2RefFlowFunction_11.inputUpdated(mapRef2RefFlowFunction_9);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_11()) {
      isDirty_mapRef2RefFlowFunction_11 = mapRef2RefFlowFunction_11.map();
      if (isDirty_mapRef2RefFlowFunction_11) {
        peekFlowFunction_13.inputUpdated(mapRef2RefFlowFunction_11);
      }
    }
    if (guardCheck_peekFlowFunction_13()) {
      peekFlowFunction_13.peek();
    }
    afterEvent();
  }

  public void handleEvent(MtmInstrument typedEvent) {
    auditEvent(typedEvent);
    // Default, no filter methods
    isDirty_mtMRateCalculator_45 = mtMRateCalculator_45.updateMtmInstrument(typedEvent);
    if (isDirty_mtMRateCalculator_45) {
      mapRef2RefFlowFunction_7.publishTriggerNodeUpdated(mtMRateCalculator_45);
    }
    if (guardCheck_pnlSummaryCalc_10()) {
      isDirty_pnlSummaryCalc_10 = pnlSummaryCalc_10.trigger();
    }
    if (guardCheck_mapRef2RefFlowFunction_7()) {
      isDirty_mapRef2RefFlowFunction_7 = mapRef2RefFlowFunction_7.map();
      if (isDirty_mapRef2RefFlowFunction_7) {
        mapRef2RefFlowFunction_9.inputUpdated(mapRef2RefFlowFunction_7);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_9()) {
      isDirty_mapRef2RefFlowFunction_9 = mapRef2RefFlowFunction_9.map();
      if (isDirty_mapRef2RefFlowFunction_9) {
        mapRef2RefFlowFunction_11.inputUpdated(mapRef2RefFlowFunction_9);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_11()) {
      isDirty_mapRef2RefFlowFunction_11 = mapRef2RefFlowFunction_11.map();
      if (isDirty_mapRef2RefFlowFunction_11) {
        peekFlowFunction_13.inputUpdated(mapRef2RefFlowFunction_11);
      }
    }
    if (guardCheck_peekFlowFunction_13()) {
      peekFlowFunction_13.peek();
    }
    afterEvent();
  }

  public void handleEvent(Trade typedEvent) {
    auditEvent(typedEvent);
    // Default, no filter methods
    isDirty_handlerTrade = handlerTrade.onEvent(typedEvent);
    if (isDirty_handlerTrade) {
      mapRef2RefFlowFunction_1.inputUpdated(handlerTrade);
      mapRef2RefFlowFunction_3.inputUpdated(handlerTrade);
    }
    if (guardCheck_mapRef2RefFlowFunction_1()) {
      isDirty_mapRef2RefFlowFunction_1 = mapRef2RefFlowFunction_1.map();
      if (isDirty_mapRef2RefFlowFunction_1) {
        binaryMapToRefFlowFunction_5.inputUpdated(mapRef2RefFlowFunction_1);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_3()) {
      isDirty_mapRef2RefFlowFunction_3 = mapRef2RefFlowFunction_3.map();
      if (isDirty_mapRef2RefFlowFunction_3) {
        binaryMapToRefFlowFunction_5.input2Updated(mapRef2RefFlowFunction_3);
      }
    }
    if (guardCheck_binaryMapToRefFlowFunction_5()) {
      isDirty_binaryMapToRefFlowFunction_5 = binaryMapToRefFlowFunction_5.map();
      if (isDirty_binaryMapToRefFlowFunction_5) {
        mapRef2RefFlowFunction_7.inputUpdated(binaryMapToRefFlowFunction_5);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_7()) {
      isDirty_mapRef2RefFlowFunction_7 = mapRef2RefFlowFunction_7.map();
      if (isDirty_mapRef2RefFlowFunction_7) {
        mapRef2RefFlowFunction_9.inputUpdated(mapRef2RefFlowFunction_7);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_9()) {
      isDirty_mapRef2RefFlowFunction_9 = mapRef2RefFlowFunction_9.map();
      if (isDirty_mapRef2RefFlowFunction_9) {
        mapRef2RefFlowFunction_11.inputUpdated(mapRef2RefFlowFunction_9);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_11()) {
      isDirty_mapRef2RefFlowFunction_11 = mapRef2RefFlowFunction_11.map();
      if (isDirty_mapRef2RefFlowFunction_11) {
        peekFlowFunction_13.inputUpdated(mapRef2RefFlowFunction_11);
      }
    }
    if (guardCheck_peekFlowFunction_13()) {
      peekFlowFunction_13.peek();
    }
    afterEvent();
  }

  public void handleEvent(Signal typedEvent) {
    auditEvent(typedEvent);
    switch (typedEvent.filterString()) {
        // Event Class:[com.fluxtion.runtime.event.Signal] filterString:[eob]
      case ("eob"):
        handle_Signal_eob(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  public void handleEvent(ClockStrategyEvent typedEvent) {
    auditEvent(typedEvent);
    // Default, no filter methods
    isDirty_clock = true;
    clock.setClockStrategy(typedEvent);
    afterEvent();
  }
  // EVENT DISPATCH - END

  // FILTERED DISPATCH - START
  private void handle_Signal_eob(Signal typedEvent) {
    isDirty_pnlSummaryCalc_10 = pnlSummaryCalc_10.eobTrigger(typedEvent);
    if (guardCheck_mapRef2RefFlowFunction_11()) {
      isDirty_mapRef2RefFlowFunction_11 = mapRef2RefFlowFunction_11.map();
      if (isDirty_mapRef2RefFlowFunction_11) {
        peekFlowFunction_13.inputUpdated(mapRef2RefFlowFunction_11);
      }
    }
    if (guardCheck_peekFlowFunction_13()) {
      peekFlowFunction_13.peek();
    }
  }
  // FILTERED DISPATCH - END

  // EXPORTED SERVICE FUNCTIONS - START
  @Override
  public void deRegisterService(com.fluxtion.runtime.service.Service<?> arg0) {
    beforeServiceCall(
        "public void com.fluxtion.runtime.service.ServiceRegistryNode.deRegisterService(com.fluxtion.runtime.service.Service<?>)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    serviceRegistry.deRegisterService(arg0);
    afterServiceCall();
  }

  @Override
  public void registerService(com.fluxtion.runtime.service.Service<?> arg0) {
    beforeServiceCall(
        "public void com.fluxtion.runtime.service.ServiceRegistryNode.registerService(com.fluxtion.runtime.service.Service<?>)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    serviceRegistry.registerService(arg0);
    afterServiceCall();
  }
  // EXPORTED SERVICE FUNCTIONS - END

  // EVENT BUFFERING - START
  public void bufferEvent(Object event) {
    throw new UnsupportedOperationException("bufferEvent not supported");
  }

  public void triggerCalculation() {
    throw new UnsupportedOperationException("triggerCalculation not supported");
  }
  // EVENT BUFFERING - END

  private void auditEvent(Object typedEvent) {
    clock.eventReceived(typedEvent);
    nodeNameLookup.eventReceived(typedEvent);
    serviceRegistry.eventReceived(typedEvent);
  }

  private void auditEvent(Event typedEvent) {
    clock.eventReceived(typedEvent);
    nodeNameLookup.eventReceived(typedEvent);
    serviceRegistry.eventReceived(typedEvent);
  }

  private void initialiseAuditor(Auditor auditor) {
    auditor.init();
    auditor.nodeRegistered(mtMRateCalculator_45, "mtMRateCalculator_45");
    auditor.nodeRegistered(pnlSummaryCalc_10, "pnlSummaryCalc_10");
    auditor.nodeRegistered(callbackDispatcher, "callbackDispatcher");
    auditor.nodeRegistered(binaryMapToRefFlowFunction_5, "binaryMapToRefFlowFunction_5");
    auditor.nodeRegistered(mapRef2RefFlowFunction_1, "mapRef2RefFlowFunction_1");
    auditor.nodeRegistered(mapRef2RefFlowFunction_3, "mapRef2RefFlowFunction_3");
    auditor.nodeRegistered(mapRef2RefFlowFunction_7, "mapRef2RefFlowFunction_7");
    auditor.nodeRegistered(mapRef2RefFlowFunction_9, "mapRef2RefFlowFunction_9");
    auditor.nodeRegistered(mapRef2RefFlowFunction_11, "mapRef2RefFlowFunction_11");
    auditor.nodeRegistered(peekFlowFunction_13, "peekFlowFunction_13");
    auditor.nodeRegistered(groupByFlowFunctionWrapper_0, "groupByFlowFunctionWrapper_0");
    auditor.nodeRegistered(groupByFlowFunctionWrapper_2, "groupByFlowFunctionWrapper_2");
    auditor.nodeRegistered(groupByMapFlowFunction_6, "groupByMapFlowFunction_6");
    auditor.nodeRegistered(groupByMapFlowFunction_8, "groupByMapFlowFunction_8");
    auditor.nodeRegistered(outerJoin_4, "outerJoin_4");
    auditor.nodeRegistered(templateMessage_12, "templateMessage_12");
    auditor.nodeRegistered(mapTuple_47, "mapTuple_47");
    auditor.nodeRegistered(subscriptionManager, "subscriptionManager");
    auditor.nodeRegistered(handlerTrade, "handlerTrade");
    auditor.nodeRegistered(context, "context");
  }

  private void beforeServiceCall(String functionDescription) {
    functionAudit.setFunctionDescription(functionDescription);
    auditEvent(functionAudit);
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
  }

  private void afterServiceCall() {
    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
  }

  private void afterEvent() {

    clock.processingComplete();
    nodeNameLookup.processingComplete();
    serviceRegistry.processingComplete();
    isDirty_binaryMapToRefFlowFunction_5 = false;
    isDirty_clock = false;
    isDirty_handlerTrade = false;
    isDirty_mapRef2RefFlowFunction_1 = false;
    isDirty_mapRef2RefFlowFunction_3 = false;
    isDirty_mapRef2RefFlowFunction_7 = false;
    isDirty_mapRef2RefFlowFunction_9 = false;
    isDirty_mapRef2RefFlowFunction_11 = false;
    isDirty_mtMRateCalculator_45 = false;
    isDirty_pnlSummaryCalc_10 = false;
  }

  @Override
  public void batchPause() {
    auditEvent(Lifecycle.LifecycleEvent.BatchPause);
    processing = true;

    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
  }

  @Override
  public void batchEnd() {
    auditEvent(Lifecycle.LifecycleEvent.BatchEnd);
    processing = true;

    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
  }

  @Override
  public boolean isDirty(Object node) {
    return dirtySupplier(node).getAsBoolean();
  }

  @Override
  public BooleanSupplier dirtySupplier(Object node) {
    if (dirtyFlagSupplierMap.isEmpty()) {
      dirtyFlagSupplierMap.put(
          binaryMapToRefFlowFunction_5, () -> isDirty_binaryMapToRefFlowFunction_5);
      dirtyFlagSupplierMap.put(clock, () -> isDirty_clock);
      dirtyFlagSupplierMap.put(handlerTrade, () -> isDirty_handlerTrade);
      dirtyFlagSupplierMap.put(mapRef2RefFlowFunction_1, () -> isDirty_mapRef2RefFlowFunction_1);
      dirtyFlagSupplierMap.put(mapRef2RefFlowFunction_11, () -> isDirty_mapRef2RefFlowFunction_11);
      dirtyFlagSupplierMap.put(mapRef2RefFlowFunction_3, () -> isDirty_mapRef2RefFlowFunction_3);
      dirtyFlagSupplierMap.put(mapRef2RefFlowFunction_7, () -> isDirty_mapRef2RefFlowFunction_7);
      dirtyFlagSupplierMap.put(mapRef2RefFlowFunction_9, () -> isDirty_mapRef2RefFlowFunction_9);
      dirtyFlagSupplierMap.put(mtMRateCalculator_45, () -> isDirty_mtMRateCalculator_45);
      dirtyFlagSupplierMap.put(pnlSummaryCalc_10, () -> isDirty_pnlSummaryCalc_10);
    }
    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(
          binaryMapToRefFlowFunction_5, (b) -> isDirty_binaryMapToRefFlowFunction_5 = b);
      dirtyFlagUpdateMap.put(clock, (b) -> isDirty_clock = b);
      dirtyFlagUpdateMap.put(handlerTrade, (b) -> isDirty_handlerTrade = b);
      dirtyFlagUpdateMap.put(mapRef2RefFlowFunction_1, (b) -> isDirty_mapRef2RefFlowFunction_1 = b);
      dirtyFlagUpdateMap.put(
          mapRef2RefFlowFunction_11, (b) -> isDirty_mapRef2RefFlowFunction_11 = b);
      dirtyFlagUpdateMap.put(mapRef2RefFlowFunction_3, (b) -> isDirty_mapRef2RefFlowFunction_3 = b);
      dirtyFlagUpdateMap.put(mapRef2RefFlowFunction_7, (b) -> isDirty_mapRef2RefFlowFunction_7 = b);
      dirtyFlagUpdateMap.put(mapRef2RefFlowFunction_9, (b) -> isDirty_mapRef2RefFlowFunction_9 = b);
      dirtyFlagUpdateMap.put(mtMRateCalculator_45, (b) -> isDirty_mtMRateCalculator_45 = b);
      dirtyFlagUpdateMap.put(pnlSummaryCalc_10, (b) -> isDirty_pnlSummaryCalc_10 = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_pnlSummaryCalc_10() {
    return isDirty_mtMRateCalculator_45;
  }

  private boolean guardCheck_binaryMapToRefFlowFunction_5() {
    return isDirty_mapRef2RefFlowFunction_1 | isDirty_mapRef2RefFlowFunction_3;
  }

  private boolean guardCheck_mapRef2RefFlowFunction_1() {
    return isDirty_handlerTrade;
  }

  private boolean guardCheck_mapRef2RefFlowFunction_3() {
    return isDirty_handlerTrade;
  }

  private boolean guardCheck_mapRef2RefFlowFunction_7() {
    return isDirty_binaryMapToRefFlowFunction_5 | isDirty_mtMRateCalculator_45;
  }

  private boolean guardCheck_mapRef2RefFlowFunction_9() {
    return isDirty_mapRef2RefFlowFunction_7;
  }

  private boolean guardCheck_mapRef2RefFlowFunction_11() {
    return isDirty_mapRef2RefFlowFunction_9 | isDirty_pnlSummaryCalc_10;
  }

  private boolean guardCheck_peekFlowFunction_13() {
    return isDirty_mapRef2RefFlowFunction_11;
  }

  private boolean guardCheck_groupByMapFlowFunction_8() {
    return isDirty_mtMRateCalculator_45;
  }

  private boolean guardCheck_context() {
    return isDirty_clock;
  }

  @Override
  public <T> T getNodeById(String id) throws NoSuchFieldException {
    return nodeNameLookup.getInstanceById(id);
  }

  @Override
  public <A extends Auditor> A getAuditorById(String id)
      throws NoSuchFieldException, IllegalAccessException {
    return (A) this.getClass().getField(id).get(this);
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
  public PnlFromJoinCalculator newInstance() {
    return new PnlFromJoinCalculator();
  }

  @Override
  public PnlFromJoinCalculator newInstance(Map<Object, Object> contextMap) {
    return new PnlFromJoinCalculator();
  }

  @Override
  public String getLastAuditLogRecord() {
    try {
      EventLogManager eventLogManager =
          (EventLogManager) this.getClass().getField(EventLogManager.NODE_NAME).get(this);
      return eventLogManager.lastRecordAsString();
    } catch (Throwable e) {
      return "";
    }
  }

  public void unKnownEventHandler(Object object) {
    unKnownEventHandler.accept(object);
  }

  @Override
  public <T> void setUnKnownEventHandler(Consumer<T> consumer) {
    unKnownEventHandler = consumer;
  }

  @Override
  public SubscriptionManager getSubscriptionManager() {
    return subscriptionManager;
  }
}
