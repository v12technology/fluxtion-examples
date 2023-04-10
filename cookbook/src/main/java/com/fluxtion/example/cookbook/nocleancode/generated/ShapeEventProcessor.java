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
package com.fluxtion.example.cookbook.nocleancode.generated;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.example.cookbook.nocleancode.AreaCalculator;
import com.fluxtion.example.cookbook.nocleancode.Shape;
import com.fluxtion.example.cookbook.nocleancode.ShapeProcessor;
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
import java.util.Map;

import java.util.IdentityHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 *
 *
 * <pre>
 * generation time                 : 2023-04-10T11:30:54.290871
 * eventProcessorGenerator version : 9.0.1
 * api version                     : 9.0.1
 * </pre>
 *
 * Event classes supported:
 *
 * <ul>
 *   <li>com.fluxtion.example.cookbook.nocleancode.Shape
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public class ShapeEventProcessor
    implements EventProcessor<ShapeEventProcessor>,
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle,
        ShapeProcessor {

  //Node declarations
  private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  public final NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final SubscriptionManagerNode subscriptionManager = new SubscriptionManagerNode();
  private final MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  private final AreaCalculator areaCalculator_0 = new AreaCalculator();
  //Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(0);
  private final IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(0);

  //Forked declarations

  //Filter constants

  public ShapeEventProcessor(Map<Object, Object> contextMap) {
    context.replaceMappings(contextMap);
    //node auditors
    initialiseAuditor(nodeNameLookup);
    subscriptionManager.setSubscribingEventProcessor(this);
    context.setEventProcessorCallback(this);
  }

  public ShapeEventProcessor() {
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
    if (event instanceof com.fluxtion.example.cookbook.nocleancode.Shape) {
      Shape typedEvent = (Shape) event;
      handleEvent(typedEvent);
    }
  }

  public void handleEvent(Shape typedEvent) {
    auditEvent(typedEvent);
    switch (typedEvent.filterId()) {
        //Event Class:[com.fluxtion.example.cookbook.nocleancode.Shape] filterId:[0]
      case (0):
        AreaCalculator.circle(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.example.cookbook.nocleancode.Shape] filterId:[3]
      case (3):
        AreaCalculator.triangle(typedEvent);
        afterEvent();
        return;
        //Event Class:[com.fluxtion.example.cookbook.nocleancode.Shape] filterId:[4]
      case (4):
        AreaCalculator.rectangle(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  public void bufferEvent(Object event) {
    buffering = true;
    if (event instanceof com.fluxtion.example.cookbook.nocleancode.Shape) {
      Shape typedEvent = (Shape) event;
      auditEvent(typedEvent);
      switch (typedEvent.filterId()) {
          //Event Class:[com.fluxtion.example.cookbook.nocleancode.Shape] filterId:[0]
        case (0):
          AreaCalculator.circle(typedEvent);
          //event stack unwind callbacks
          afterEvent();
          return;
          //Event Class:[com.fluxtion.example.cookbook.nocleancode.Shape] filterId:[3]
        case (3):
          AreaCalculator.triangle(typedEvent);
          //event stack unwind callbacks
          afterEvent();
          return;
          //Event Class:[com.fluxtion.example.cookbook.nocleancode.Shape] filterId:[4]
        case (4):
          AreaCalculator.rectangle(typedEvent);
          //event stack unwind callbacks
          afterEvent();
          return;
      }
    }
  }

  public void triggerCalculation() {
    buffering = false;
    String typedEvent = "No event information - buffered dispatch";
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
    auditor.nodeRegistered(areaCalculator_0, "areaCalculator_0");
    auditor.nodeRegistered(callbackDispatcher, "callbackDispatcher");
    auditor.nodeRegistered(subscriptionManager, "subscriptionManager");
    auditor.nodeRegistered(context, "context");
  }

  private void afterEvent() {

    nodeNameLookup.processingComplete();
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
    areaCalculator_0.finishCalc();
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
    if (dirtyFlagSupplierMap.isEmpty()) {}

    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {}

    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
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
  public ShapeEventProcessor newInstance() {
    return new ShapeEventProcessor();
  }

  @Override
  public ShapeEventProcessor newInstance(Map<Object, Object> contextMap) {
    return new ShapeEventProcessor();
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
