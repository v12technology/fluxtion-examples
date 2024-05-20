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
package com.fluxtion.example.jmh.pricer.generated;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.example.jmh.pricer.PriceCalculator;
import com.fluxtion.example.jmh.pricer.PriceLadderConsumer;
import com.fluxtion.example.jmh.pricer.node.LevelsCalculator;
import com.fluxtion.example.jmh.pricer.node.MidCalculator;
import com.fluxtion.example.jmh.pricer.node.PriceLadderPublisher;
import com.fluxtion.example.jmh.pricer.node.SkewCalculator;
import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.audit.EventLogManager;
import com.fluxtion.runtime.audit.NodeNameAuditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.callback.ExportFunctionAuditEvent;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.input.EventFeed;
import com.fluxtion.runtime.input.SubscriptionManagerNode;
import com.fluxtion.runtime.node.ForkedTriggerTask;
import com.fluxtion.runtime.node.MutableEventProcessorContext;
import com.fluxtion.runtime.time.Clock;
import com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent;
import java.util.Map;

import java.util.IdentityHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 *
 *
 * <pre>
 * generation time                 : Not available
 * eventProcessorGenerator version : 9.3.10
 * api version                     : 9.3.10
 * </pre>
 *
 * Event classes supported:
 *
 * <ul>
 *   <li>com.fluxtion.compiler.generation.model.ExportFunctionMarker
 *   <li>com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class PriceLadderProcessor
    implements EventProcessor<PriceLadderProcessor>,
        /*--- @ExportService start ---*/
        PriceCalculator,
        PriceLadderConsumer,
        /*--- @ExportService end ---*/
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle {

  //Node declarations
  private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  private final MidCalculator midCalculator_3 = new MidCalculator();
  public final NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final SkewCalculator skewCalculator_2 = new SkewCalculator(midCalculator_3);
  private final LevelsCalculator levelsCalculator_1 = new LevelsCalculator(skewCalculator_2);
  private final PriceLadderPublisher priceLadderPublisher_0 =
      new PriceLadderPublisher(levelsCalculator_1);
  private final SubscriptionManagerNode subscriptionManager = new SubscriptionManagerNode();
  private final MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  public final Clock clock = new Clock();
  private final ExportFunctionAuditEvent functionAudit = new ExportFunctionAuditEvent();
  //Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(3);
  private final IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(3);

  private boolean isDirty_levelsCalculator_1 = false;
  private boolean isDirty_midCalculator_3 = false;
  private boolean isDirty_skewCalculator_2 = false;

  //Forked declarations

  //Filter constants

  //unknown event handler
  private Consumer unKnownEventHandler = (e) -> {};

  public PriceLadderProcessor(Map<Object, Object> contextMap) {
    if (context != null) {
      context.replaceMappings(contextMap);
    }
    //node auditors
    initialiseAuditor(clock);
    initialiseAuditor(nodeNameLookup);
    if (subscriptionManager != null) {
      subscriptionManager.setSubscribingEventProcessor(this);
    }
    if (context != null) {
      context.setEventProcessorCallback(this);
    }
  }

  public PriceLadderProcessor() {
    this(null);
  }

  @Override
  public void init() {
    initCalled = true;
    auditEvent(Lifecycle.LifecycleEvent.Init);
    //initialise dirty lookup map
    isDirty("test");
    clock.init();
    afterEvent();
  }

  @Override
  public void start() {
    if (!initCalled) {
      throw new RuntimeException("init() must be called before start()");
    }
    processing = true;
    auditEvent(Lifecycle.LifecycleEvent.Start);

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
    nodeNameLookup.tearDown();
    clock.tearDown();
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

  //EVENT DISPATCH - START
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

  @Override
  public void onEventInternal(Object event) {
    if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      handleEvent(typedEvent);
    } else {
      unKnownEventHandler(event);
    }
  }

  public void handleEvent(ClockStrategyEvent typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    clock.setClockStrategy(typedEvent);
    afterEvent();
  }
  //EVENT DISPATCH - END

  //EXPORTED SERVICE FUNCTIONS - START
  @Override
  public boolean newPriceLadder(com.fluxtion.example.jmh.pricer.PriceLadder arg0) {
    beforeServiceCall(
        "public boolean com.fluxtion.example.jmh.pricer.node.MidCalculator.newPriceLadder(com.fluxtion.example.jmh.pricer.PriceLadder)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    isDirty_midCalculator_3 = midCalculator_3.newPriceLadder(arg0);
    if (guardCheck_skewCalculator_2()) {
      isDirty_skewCalculator_2 = skewCalculator_2.calculateSkewedLadder();
    }
    if (guardCheck_levelsCalculator_1()) {
      isDirty_levelsCalculator_1 = levelsCalculator_1.calculateLevelsForLadder();
    }
    if (guardCheck_priceLadderPublisher_0()) {
      priceLadderPublisher_0.publishPriceLadder();
    }
    afterServiceCall();
    return true;
  }

  @Override
  public void setLevels(int arg0) {
    beforeServiceCall(
        "public default void com.fluxtion.example.jmh.pricer.PriceCalculator.setLevels(int)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    isDirty_skewCalculator_2 = true;
    skewCalculator_2.setLevels(arg0);
    isDirty_levelsCalculator_1 = true;
    levelsCalculator_1.setLevels(arg0);
    priceLadderPublisher_0.setLevels(arg0);
    afterServiceCall();
  }

  @Override
  public void setPriceDistributor(com.fluxtion.example.jmh.pricer.node.PriceDistributor arg0) {
    beforeServiceCall(
        "public default void com.fluxtion.example.jmh.pricer.PriceCalculator.setPriceDistributor(com.fluxtion.example.jmh.pricer.node.PriceDistributor)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    isDirty_skewCalculator_2 = true;
    skewCalculator_2.setPriceDistributor(arg0);
    isDirty_levelsCalculator_1 = true;
    levelsCalculator_1.setPriceDistributor(arg0);
    priceLadderPublisher_0.setPriceDistributor(arg0);
    afterServiceCall();
  }

  @Override
  public void setSkew(int arg0) {
    beforeServiceCall(
        "public void com.fluxtion.example.jmh.pricer.node.SkewCalculator.setSkew(int)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    isDirty_skewCalculator_2 = true;
    skewCalculator_2.setSkew(arg0);
    isDirty_levelsCalculator_1 = true;
    levelsCalculator_1.setSkew(arg0);
    priceLadderPublisher_0.setSkew(arg0);
    afterServiceCall();
  }
  //EXPORTED SERVICE FUNCTIONS - END

  public void bufferEvent(Object event) {
    buffering = true;
    if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      auditEvent(typedEvent);
      clock.setClockStrategy(typedEvent);
    }
  }

  public void triggerCalculation() {
    buffering = false;
    String typedEvent = "No event information - buffered dispatch";
    if (guardCheck_skewCalculator_2()) {
      isDirty_skewCalculator_2 = true;
      skewCalculator_2.calculateSkewedLadder();
    }
    if (guardCheck_levelsCalculator_1()) {
      isDirty_levelsCalculator_1 = true;
      levelsCalculator_1.calculateLevelsForLadder();
    }
    if (guardCheck_priceLadderPublisher_0()) {
      priceLadderPublisher_0.publishPriceLadder();
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
    auditor.nodeRegistered(levelsCalculator_1, "levelsCalculator_1");
    auditor.nodeRegistered(midCalculator_3, "midCalculator_3");
    auditor.nodeRegistered(priceLadderPublisher_0, "priceLadderPublisher_0");
    auditor.nodeRegistered(skewCalculator_2, "skewCalculator_2");
    auditor.nodeRegistered(callbackDispatcher, "callbackDispatcher");
    auditor.nodeRegistered(subscriptionManager, "subscriptionManager");
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
    isDirty_levelsCalculator_1 = false;
    isDirty_midCalculator_3 = false;
    isDirty_skewCalculator_2 = false;
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
      dirtyFlagSupplierMap.put(levelsCalculator_1, () -> isDirty_levelsCalculator_1);
      dirtyFlagSupplierMap.put(midCalculator_3, () -> isDirty_midCalculator_3);
      dirtyFlagSupplierMap.put(skewCalculator_2, () -> isDirty_skewCalculator_2);
    }
    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(levelsCalculator_1, (b) -> isDirty_levelsCalculator_1 = b);
      dirtyFlagUpdateMap.put(midCalculator_3, (b) -> isDirty_midCalculator_3 = b);
      dirtyFlagUpdateMap.put(skewCalculator_2, (b) -> isDirty_skewCalculator_2 = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_levelsCalculator_1() {
    return isDirty_skewCalculator_2;
  }

  private boolean guardCheck_priceLadderPublisher_0() {
    return isDirty_levelsCalculator_1;
  }

  private boolean guardCheck_skewCalculator_2() {
    return isDirty_midCalculator_3;
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
  public PriceLadderProcessor newInstance() {
    return new PriceLadderProcessor();
  }

  @Override
  public PriceLadderProcessor newInstance(Map<Object, Object> contextMap) {
    return new PriceLadderProcessor();
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
}
