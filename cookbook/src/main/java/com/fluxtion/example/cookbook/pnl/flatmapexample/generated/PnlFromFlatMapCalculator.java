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
package com.fluxtion.example.cookbook.pnl.flatmapexample.generated;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.example.cookbook.pnl.calculator.MtMRateCalculator;
import com.fluxtion.example.cookbook.pnl.calculator.PnlSummaryCalc;
import com.fluxtion.example.cookbook.pnl.calculator.TradeLegToPositionAggregate;
import com.fluxtion.example.cookbook.pnl.events.MidPrice;
import com.fluxtion.example.cookbook.pnl.events.MtmInstrument;
import com.fluxtion.example.cookbook.pnl.events.Trade;
import com.fluxtion.example.cookbook.pnl.events.TradeLeg;
import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.audit.EventLogManager;
import com.fluxtion.runtime.audit.NodeNameAuditor;
import com.fluxtion.runtime.callback.CallBackNode;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.callback.ExportFunctionAuditEvent;
import com.fluxtion.runtime.callback.InstanceCallbackEvent.InstanceCallbackEvent_0;
import com.fluxtion.runtime.dataflow.function.FlatMapArrayFlowFunction;
import com.fluxtion.runtime.dataflow.function.MapFlowFunction.MapRef2RefFlowFunction;
import com.fluxtion.runtime.dataflow.function.PeekFlowFunction;
import com.fluxtion.runtime.dataflow.groupby.GroupBy.EmptyGroupBy;
import com.fluxtion.runtime.dataflow.groupby.GroupByFlowFunctionWrapper;
import com.fluxtion.runtime.dataflow.helpers.Mappers;
import com.fluxtion.runtime.dataflow.helpers.Peekers.TemplateMessage;
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
 * eventProcessorGenerator version : 9.7.2
 * api version                     : 9.7.2
 * </pre>
 *
 * Event classes supported:
 *
 * <ul>
 *   <li>com.fluxtion.compiler.generation.model.ExportFunctionMarker
 *   <li>com.fluxtion.example.cookbook.pnl.events.MidPrice
 *   <li>com.fluxtion.example.cookbook.pnl.events.MtmInstrument
 *   <li>com.fluxtion.example.cookbook.pnl.events.Trade
 *   <li>com.fluxtion.runtime.callback.InstanceCallbackEvent.InstanceCallbackEvent_0
 *   <li>com.fluxtion.runtime.event.Signal
 *   <li>com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class PnlFromFlatMapCalculator
    implements EventProcessor<PnlFromFlatMapCalculator>,
        /*--- @ExportService start ---*/
        @ExportService ServiceListener,
        /*--- @ExportService end ---*/
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle {

  // Node declarations
  private final transient InstanceCallbackEvent_0 callBackTriggerEvent_0 =
      new InstanceCallbackEvent_0();
  private final transient CallBackNode callBackNode_7 = new CallBackNode<>(callBackTriggerEvent_0);
  private final transient CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  public final transient Clock clock = new Clock();
  private final transient GroupByFlowFunctionWrapper groupByFlowFunctionWrapper_1 =
      new GroupByFlowFunctionWrapper<>(
          TradeLeg::instrument, Mappers::identity, TradeLegToPositionAggregate::new);
  private final transient MtMRateCalculator mtMRateCalculator_9 = new MtMRateCalculator();
  public final transient NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final transient PnlSummaryCalc pnlSummaryCalc_3 = new PnlSummaryCalc(mtMRateCalculator_9);
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
  private final transient FlatMapArrayFlowFunction flatMapArrayFlowFunction_0 =
      new FlatMapArrayFlowFunction<>(handlerTrade, Trade::tradeLegs);
  private final transient MapRef2RefFlowFunction mapRef2RefFlowFunction_2 =
      new MapRef2RefFlowFunction<>(
          flatMapArrayFlowFunction_0, groupByFlowFunctionWrapper_1::aggregate);
  private final transient MapRef2RefFlowFunction mapRef2RefFlowFunction_4 =
      new MapRef2RefFlowFunction<>(
          mapRef2RefFlowFunction_2, pnlSummaryCalc_3::calcMtmAndUpdateSummary);
  public final transient ServiceRegistryNode serviceRegistry = new ServiceRegistryNode();
  private final transient TemplateMessage templateMessage_5 = new TemplateMessage<>("{}");
  private final transient PeekFlowFunction peekFlowFunction_6 =
      new PeekFlowFunction<>(mapRef2RefFlowFunction_4, templateMessage_5::templateAndLogToConsole);
  private final transient ExportFunctionAuditEvent functionAudit = new ExportFunctionAuditEvent();
  // Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final transient IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(8);
  private final transient IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(8);

  private boolean isDirty_callBackNode_7 = false;
  private boolean isDirty_clock = false;
  private boolean isDirty_flatMapArrayFlowFunction_0 = false;
  private boolean isDirty_handlerTrade = false;
  private boolean isDirty_mapRef2RefFlowFunction_2 = false;
  private boolean isDirty_mapRef2RefFlowFunction_4 = false;
  private boolean isDirty_mtMRateCalculator_9 = false;
  private boolean isDirty_pnlSummaryCalc_3 = false;

  // Forked declarations

  // Filter constants

  // unknown event handler
  private Consumer unKnownEventHandler = (e) -> {};

  public PnlFromFlatMapCalculator(Map<Object, Object> contextMap) {
    if (context != null) {
      context.replaceMappings(contextMap);
    }
    flatMapArrayFlowFunction_0.setFlatMapCompleteSignal("eob");
    flatMapArrayFlowFunction_0.dirtyStateMonitor = callbackDispatcher;
    flatMapArrayFlowFunction_0.callback = callBackNode_7;
    mapRef2RefFlowFunction_2.setDefaultValue(new EmptyGroupBy());
    mapRef2RefFlowFunction_2.setEventProcessorContext(context);
    mapRef2RefFlowFunction_2.setPublishTriggerOverrideNode(pnlSummaryCalc_3);
    mapRef2RefFlowFunction_4.setEventProcessorContext(context);
    peekFlowFunction_6.setEventProcessorContext(context);
    templateMessage_5.clock = clock;
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

  public PnlFromFlatMapCalculator() {
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
    flatMapArrayFlowFunction_0.init();
    mapRef2RefFlowFunction_2.initialiseEventStream();
    mapRef2RefFlowFunction_4.initialiseEventStream();
    templateMessage_5.initialise();
    peekFlowFunction_6.initialiseEventStream();
    afterEvent();
  }

  @Override
  public void start() {
    if (!initCalled) {
      throw new RuntimeException("init() must be called before start()");
    }
    processing = true;
    auditEvent(Lifecycle.LifecycleEvent.Start);
    templateMessage_5.start();
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
    } else if (event
        instanceof com.fluxtion.runtime.callback.InstanceCallbackEvent.InstanceCallbackEvent_0) {
      InstanceCallbackEvent_0 typedEvent = (InstanceCallbackEvent_0) event;
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
    isDirty_mtMRateCalculator_9 = mtMRateCalculator_9.midRate(typedEvent);
    if (guardCheck_pnlSummaryCalc_3()) {
      isDirty_pnlSummaryCalc_3 = pnlSummaryCalc_3.trigger();
      if (isDirty_pnlSummaryCalc_3) {
        mapRef2RefFlowFunction_2.publishTriggerOverrideNodeUpdated(pnlSummaryCalc_3);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_2()) {
      isDirty_mapRef2RefFlowFunction_2 = mapRef2RefFlowFunction_2.map();
      if (isDirty_mapRef2RefFlowFunction_2) {
        mapRef2RefFlowFunction_4.inputUpdated(mapRef2RefFlowFunction_2);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_4()) {
      isDirty_mapRef2RefFlowFunction_4 = mapRef2RefFlowFunction_4.map();
      if (isDirty_mapRef2RefFlowFunction_4) {
        peekFlowFunction_6.inputUpdated(mapRef2RefFlowFunction_4);
      }
    }
    if (guardCheck_peekFlowFunction_6()) {
      peekFlowFunction_6.peek();
    }
    afterEvent();
  }

  public void handleEvent(MtmInstrument typedEvent) {
    auditEvent(typedEvent);
    // Default, no filter methods
    isDirty_mtMRateCalculator_9 = mtMRateCalculator_9.updateMtmInstrument(typedEvent);
    if (guardCheck_pnlSummaryCalc_3()) {
      isDirty_pnlSummaryCalc_3 = pnlSummaryCalc_3.trigger();
      if (isDirty_pnlSummaryCalc_3) {
        mapRef2RefFlowFunction_2.publishTriggerOverrideNodeUpdated(pnlSummaryCalc_3);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_2()) {
      isDirty_mapRef2RefFlowFunction_2 = mapRef2RefFlowFunction_2.map();
      if (isDirty_mapRef2RefFlowFunction_2) {
        mapRef2RefFlowFunction_4.inputUpdated(mapRef2RefFlowFunction_2);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_4()) {
      isDirty_mapRef2RefFlowFunction_4 = mapRef2RefFlowFunction_4.map();
      if (isDirty_mapRef2RefFlowFunction_4) {
        peekFlowFunction_6.inputUpdated(mapRef2RefFlowFunction_4);
      }
    }
    if (guardCheck_peekFlowFunction_6()) {
      peekFlowFunction_6.peek();
    }
    afterEvent();
  }

  public void handleEvent(Trade typedEvent) {
    auditEvent(typedEvent);
    // Default, no filter methods
    isDirty_handlerTrade = handlerTrade.onEvent(typedEvent);
    if (isDirty_handlerTrade) {
      flatMapArrayFlowFunction_0.inputUpdatedAndFlatMap(handlerTrade);
    }
    afterEvent();
  }

  public void handleEvent(InstanceCallbackEvent_0 typedEvent) {
    auditEvent(typedEvent);
    // Default, no filter methods
    isDirty_callBackNode_7 = callBackNode_7.onEvent(typedEvent);
    if (guardCheck_flatMapArrayFlowFunction_0()) {
      isDirty_flatMapArrayFlowFunction_0 = true;
      flatMapArrayFlowFunction_0.callbackReceived();
      if (isDirty_flatMapArrayFlowFunction_0) {
        mapRef2RefFlowFunction_2.inputUpdated(flatMapArrayFlowFunction_0);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_2()) {
      isDirty_mapRef2RefFlowFunction_2 = mapRef2RefFlowFunction_2.map();
      if (isDirty_mapRef2RefFlowFunction_2) {
        mapRef2RefFlowFunction_4.inputUpdated(mapRef2RefFlowFunction_2);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_4()) {
      isDirty_mapRef2RefFlowFunction_4 = mapRef2RefFlowFunction_4.map();
      if (isDirty_mapRef2RefFlowFunction_4) {
        peekFlowFunction_6.inputUpdated(mapRef2RefFlowFunction_4);
      }
    }
    if (guardCheck_peekFlowFunction_6()) {
      peekFlowFunction_6.peek();
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
    isDirty_pnlSummaryCalc_3 = pnlSummaryCalc_3.eobTrigger(typedEvent);
    if (isDirty_pnlSummaryCalc_3) {
      mapRef2RefFlowFunction_2.publishTriggerOverrideNodeUpdated(pnlSummaryCalc_3);
    }
    if (guardCheck_mapRef2RefFlowFunction_2()) {
      isDirty_mapRef2RefFlowFunction_2 = mapRef2RefFlowFunction_2.map();
      if (isDirty_mapRef2RefFlowFunction_2) {
        mapRef2RefFlowFunction_4.inputUpdated(mapRef2RefFlowFunction_2);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_4()) {
      isDirty_mapRef2RefFlowFunction_4 = mapRef2RefFlowFunction_4.map();
      if (isDirty_mapRef2RefFlowFunction_4) {
        peekFlowFunction_6.inputUpdated(mapRef2RefFlowFunction_4);
      }
    }
    if (guardCheck_peekFlowFunction_6()) {
      peekFlowFunction_6.peek();
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
    auditor.nodeRegistered(mtMRateCalculator_9, "mtMRateCalculator_9");
    auditor.nodeRegistered(pnlSummaryCalc_3, "pnlSummaryCalc_3");
    auditor.nodeRegistered(callBackNode_7, "callBackNode_7");
    auditor.nodeRegistered(callbackDispatcher, "callbackDispatcher");
    auditor.nodeRegistered(callBackTriggerEvent_0, "callBackTriggerEvent_0");
    auditor.nodeRegistered(flatMapArrayFlowFunction_0, "flatMapArrayFlowFunction_0");
    auditor.nodeRegistered(mapRef2RefFlowFunction_2, "mapRef2RefFlowFunction_2");
    auditor.nodeRegistered(mapRef2RefFlowFunction_4, "mapRef2RefFlowFunction_4");
    auditor.nodeRegistered(peekFlowFunction_6, "peekFlowFunction_6");
    auditor.nodeRegistered(groupByFlowFunctionWrapper_1, "groupByFlowFunctionWrapper_1");
    auditor.nodeRegistered(templateMessage_5, "templateMessage_5");
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
    isDirty_callBackNode_7 = false;
    isDirty_clock = false;
    isDirty_flatMapArrayFlowFunction_0 = false;
    isDirty_handlerTrade = false;
    isDirty_mapRef2RefFlowFunction_2 = false;
    isDirty_mapRef2RefFlowFunction_4 = false;
    isDirty_mtMRateCalculator_9 = false;
    isDirty_pnlSummaryCalc_3 = false;
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
      dirtyFlagSupplierMap.put(callBackNode_7, () -> isDirty_callBackNode_7);
      dirtyFlagSupplierMap.put(clock, () -> isDirty_clock);
      dirtyFlagSupplierMap.put(
          flatMapArrayFlowFunction_0, () -> isDirty_flatMapArrayFlowFunction_0);
      dirtyFlagSupplierMap.put(handlerTrade, () -> isDirty_handlerTrade);
      dirtyFlagSupplierMap.put(mapRef2RefFlowFunction_2, () -> isDirty_mapRef2RefFlowFunction_2);
      dirtyFlagSupplierMap.put(mapRef2RefFlowFunction_4, () -> isDirty_mapRef2RefFlowFunction_4);
      dirtyFlagSupplierMap.put(mtMRateCalculator_9, () -> isDirty_mtMRateCalculator_9);
      dirtyFlagSupplierMap.put(pnlSummaryCalc_3, () -> isDirty_pnlSummaryCalc_3);
    }
    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(callBackNode_7, (b) -> isDirty_callBackNode_7 = b);
      dirtyFlagUpdateMap.put(clock, (b) -> isDirty_clock = b);
      dirtyFlagUpdateMap.put(
          flatMapArrayFlowFunction_0, (b) -> isDirty_flatMapArrayFlowFunction_0 = b);
      dirtyFlagUpdateMap.put(handlerTrade, (b) -> isDirty_handlerTrade = b);
      dirtyFlagUpdateMap.put(mapRef2RefFlowFunction_2, (b) -> isDirty_mapRef2RefFlowFunction_2 = b);
      dirtyFlagUpdateMap.put(mapRef2RefFlowFunction_4, (b) -> isDirty_mapRef2RefFlowFunction_4 = b);
      dirtyFlagUpdateMap.put(mtMRateCalculator_9, (b) -> isDirty_mtMRateCalculator_9 = b);
      dirtyFlagUpdateMap.put(pnlSummaryCalc_3, (b) -> isDirty_pnlSummaryCalc_3 = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_pnlSummaryCalc_3() {
    return isDirty_mtMRateCalculator_9;
  }

  private boolean guardCheck_flatMapArrayFlowFunction_0() {
    return isDirty_callBackNode_7;
  }

  private boolean guardCheck_mapRef2RefFlowFunction_2() {
    return isDirty_flatMapArrayFlowFunction_0 | isDirty_pnlSummaryCalc_3;
  }

  private boolean guardCheck_mapRef2RefFlowFunction_4() {
    return isDirty_mapRef2RefFlowFunction_2 | isDirty_pnlSummaryCalc_3;
  }

  private boolean guardCheck_peekFlowFunction_6() {
    return isDirty_mapRef2RefFlowFunction_4;
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
  public PnlFromFlatMapCalculator newInstance() {
    return new PnlFromFlatMapCalculator();
  }

  @Override
  public PnlFromFlatMapCalculator newInstance(Map<Object, Object> contextMap) {
    return new PnlFromFlatMapCalculator();
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
