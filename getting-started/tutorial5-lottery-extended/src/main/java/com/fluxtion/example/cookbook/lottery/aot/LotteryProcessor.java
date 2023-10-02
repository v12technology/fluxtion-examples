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
package com.fluxtion.example.cookbook.lottery.aot;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.example.cookbook.lottery.api.GameResultStore;
import com.fluxtion.example.cookbook.lottery.api.LotteryMachine;
import com.fluxtion.example.cookbook.lottery.api.TicketStore;
import com.fluxtion.example.cookbook.lottery.nodes.GameReport;
import com.fluxtion.example.cookbook.lottery.nodes.LotteryMachineNode;
import com.fluxtion.example.cookbook.lottery.nodes.PowerLotteryMachine;
import com.fluxtion.example.cookbook.lottery.nodes.TicketStoreNode;
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
 * generation time                 : 2023-10-02T08:14:22.541663
 * eventProcessorGenerator version : 9.1.10
 * api version                     : 9.1.10
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
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public class LotteryProcessor
    implements EventProcessor<LotteryProcessor>,
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle,
        GameResultStore,
        LotteryMachine,
        TicketStore {

  //Node declarations
  private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  public final NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final SubscriptionManagerNode subscriptionManager = new SubscriptionManagerNode();
  private final MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  public final TicketStoreNode ticketStore = new TicketStoreNode();
  public final LotteryMachineNode lotteryMachine = new LotteryMachineNode(ticketStore);
  public final PowerLotteryMachine powerMachine = new PowerLotteryMachine(ticketStore);
  public final GameReport gameReport = new GameReport(lotteryMachine, powerMachine);
  public final Clock clock = new Clock();
  private ExportFunctionAuditEvent functionAudit = new ExportFunctionAuditEvent();
  //Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(3);
  private final IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(3);

  private boolean isDirty_lotteryMachine = false;
  private boolean isDirty_powerMachine = false;
  private boolean isDirty_ticketStore = false;
  //Forked declarations

  //Filter constants

  public LotteryProcessor(Map<Object, Object> contextMap) {
    context.replaceMappings(contextMap);
    //node auditors
    initialiseAuditor(clock);
    initialiseAuditor(nodeNameLookup);
    subscriptionManager.setSubscribingEventProcessor(this);
    context.setEventProcessorCallback(this);
  }

  public LotteryProcessor() {
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
    ticketStore.start();
    lotteryMachine.start();
    powerMachine.start();
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
  public boolean buyTicket(com.fluxtion.example.cookbook.lottery.api.Ticket arg0) {
    beforeServiceCall(
        "public boolean com.fluxtion.example.cookbook.lottery.nodes.TicketStoreNode.buyTicket(com.fluxtion.example.cookbook.lottery.api.Ticket)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    isDirty_ticketStore = ticketStore.buyTicket(arg0);
    if (guardCheck_lotteryMachine()) {
      isDirty_lotteryMachine = true;
      lotteryMachine.processNewTicketSale();
    }
    if (guardCheck_powerMachine()) {
      isDirty_powerMachine = powerMachine.processNewTicketSale();
    }
    afterServiceCall();
    return true;
  }

  @Override
  public boolean isTicketSuccessful(
      com.fluxtion.example.cookbook.lottery.api.Ticket arg0,
      java.util.function.Consumer<Boolean> arg1) {
    beforeServiceCall(
        "public boolean com.fluxtion.example.cookbook.lottery.nodes.GameReport.isTicketSuccessful(com.fluxtion.example.cookbook.lottery.api.Ticket,java.util.function.Consumer<java.lang.Boolean>)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    gameReport.isTicketSuccessful(arg0, arg1);
    afterServiceCall();
    return true;
  }

  @Override
  public boolean publishReport(java.util.function.Consumer<String> arg0) {
    beforeServiceCall(
        "public boolean com.fluxtion.example.cookbook.lottery.nodes.GameReport.publishReport(java.util.function.Consumer<java.lang.String>)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    gameReport.publishReport(arg0);
    afterServiceCall();
    return true;
  }

  @Override
  public void closeStore() {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.lottery.nodes.TicketStoreNode.closeStore()");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    isDirty_ticketStore = true;
    ticketStore.closeStore();
    afterServiceCall();
  }

  @Override
  public void openStore() {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.lottery.nodes.TicketStoreNode.openStore()");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    isDirty_ticketStore = true;
    ticketStore.openStore();
    afterServiceCall();
  }

  @Override
  public void selectWinningTicket() {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.lottery.nodes.LotteryMachineNode.selectWinningTicket()");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    isDirty_lotteryMachine = true;
    lotteryMachine.selectWinningTicket();
    gameReport.selectWinningTicket();
    afterServiceCall();
  }

  @Override
  public void setResultPublisher(java.util.function.Consumer<String> arg0) {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.lottery.nodes.LotteryMachineNode.setResultPublisher(java.util.function.Consumer<java.lang.String>)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    isDirty_lotteryMachine = true;
    lotteryMachine.setResultPublisher(arg0);
    gameReport.setResultPublisher(arg0);
    afterServiceCall();
  }

  @Override
  public void setTicketSalesPublisher(java.util.function.Consumer<String> arg0) {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.lottery.nodes.TicketStoreNode.setTicketSalesPublisher(java.util.function.Consumer<java.lang.String>)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    isDirty_ticketStore = true;
    ticketStore.setTicketSalesPublisher(arg0);
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
    if (guardCheck_lotteryMachine()) {
      isDirty_lotteryMachine = true;
      lotteryMachine.processNewTicketSale();
    }
    if (guardCheck_powerMachine()) {
      isDirty_powerMachine = powerMachine.processNewTicketSale();
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
    auditor.nodeRegistered(gameReport, "gameReport");
    auditor.nodeRegistered(lotteryMachine, "lotteryMachine");
    auditor.nodeRegistered(powerMachine, "powerMachine");
    auditor.nodeRegistered(ticketStore, "ticketStore");
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
    isDirty_lotteryMachine = false;
    isDirty_powerMachine = false;
    isDirty_ticketStore = false;
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
      dirtyFlagSupplierMap.put(lotteryMachine, () -> isDirty_lotteryMachine);
      dirtyFlagSupplierMap.put(powerMachine, () -> isDirty_powerMachine);
      dirtyFlagSupplierMap.put(ticketStore, () -> isDirty_ticketStore);
    }
    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(lotteryMachine, (b) -> isDirty_lotteryMachine = b);
      dirtyFlagUpdateMap.put(powerMachine, (b) -> isDirty_powerMachine = b);
      dirtyFlagUpdateMap.put(ticketStore, (b) -> isDirty_ticketStore = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_gameReport() {
    return isDirty_lotteryMachine | isDirty_powerMachine;
  }

  private boolean guardCheck_lotteryMachine() {
    return isDirty_ticketStore;
  }

  private boolean guardCheck_powerMachine() {
    return isDirty_ticketStore;
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
  public LotteryProcessor newInstance() {
    return new LotteryProcessor();
  }

  @Override
  public LotteryProcessor newInstance(Map<Object, Object> contextMap) {
    return new LotteryProcessor();
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
