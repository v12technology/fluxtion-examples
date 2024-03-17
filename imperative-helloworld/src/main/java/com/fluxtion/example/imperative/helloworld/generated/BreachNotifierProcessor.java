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
package com.fluxtion.example.imperative.helloworld.generated;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.example.imperative.helloworld.BreachNotifier;
import com.fluxtion.example.imperative.helloworld.DataSumCalculator;
import com.fluxtion.example.imperative.helloworld.Event_A;
import com.fluxtion.example.imperative.helloworld.Event_A_Handler;
import com.fluxtion.example.imperative.helloworld.Event_B;
import com.fluxtion.example.imperative.helloworld.Event_B_Handler;
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
 * eventProcessorGenerator version : 9.2.17
 * api version                     : 9.2.17
 * </pre>
 *
 * Event classes supported:
 *
 * <ul>
 *   <li>com.fluxtion.example.imperative.helloworld.Event_A
 *   <li>com.fluxtion.example.imperative.helloworld.Event_B
 *   <li>com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class BreachNotifierProcessor
    implements EventProcessor<BreachNotifierProcessor>,
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle {

  //Node declarations
  private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  private final Event_A_Handler event_A_Handler_2 = new Event_A_Handler();
  private final Event_B_Handler event_B_Handler_3 = new Event_B_Handler();
  private final DataSumCalculator dataSumCalculator_1 =
      new DataSumCalculator(event_A_Handler_2, event_B_Handler_3);
  private final BreachNotifier breachNotifier_0 = new BreachNotifier(dataSumCalculator_1);
  public final NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
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

  private boolean isDirty_dataSumCalculator_1 = false;
  private boolean isDirty_event_A_Handler_2 = false;
  private boolean isDirty_event_B_Handler_3 = false;
  //Forked declarations

  //Filter constants

  public BreachNotifierProcessor(Map<Object, Object> contextMap) {
    context.replaceMappings(contextMap);
    //node auditors
    initialiseAuditor(clock);
    initialiseAuditor(nodeNameLookup);
    subscriptionManager.setSubscribingEventProcessor(this);
    context.setEventProcessorCallback(this);
  }

  public BreachNotifierProcessor() {
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
    if (event instanceof com.fluxtion.example.imperative.helloworld.Event_A) {
      Event_A typedEvent = (Event_A) event;
      handleEvent(typedEvent);
    } else if (event instanceof com.fluxtion.example.imperative.helloworld.Event_B) {
      Event_B typedEvent = (Event_B) event;
      handleEvent(typedEvent);
    } else if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      handleEvent(typedEvent);
    }
  }

  public void handleEvent(Event_A typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_event_A_Handler_2 = event_A_Handler_2.data1Update(typedEvent);
    if (guardCheck_dataSumCalculator_1()) {
      isDirty_dataSumCalculator_1 = dataSumCalculator_1.calculate();
    }
    if (guardCheck_breachNotifier_0()) {
      breachNotifier_0.printWarning();
    }
    afterEvent();
  }

  public void handleEvent(Event_B typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_event_B_Handler_3 = event_B_Handler_3.data1Update(typedEvent);
    if (guardCheck_dataSumCalculator_1()) {
      isDirty_dataSumCalculator_1 = dataSumCalculator_1.calculate();
    }
    if (guardCheck_breachNotifier_0()) {
      breachNotifier_0.printWarning();
    }
    afterEvent();
  }

  public void handleEvent(ClockStrategyEvent typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    clock.setClockStrategy(typedEvent);
    afterEvent();
  }
  //EVENT DISPATCH - END

  public void bufferEvent(Object event) {
    buffering = true;
    if (event instanceof com.fluxtion.example.imperative.helloworld.Event_A) {
      Event_A typedEvent = (Event_A) event;
      auditEvent(typedEvent);
      isDirty_event_A_Handler_2 = event_A_Handler_2.data1Update(typedEvent);
    } else if (event instanceof com.fluxtion.example.imperative.helloworld.Event_B) {
      Event_B typedEvent = (Event_B) event;
      auditEvent(typedEvent);
      isDirty_event_B_Handler_3 = event_B_Handler_3.data1Update(typedEvent);
    } else if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      auditEvent(typedEvent);
      clock.setClockStrategy(typedEvent);
    }
  }

  public void triggerCalculation() {
    buffering = false;
    String typedEvent = "No event information - buffered dispatch";
    if (guardCheck_dataSumCalculator_1()) {
      isDirty_dataSumCalculator_1 = dataSumCalculator_1.calculate();
    }
    if (guardCheck_breachNotifier_0()) {
      breachNotifier_0.printWarning();
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
    auditor.nodeRegistered(breachNotifier_0, "breachNotifier_0");
    auditor.nodeRegistered(dataSumCalculator_1, "dataSumCalculator_1");
    auditor.nodeRegistered(event_A_Handler_2, "event_A_Handler_2");
    auditor.nodeRegistered(event_B_Handler_3, "event_B_Handler_3");
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
    isDirty_dataSumCalculator_1 = false;
    isDirty_event_A_Handler_2 = false;
    isDirty_event_B_Handler_3 = false;
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
      dirtyFlagSupplierMap.put(dataSumCalculator_1, () -> isDirty_dataSumCalculator_1);
      dirtyFlagSupplierMap.put(event_A_Handler_2, () -> isDirty_event_A_Handler_2);
      dirtyFlagSupplierMap.put(event_B_Handler_3, () -> isDirty_event_B_Handler_3);
    }
    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(dataSumCalculator_1, (b) -> isDirty_dataSumCalculator_1 = b);
      dirtyFlagUpdateMap.put(event_A_Handler_2, (b) -> isDirty_event_A_Handler_2 = b);
      dirtyFlagUpdateMap.put(event_B_Handler_3, (b) -> isDirty_event_B_Handler_3 = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_breachNotifier_0() {
    return isDirty_dataSumCalculator_1;
  }

  private boolean guardCheck_dataSumCalculator_1() {
    return isDirty_event_A_Handler_2 | isDirty_event_B_Handler_3;
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
  public BreachNotifierProcessor newInstance() {
    return new BreachNotifierProcessor();
  }

  @Override
  public BreachNotifierProcessor newInstance(Map<Object, Object> contextMap) {
    return new BreachNotifierProcessor();
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
}
