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
package com.fluxtion.example.cookbook.parallel.aot;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.example.cookbook.parallel.RequestHandler;
import com.fluxtion.example.cookbook.parallel.SimulatedTask.Asynchronous;
import com.fluxtion.example.cookbook.parallel.TaskCollector;
import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.audit.EventLogManager;
import com.fluxtion.runtime.audit.NodeNameAuditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.input.EventFeed;
import com.fluxtion.runtime.input.SubscriptionManagerNode;
import com.fluxtion.runtime.node.ForkedTriggerTask;
import com.fluxtion.runtime.node.MutableEventProcessorContext;
import com.fluxtion.runtime.node.MutableEventProcessorContext;
import java.util.Arrays;
import java.util.Map;

import java.util.IdentityHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 *
 *
 * <pre>
 * generation time                 : 2023-04-11T10:15:08.563461
 * eventProcessorGenerator version : 9.0.1
 * api version                     : 9.0.1
 * </pre>
 *
 * Event classes supported:
 *
 * <ul>
 *   <li>java.lang.String
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public class AotParallelProcessor
    implements EventProcessor<AotParallelProcessor>,
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle {

  //Node declarations
  private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  public final NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final RequestHandler requestHandler_2 = new RequestHandler();
  private final Asynchronous asynchronous_1 = new Asynchronous("async1", 250, requestHandler_2);
  private final Asynchronous asynchronous_3 = new Asynchronous("async2", 225, requestHandler_2);
  private final Asynchronous asynchronous_4 = new Asynchronous("async3", 18, requestHandler_2);
  private final Asynchronous asynchronous_5 = new Asynchronous("async4", 185, requestHandler_2);
  private final SubscriptionManagerNode subscriptionManager = new SubscriptionManagerNode();
  private final MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  public final TaskCollector taskCollector =
      new TaskCollector(
          Arrays.asList(asynchronous_1, asynchronous_3, asynchronous_4, asynchronous_5),
          requestHandler_2);
  //Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(5);
  private final IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(5);

  private boolean isDirty_asynchronous_1 = false;
  private boolean isDirty_asynchronous_3 = false;
  private boolean isDirty_asynchronous_4 = false;
  private boolean isDirty_asynchronous_5 = false;
  private boolean isDirty_requestHandler_2 = false;
  //Forked declarations
  private final ForkedTriggerTask fork_asynchronous_1 =
      new ForkedTriggerTask(asynchronous_1::executeTask, "asynchronous_1");
  private final ForkedTriggerTask fork_asynchronous_3 =
      new ForkedTriggerTask(asynchronous_3::executeTask, "asynchronous_3");
  private final ForkedTriggerTask fork_asynchronous_4 =
      new ForkedTriggerTask(asynchronous_4::executeTask, "asynchronous_4");
  private final ForkedTriggerTask fork_asynchronous_5 =
      new ForkedTriggerTask(asynchronous_5::executeTask, "asynchronous_5");

  //Filter constants

  public AotParallelProcessor(Map<Object, Object> contextMap) {
    context.replaceMappings(contextMap);
    //node auditors
    initialiseAuditor(nodeNameLookup);
    subscriptionManager.setSubscribingEventProcessor(this);
    context.setEventProcessorCallback(this);
  }

  public AotParallelProcessor() {
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
    if (event instanceof java.lang.String) {
      String typedEvent = (String) event;
      handleEvent(typedEvent);
    }
  }

  public void handleEvent(String typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_requestHandler_2 = requestHandler_2.stringRequest(typedEvent);
    if (guardCheck_asynchronous_1()) {
      fork_asynchronous_1.onTrigger();
    }
    if (guardCheck_asynchronous_3()) {
      fork_asynchronous_3.onTrigger();
    }
    if (guardCheck_asynchronous_4()) {
      fork_asynchronous_4.onTrigger();
    }
    if (guardCheck_asynchronous_5()) {
      fork_asynchronous_5.onTrigger();
    }
    if (guardCheck_taskCollector()) {
      taskCollector.collectResults();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void bufferEvent(Object event) {
    buffering = true;
    if (event instanceof java.lang.String) {
      String typedEvent = (String) event;
      auditEvent(typedEvent);
      isDirty_requestHandler_2 = requestHandler_2.stringRequest(typedEvent);
      //event stack unwind callbacks
    }
  }

  public void triggerCalculation() {
    buffering = false;
    String typedEvent = "No event information - buffered dispatch";
    if (guardCheck_asynchronous_1()) {
      fork_asynchronous_1.onTrigger();
    }
    if (guardCheck_asynchronous_3()) {
      fork_asynchronous_3.onTrigger();
    }
    if (guardCheck_asynchronous_4()) {
      fork_asynchronous_4.onTrigger();
    }
    if (guardCheck_asynchronous_5()) {
      fork_asynchronous_5.onTrigger();
    }
    if (guardCheck_taskCollector()) {
      taskCollector.collectResults();
    }
    afterEvent();
  }

  private void auditEvent(Object typedEvent) {
    nodeNameLookup.eventReceived(typedEvent);
  }

  private void auditEvent(Event typedEvent) {
    nodeNameLookup.eventReceived(typedEvent);
  }

  private void initialiseAuditor(Auditor auditor) {
    auditor.init();
    auditor.nodeRegistered(requestHandler_2, "requestHandler_2");
    auditor.nodeRegistered(asynchronous_1, "asynchronous_1");
    auditor.nodeRegistered(asynchronous_3, "asynchronous_3");
    auditor.nodeRegistered(asynchronous_4, "asynchronous_4");
    auditor.nodeRegistered(asynchronous_5, "asynchronous_5");
    auditor.nodeRegistered(taskCollector, "taskCollector");
    auditor.nodeRegistered(callbackDispatcher, "callbackDispatcher");
    auditor.nodeRegistered(subscriptionManager, "subscriptionManager");
    auditor.nodeRegistered(context, "context");
    auditor.nodeRegistered(fork_asynchronous_1, "fork_asynchronous_1");
    auditor.nodeRegistered(fork_asynchronous_3, "fork_asynchronous_3");
    auditor.nodeRegistered(fork_asynchronous_4, "fork_asynchronous_4");
    auditor.nodeRegistered(fork_asynchronous_5, "fork_asynchronous_5");
  }

  private void afterEvent() {
    fork_asynchronous_1.reinitialize();
    fork_asynchronous_3.reinitialize();
    fork_asynchronous_4.reinitialize();
    fork_asynchronous_5.reinitialize();
    nodeNameLookup.processingComplete();
    isDirty_asynchronous_1 = false;
    isDirty_asynchronous_3 = false;
    isDirty_asynchronous_4 = false;
    isDirty_asynchronous_5 = false;
    isDirty_requestHandler_2 = false;
    fork_asynchronous_1.reinitialize();
    fork_asynchronous_3.reinitialize();
    fork_asynchronous_4.reinitialize();
    fork_asynchronous_5.reinitialize();
  }

  @Override
  public void init() {
    initCalled = true;
    //initialise dirty lookup map
    isDirty("test");
  }

  @Override
  public void start() {
    if (!initCalled) {
      throw new RuntimeException("init() must be called before start()");
    }
  }

  @Override
  public void stop() {
    if (!initCalled) {
      throw new RuntimeException("init() must be called before stop()");
    }
  }

  @Override
  public void tearDown() {
    initCalled = false;
    nodeNameLookup.tearDown();
    subscriptionManager.tearDown();
  }

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}

  @Override
  public boolean isDirty(Object node) {
    return dirtySupplier(node).getAsBoolean();
  }

  public BooleanSupplier dirtySupplier(Object node) {
    if (dirtyFlagSupplierMap.isEmpty()) {
      dirtyFlagSupplierMap.put(asynchronous_1, () -> isDirty_asynchronous_1);
      dirtyFlagSupplierMap.put(asynchronous_3, () -> isDirty_asynchronous_3);
      dirtyFlagSupplierMap.put(asynchronous_4, () -> isDirty_asynchronous_4);
      dirtyFlagSupplierMap.put(asynchronous_5, () -> isDirty_asynchronous_5);
      dirtyFlagSupplierMap.put(requestHandler_2, () -> isDirty_requestHandler_2);
    }
    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(asynchronous_1, (b) -> isDirty_asynchronous_1 = b);
      dirtyFlagUpdateMap.put(asynchronous_3, (b) -> isDirty_asynchronous_3 = b);
      dirtyFlagUpdateMap.put(asynchronous_4, (b) -> isDirty_asynchronous_4 = b);
      dirtyFlagUpdateMap.put(asynchronous_5, (b) -> isDirty_asynchronous_5 = b);
      dirtyFlagUpdateMap.put(requestHandler_2, (b) -> isDirty_requestHandler_2 = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_asynchronous_1() {
    return isDirty_requestHandler_2;
  }

  private boolean guardCheck_asynchronous_3() {
    return isDirty_requestHandler_2;
  }

  private boolean guardCheck_asynchronous_4() {
    return isDirty_requestHandler_2;
  }

  private boolean guardCheck_asynchronous_5() {
    return isDirty_requestHandler_2;
  }

  private boolean guardCheck_taskCollector() {
    isDirty_asynchronous_1 = fork_asynchronous_1.afterEvent();
    if (isDirty_asynchronous_1) {}
    isDirty_asynchronous_3 = fork_asynchronous_3.afterEvent();
    if (isDirty_asynchronous_3) {}
    isDirty_asynchronous_4 = fork_asynchronous_4.afterEvent();
    if (isDirty_asynchronous_4) {}
    isDirty_asynchronous_5 = fork_asynchronous_5.afterEvent();
    if (isDirty_asynchronous_5) {}
    return isDirty_asynchronous_1
        | isDirty_asynchronous_3
        | isDirty_asynchronous_4
        | isDirty_asynchronous_5
        | isDirty_requestHandler_2;
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
  public AotParallelProcessor newInstance() {
    return new AotParallelProcessor();
  }

  @Override
  public AotParallelProcessor newInstance(Map<Object, Object> contextMap) {
    return new AotParallelProcessor();
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
