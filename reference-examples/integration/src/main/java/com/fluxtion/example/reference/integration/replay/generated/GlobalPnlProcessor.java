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
package com.fluxtion.example.reference.integration.replay.generated;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.compiler.replay.YamlReplayRecordWriter;
import com.fluxtion.example.reference.integration.replay.BookPnl;
import com.fluxtion.example.reference.integration.replay.GlobalPnl;
import com.fluxtion.example.reference.integration.replay.PnlUpdate;
import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.audit.EventLogManager;
import com.fluxtion.runtime.audit.NodeNameAuditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.callback.ExportFunctionAuditEvent;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.input.EventFeed;
import com.fluxtion.runtime.input.SubscriptionManager;
import com.fluxtion.runtime.input.SubscriptionManagerNode;
import com.fluxtion.runtime.node.ForkedTriggerTask;
import com.fluxtion.runtime.node.MutableEventProcessorContext;
import com.fluxtion.runtime.service.ServiceListener;
import com.fluxtion.runtime.service.ServiceRegistryNode;
import com.fluxtion.runtime.time.Clock;
import com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
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
 *   <li>com.fluxtion.example.reference.integration.replay.PnlUpdate
 *   <li>com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class GlobalPnlProcessor
    implements EventProcessor<GlobalPnlProcessor>,
        /*--- @ExportService start ---*/
        @ExportService ServiceListener,
        /*--- @ExportService end ---*/
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle {

  //Node declarations
  private final transient BookPnl bookPnl_1 = new BookPnl("book1");
  private final transient BookPnl bookPnl_2 = new BookPnl("bookAAA");
  private final transient BookPnl bookPnl_3 = new BookPnl("book_XYZ");
  private final transient CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  public final transient Clock clock = new Clock();
  private final transient GlobalPnl globalPnl_0 =
      new GlobalPnl(Arrays.asList(bookPnl_1, bookPnl_2, bookPnl_3));
  public final transient NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final transient SubscriptionManagerNode subscriptionManager =
      new SubscriptionManagerNode();
  private final transient MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  public final transient ServiceRegistryNode serviceRegistry = new ServiceRegistryNode();
  public final transient YamlReplayRecordWriter yamlReplayRecordWriter =
      new YamlReplayRecordWriter(clock);
  private final transient ExportFunctionAuditEvent functionAudit = new ExportFunctionAuditEvent();
  //Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final transient IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(4);
  private final transient IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(4);

  private boolean isDirty_bookPnl_1 = false;
  private boolean isDirty_bookPnl_2 = false;
  private boolean isDirty_bookPnl_3 = false;
  private boolean isDirty_clock = false;

  //Forked declarations

  //Filter constants

  //unknown event handler
  private Consumer unKnownEventHandler = (e) -> {};

  public GlobalPnlProcessor(Map<Object, Object> contextMap) {
    if (context != null) {
      context.replaceMappings(contextMap);
    }
    yamlReplayRecordWriter.setClassBlackList(new HashSet<>(Arrays.asList()));
    yamlReplayRecordWriter.setClassWhiteList(
        new HashSet<>(
            Arrays.asList(com.fluxtion.example.reference.integration.replay.PnlUpdate.class)));
    globalPnl_0.clock = clock;
    context.setClock(clock);
    serviceRegistry.setEventProcessorContext(context);
    //node auditors
    initialiseAuditor(clock);
    initialiseAuditor(yamlReplayRecordWriter);
    initialiseAuditor(nodeNameLookup);
    initialiseAuditor(serviceRegistry);
    if (subscriptionManager != null) {
      subscriptionManager.setSubscribingEventProcessor(this);
    }
    if (context != null) {
      context.setEventProcessorCallback(this);
    }
  }

  public GlobalPnlProcessor() {
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
    globalPnl_0.start();
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
    yamlReplayRecordWriter.tearDown();
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
    if (event instanceof com.fluxtion.example.reference.integration.replay.PnlUpdate) {
      PnlUpdate typedEvent = (PnlUpdate) event;
      handleEvent(typedEvent);
    } else if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      handleEvent(typedEvent);
    } else {
      unKnownEventHandler(event);
    }
  }

  public void handleEvent(PnlUpdate typedEvent) {
    auditEvent(typedEvent);
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.example.reference.integration.replay.PnlUpdate] filterString:[book1]
      case ("book1"):
        handle_PnlUpdate_book1(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.example.reference.integration.replay.PnlUpdate] filterString:[bookAAA]
      case ("bookAAA"):
        handle_PnlUpdate_bookAAA(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.example.reference.integration.replay.PnlUpdate] filterString:[book_XYZ]
      case ("book_XYZ"):
        handle_PnlUpdate_book_XYZ(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  public void handleEvent(ClockStrategyEvent typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_clock = true;
    clock.setClockStrategy(typedEvent);
    afterEvent();
  }
  //EVENT DISPATCH - END

  //FILTERED DISPATCH - START
  private void handle_PnlUpdate_book1(PnlUpdate typedEvent) {
    isDirty_bookPnl_1 = bookPnl_1.pnlUpdate(typedEvent);
    if (guardCheck_globalPnl_0()) {
      globalPnl_0.calculate();
    }
  }

  private void handle_PnlUpdate_bookAAA(PnlUpdate typedEvent) {
    isDirty_bookPnl_2 = bookPnl_2.pnlUpdate(typedEvent);
    if (guardCheck_globalPnl_0()) {
      globalPnl_0.calculate();
    }
  }

  private void handle_PnlUpdate_book_XYZ(PnlUpdate typedEvent) {
    isDirty_bookPnl_3 = bookPnl_3.pnlUpdate(typedEvent);
    if (guardCheck_globalPnl_0()) {
      globalPnl_0.calculate();
    }
  }
  //FILTERED DISPATCH - END

  //EXPORTED SERVICE FUNCTIONS - START
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
  //EXPORTED SERVICE FUNCTIONS - END

  //EVENT BUFFERING - START
  public void bufferEvent(Object event) {
    buffering = true;
    if (event instanceof com.fluxtion.example.reference.integration.replay.PnlUpdate) {
      PnlUpdate typedEvent = (PnlUpdate) event;
      auditEvent(typedEvent);
      switch (typedEvent.filterString()) {
          //Event Class:[com.fluxtion.example.reference.integration.replay.PnlUpdate] filterString:[book1]
        case ("book1"):
          handle_PnlUpdate_book1_bufferDispatch(typedEvent);
          afterEvent();
          return;
          //Event Class:[com.fluxtion.example.reference.integration.replay.PnlUpdate] filterString:[bookAAA]
        case ("bookAAA"):
          handle_PnlUpdate_bookAAA_bufferDispatch(typedEvent);
          afterEvent();
          return;
          //Event Class:[com.fluxtion.example.reference.integration.replay.PnlUpdate] filterString:[book_XYZ]
        case ("book_XYZ"):
          handle_PnlUpdate_book_XYZ_bufferDispatch(typedEvent);
          afterEvent();
          return;
      }
    } else if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      auditEvent(typedEvent);
      isDirty_clock = true;
      clock.setClockStrategy(typedEvent);
    }
  }

  private void handle_PnlUpdate_book1_bufferDispatch(PnlUpdate typedEvent) {
    isDirty_bookPnl_1 = bookPnl_1.pnlUpdate(typedEvent);
  }

  private void handle_PnlUpdate_bookAAA_bufferDispatch(PnlUpdate typedEvent) {
    isDirty_bookPnl_2 = bookPnl_2.pnlUpdate(typedEvent);
  }

  private void handle_PnlUpdate_book_XYZ_bufferDispatch(PnlUpdate typedEvent) {
    isDirty_bookPnl_3 = bookPnl_3.pnlUpdate(typedEvent);
  }

  public void triggerCalculation() {
    buffering = false;
    String typedEvent = "No event information - buffered dispatch";
    if (guardCheck_globalPnl_0()) {
      globalPnl_0.calculate();
    }
    afterEvent();
  }
  //EVENT BUFFERING - END

  private void auditEvent(Object typedEvent) {
    clock.eventReceived(typedEvent);
    yamlReplayRecordWriter.eventReceived(typedEvent);
    nodeNameLookup.eventReceived(typedEvent);
    serviceRegistry.eventReceived(typedEvent);
  }

  private void auditEvent(Event typedEvent) {
    clock.eventReceived(typedEvent);
    yamlReplayRecordWriter.eventReceived(typedEvent);
    nodeNameLookup.eventReceived(typedEvent);
    serviceRegistry.eventReceived(typedEvent);
  }

  private void initialiseAuditor(Auditor auditor) {
    auditor.init();
    auditor.nodeRegistered(bookPnl_1, "bookPnl_1");
    auditor.nodeRegistered(bookPnl_2, "bookPnl_2");
    auditor.nodeRegistered(bookPnl_3, "bookPnl_3");
    auditor.nodeRegistered(globalPnl_0, "globalPnl_0");
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
    yamlReplayRecordWriter.processingComplete();
    nodeNameLookup.processingComplete();
    serviceRegistry.processingComplete();
    isDirty_bookPnl_1 = false;
    isDirty_bookPnl_2 = false;
    isDirty_bookPnl_3 = false;
    isDirty_clock = false;
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
      dirtyFlagSupplierMap.put(bookPnl_1, () -> isDirty_bookPnl_1);
      dirtyFlagSupplierMap.put(bookPnl_2, () -> isDirty_bookPnl_2);
      dirtyFlagSupplierMap.put(bookPnl_3, () -> isDirty_bookPnl_3);
      dirtyFlagSupplierMap.put(clock, () -> isDirty_clock);
    }
    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(bookPnl_1, (b) -> isDirty_bookPnl_1 = b);
      dirtyFlagUpdateMap.put(bookPnl_2, (b) -> isDirty_bookPnl_2 = b);
      dirtyFlagUpdateMap.put(bookPnl_3, (b) -> isDirty_bookPnl_3 = b);
      dirtyFlagUpdateMap.put(clock, (b) -> isDirty_clock = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_yamlReplayRecordWriter() {
    return isDirty_clock;
  }

  private boolean guardCheck_globalPnl_0() {
    return isDirty_bookPnl_1 | isDirty_bookPnl_2 | isDirty_bookPnl_3 | isDirty_clock;
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
  public GlobalPnlProcessor newInstance() {
    return new GlobalPnlProcessor();
  }

  @Override
  public GlobalPnlProcessor newInstance(Map<Object, Object> contextMap) {
    return new GlobalPnlProcessor();
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
