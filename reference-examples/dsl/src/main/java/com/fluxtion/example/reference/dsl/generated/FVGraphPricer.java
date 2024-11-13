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
package com.fluxtion.example.reference.dsl.generated;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.example.reference.dsl.frontpage_examples.GraphPricer;
import com.fluxtion.example.reference.dsl.frontpage_examples.GraphPricer.FairValueAbsolute;
import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.audit.EventLogManager;
import com.fluxtion.runtime.audit.NodeNameAuditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.callback.ExportFunctionAuditEvent;
import com.fluxtion.runtime.dataflow.function.BinaryMapFlowFunction.BinaryMapToDoubleFlowFunction;
import com.fluxtion.runtime.dataflow.function.MapFlowFunction.MapDouble2ToDoubleFlowFunction;
import com.fluxtion.runtime.dataflow.function.MapFlowFunction.MapRef2ToDoubleFlowFunction;
import com.fluxtion.runtime.dataflow.function.NodeToFlowFunction;
import com.fluxtion.runtime.dataflow.function.PeekFlowFunction.DoublePeekFlowFunction;
import com.fluxtion.runtime.dataflow.helpers.Mappers;
import com.fluxtion.runtime.dataflow.helpers.Peekers.TemplateMessage;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.event.Signal.DoubleSignal;
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
 * eventProcessorGenerator version : 9.3.45
 * api version                     : 9.3.45
 * </pre>
 *
 * Event classes supported:
 *
 * <ul>
 *   <li>com.fluxtion.compiler.generation.model.ExportFunctionMarker
 *   <li>com.fluxtion.runtime.event.Signal.DoubleSignal
 *   <li>com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class FVGraphPricer
    implements EventProcessor<FVGraphPricer>,
        /*--- @ExportService start ---*/
        ServiceListener,
        /*--- @ExportService end ---*/
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle {

  //Node declarations
  private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  public final Clock clock = new Clock();
  public final NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final SubscriptionManagerNode subscriptionManager = new SubscriptionManagerNode();
  private final MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  private final DefaultEventHandlerNode handlerDoubleSignal_daysToExpiry =
      new DefaultEventHandlerNode<>(
          2147483647,
          "daysToExpiry",
          com.fluxtion.runtime.event.Signal.DoubleSignal.class,
          "handlerDoubleSignal_daysToExpiry",
          context);
  private final DefaultEventHandlerNode handlerDoubleSignal_dividends =
      new DefaultEventHandlerNode<>(
          2147483647,
          "dividends",
          com.fluxtion.runtime.event.Signal.DoubleSignal.class,
          "handlerDoubleSignal_dividends",
          context);
  private final DefaultEventHandlerNode handlerDoubleSignal_futuresPrice =
      new DefaultEventHandlerNode<>(
          2147483647,
          "futuresPrice",
          com.fluxtion.runtime.event.Signal.DoubleSignal.class,
          "handlerDoubleSignal_futuresPrice",
          context);
  private final DefaultEventHandlerNode handlerDoubleSignal_interestRate =
      new DefaultEventHandlerNode<>(
          2147483647,
          "interestRate",
          com.fluxtion.runtime.event.Signal.DoubleSignal.class,
          "handlerDoubleSignal_interestRate",
          context);
  private final DefaultEventHandlerNode handlerDoubleSignal_spot =
      new DefaultEventHandlerNode<>(
          2147483647,
          "spot",
          com.fluxtion.runtime.event.Signal.DoubleSignal.class,
          "handlerDoubleSignal_spot",
          context);
  private final MapRef2ToDoubleFlowFunction mapRef2ToDoubleFlowFunction_0 =
      new MapRef2ToDoubleFlowFunction<>(handlerDoubleSignal_spot, DoubleSignal::getValue);
  private final MapRef2ToDoubleFlowFunction mapRef2ToDoubleFlowFunction_1 =
      new MapRef2ToDoubleFlowFunction<>(handlerDoubleSignal_dividends, DoubleSignal::getValue);
  private final MapRef2ToDoubleFlowFunction mapRef2ToDoubleFlowFunction_2 =
      new MapRef2ToDoubleFlowFunction<>(handlerDoubleSignal_interestRate, DoubleSignal::getValue);
  private final MapRef2ToDoubleFlowFunction mapRef2ToDoubleFlowFunction_3 =
      new MapRef2ToDoubleFlowFunction<>(handlerDoubleSignal_daysToExpiry, DoubleSignal::getValue);
  private final FairValueAbsolute fairValueAbsolute_21 = new FairValueAbsolute();
  private final MapRef2ToDoubleFlowFunction mapRef2ToDoubleFlowFunction_16 =
      new MapRef2ToDoubleFlowFunction<>(handlerDoubleSignal_futuresPrice, DoubleSignal::getValue);
  private final NodeToFlowFunction nodeToFlowFunction_4 =
      new NodeToFlowFunction<>(fairValueAbsolute_21);
  private final MapRef2ToDoubleFlowFunction mapRef2ToDoubleFlowFunction_5 =
      new MapRef2ToDoubleFlowFunction<>(nodeToFlowFunction_4, FairValueAbsolute::getDerivedValue);
  private final MapDouble2ToDoubleFlowFunction mapDouble2ToDoubleFlowFunction_6 =
      new MapDouble2ToDoubleFlowFunction(mapRef2ToDoubleFlowFunction_5, GraphPricer::round2Dps);
  private final BinaryMapToDoubleFlowFunction binaryMapToDoubleFlowFunction_9 =
      new BinaryMapToDoubleFlowFunction<>(
          mapRef2ToDoubleFlowFunction_0,
          mapDouble2ToDoubleFlowFunction_6,
          Mappers::subtractDoubles);
  private final BinaryMapToDoubleFlowFunction binaryMapToDoubleFlowFunction_17 =
      new BinaryMapToDoubleFlowFunction<>(
          mapRef2ToDoubleFlowFunction_16,
          mapDouble2ToDoubleFlowFunction_6,
          Mappers::subtractDoubles);
  private final MapDouble2ToDoubleFlowFunction mapDouble2ToDoubleFlowFunction_10 =
      new MapDouble2ToDoubleFlowFunction(binaryMapToDoubleFlowFunction_9, GraphPricer::round2Dps);
  private final BinaryMapToDoubleFlowFunction binaryMapToDoubleFlowFunction_13 =
      new BinaryMapToDoubleFlowFunction<>(
          mapDouble2ToDoubleFlowFunction_10, mapRef2ToDoubleFlowFunction_0, Double::sum);
  private final MapDouble2ToDoubleFlowFunction mapDouble2ToDoubleFlowFunction_18 =
      new MapDouble2ToDoubleFlowFunction(binaryMapToDoubleFlowFunction_17, GraphPricer::round2Dps);
  public final ServiceRegistryNode serviceRegistry = new ServiceRegistryNode();
  private final TemplateMessage templateMessage_7 = new TemplateMessage<>("fvAbs = {}");
  private final DoublePeekFlowFunction doublePeekFlowFunction_8 =
      new DoublePeekFlowFunction(
          mapDouble2ToDoubleFlowFunction_6, templateMessage_7::templateAndLogToConsole);
  private final TemplateMessage templateMessage_11 =
      new TemplateMessage<>("spotPriceAdjustment:{}");
  private final DoublePeekFlowFunction doublePeekFlowFunction_12 =
      new DoublePeekFlowFunction(
          mapDouble2ToDoubleFlowFunction_10, templateMessage_11::templateAndLogToConsole);
  private final TemplateMessage templateMessage_14 = new TemplateMessage<>("spotPriceFV:{}");
  private final DoublePeekFlowFunction doublePeekFlowFunction_15 =
      new DoublePeekFlowFunction(
          binaryMapToDoubleFlowFunction_13, templateMessage_14::templateAndLogToConsole);
  private final TemplateMessage templateMessage_19 =
      new TemplateMessage<>("futurePriceAdjustment:{}");
  private final DoublePeekFlowFunction doublePeekFlowFunction_20 =
      new DoublePeekFlowFunction(
          mapDouble2ToDoubleFlowFunction_18, templateMessage_19::templateAndLogToConsole);
  private final ExportFunctionAuditEvent functionAudit = new ExportFunctionAuditEvent();
  //Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(20);
  private final IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(20);

  private boolean isDirty_binaryMapToDoubleFlowFunction_9 = false;
  private boolean isDirty_binaryMapToDoubleFlowFunction_13 = false;
  private boolean isDirty_binaryMapToDoubleFlowFunction_17 = false;
  private boolean isDirty_clock = false;
  private boolean isDirty_fairValueAbsolute_21 = false;
  private boolean isDirty_handlerDoubleSignal_daysToExpiry = false;
  private boolean isDirty_handlerDoubleSignal_dividends = false;
  private boolean isDirty_handlerDoubleSignal_futuresPrice = false;
  private boolean isDirty_handlerDoubleSignal_interestRate = false;
  private boolean isDirty_handlerDoubleSignal_spot = false;
  private boolean isDirty_mapDouble2ToDoubleFlowFunction_6 = false;
  private boolean isDirty_mapDouble2ToDoubleFlowFunction_10 = false;
  private boolean isDirty_mapDouble2ToDoubleFlowFunction_18 = false;
  private boolean isDirty_mapRef2ToDoubleFlowFunction_0 = false;
  private boolean isDirty_mapRef2ToDoubleFlowFunction_1 = false;
  private boolean isDirty_mapRef2ToDoubleFlowFunction_2 = false;
  private boolean isDirty_mapRef2ToDoubleFlowFunction_3 = false;
  private boolean isDirty_mapRef2ToDoubleFlowFunction_5 = false;
  private boolean isDirty_mapRef2ToDoubleFlowFunction_16 = false;
  private boolean isDirty_nodeToFlowFunction_4 = false;

  //Forked declarations

  //Filter constants

  //unknown event handler
  private Consumer unKnownEventHandler = (e) -> {};

  public FVGraphPricer(Map<Object, Object> contextMap) {
    if (context != null) {
      context.replaceMappings(contextMap);
    }
    fairValueAbsolute_21.setCashPrice(mapRef2ToDoubleFlowFunction_0);
    fairValueAbsolute_21.setDaysToExpiry(mapRef2ToDoubleFlowFunction_3);
    fairValueAbsolute_21.setDividends(mapRef2ToDoubleFlowFunction_1);
    fairValueAbsolute_21.setInterestRate(mapRef2ToDoubleFlowFunction_2);
    binaryMapToDoubleFlowFunction_9.setEventProcessorContext(context);
    binaryMapToDoubleFlowFunction_13.setEventProcessorContext(context);
    binaryMapToDoubleFlowFunction_17.setEventProcessorContext(context);
    mapDouble2ToDoubleFlowFunction_6.setEventProcessorContext(context);
    mapDouble2ToDoubleFlowFunction_10.setEventProcessorContext(context);
    mapDouble2ToDoubleFlowFunction_18.setEventProcessorContext(context);
    mapRef2ToDoubleFlowFunction_0.setEventProcessorContext(context);
    mapRef2ToDoubleFlowFunction_1.setEventProcessorContext(context);
    mapRef2ToDoubleFlowFunction_2.setEventProcessorContext(context);
    mapRef2ToDoubleFlowFunction_3.setEventProcessorContext(context);
    mapRef2ToDoubleFlowFunction_5.setEventProcessorContext(context);
    mapRef2ToDoubleFlowFunction_16.setEventProcessorContext(context);
    nodeToFlowFunction_4.nodeNameLookup = nodeNameLookup;
    nodeToFlowFunction_4.dirtyStateMonitor = callbackDispatcher;
    doublePeekFlowFunction_8.setEventProcessorContext(context);
    doublePeekFlowFunction_12.setEventProcessorContext(context);
    doublePeekFlowFunction_15.setEventProcessorContext(context);
    doublePeekFlowFunction_20.setEventProcessorContext(context);
    templateMessage_7.clock = clock;
    templateMessage_11.clock = clock;
    templateMessage_14.clock = clock;
    templateMessage_19.clock = clock;
    context.setClock(clock);
    serviceRegistry.setEventProcessorContext(context);
    //node auditors
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

  public FVGraphPricer() {
    this(null);
  }

  @Override
  public void init() {
    initCalled = true;
    auditEvent(Lifecycle.LifecycleEvent.Init);
    //initialise dirty lookup map
    isDirty("test");
    clock.init();
    handlerDoubleSignal_daysToExpiry.init();
    handlerDoubleSignal_dividends.init();
    handlerDoubleSignal_futuresPrice.init();
    handlerDoubleSignal_interestRate.init();
    handlerDoubleSignal_spot.init();
    mapRef2ToDoubleFlowFunction_0.initialiseEventStream();
    mapRef2ToDoubleFlowFunction_1.initialiseEventStream();
    mapRef2ToDoubleFlowFunction_2.initialiseEventStream();
    mapRef2ToDoubleFlowFunction_3.initialiseEventStream();
    fairValueAbsolute_21.init();
    mapRef2ToDoubleFlowFunction_16.initialiseEventStream();
    nodeToFlowFunction_4.init();
    mapRef2ToDoubleFlowFunction_5.initialiseEventStream();
    mapDouble2ToDoubleFlowFunction_6.initialiseEventStream();
    binaryMapToDoubleFlowFunction_9.initialiseEventStream();
    binaryMapToDoubleFlowFunction_17.initialiseEventStream();
    mapDouble2ToDoubleFlowFunction_10.initialiseEventStream();
    binaryMapToDoubleFlowFunction_13.initialiseEventStream();
    mapDouble2ToDoubleFlowFunction_18.initialiseEventStream();
    templateMessage_7.initialise();
    doublePeekFlowFunction_8.initialiseEventStream();
    templateMessage_11.initialise();
    doublePeekFlowFunction_12.initialiseEventStream();
    templateMessage_14.initialise();
    doublePeekFlowFunction_15.initialiseEventStream();
    templateMessage_19.initialise();
    doublePeekFlowFunction_20.initialiseEventStream();
    afterEvent();
  }

  @Override
  public void start() {
    if (!initCalled) {
      throw new RuntimeException("init() must be called before start()");
    }
    processing = true;
    auditEvent(Lifecycle.LifecycleEvent.Start);
    templateMessage_7.start();
    templateMessage_11.start();
    templateMessage_14.start();
    templateMessage_19.start();
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
    handlerDoubleSignal_spot.tearDown();
    handlerDoubleSignal_interestRate.tearDown();
    handlerDoubleSignal_futuresPrice.tearDown();
    handlerDoubleSignal_dividends.tearDown();
    handlerDoubleSignal_daysToExpiry.tearDown();
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
    if (event instanceof com.fluxtion.runtime.event.Signal.DoubleSignal) {
      DoubleSignal typedEvent = (DoubleSignal) event;
      handleEvent(typedEvent);
    } else if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      handleEvent(typedEvent);
    } else {
      unKnownEventHandler(event);
    }
  }

  public void handleEvent(DoubleSignal typedEvent) {
    auditEvent(typedEvent);
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.runtime.event.Signal.DoubleSignal] filterString:[daysToExpiry]
      case ("daysToExpiry"):
        isDirty_handlerDoubleSignal_daysToExpiry =
            handlerDoubleSignal_daysToExpiry.onEvent(typedEvent);
        if (isDirty_handlerDoubleSignal_daysToExpiry) {
          mapRef2ToDoubleFlowFunction_3.inputUpdated(handlerDoubleSignal_daysToExpiry);
        }
        if (guardCheck_mapRef2ToDoubleFlowFunction_3()) {
          isDirty_mapRef2ToDoubleFlowFunction_3 = mapRef2ToDoubleFlowFunction_3.map();
        }
        if (guardCheck_fairValueAbsolute_21()) {
          isDirty_fairValueAbsolute_21 = fairValueAbsolute_21.calculate();
        }
        if (guardCheck_nodeToFlowFunction_4()) {
          isDirty_nodeToFlowFunction_4 = true;
          nodeToFlowFunction_4.sourceUpdated();
          if (isDirty_nodeToFlowFunction_4) {
            mapRef2ToDoubleFlowFunction_5.inputUpdated(nodeToFlowFunction_4);
          }
        }
        if (guardCheck_mapRef2ToDoubleFlowFunction_5()) {
          isDirty_mapRef2ToDoubleFlowFunction_5 = mapRef2ToDoubleFlowFunction_5.map();
          if (isDirty_mapRef2ToDoubleFlowFunction_5) {
            mapDouble2ToDoubleFlowFunction_6.inputUpdated(mapRef2ToDoubleFlowFunction_5);
          }
        }
        if (guardCheck_mapDouble2ToDoubleFlowFunction_6()) {
          isDirty_mapDouble2ToDoubleFlowFunction_6 = mapDouble2ToDoubleFlowFunction_6.map();
          if (isDirty_mapDouble2ToDoubleFlowFunction_6) {
            binaryMapToDoubleFlowFunction_9.input2Updated(mapDouble2ToDoubleFlowFunction_6);
            binaryMapToDoubleFlowFunction_17.input2Updated(mapDouble2ToDoubleFlowFunction_6);
            doublePeekFlowFunction_8.inputUpdated(mapDouble2ToDoubleFlowFunction_6);
          }
        }
        if (guardCheck_binaryMapToDoubleFlowFunction_9()) {
          isDirty_binaryMapToDoubleFlowFunction_9 = binaryMapToDoubleFlowFunction_9.map();
          if (isDirty_binaryMapToDoubleFlowFunction_9) {
            mapDouble2ToDoubleFlowFunction_10.inputUpdated(binaryMapToDoubleFlowFunction_9);
          }
        }
        if (guardCheck_binaryMapToDoubleFlowFunction_17()) {
          isDirty_binaryMapToDoubleFlowFunction_17 = binaryMapToDoubleFlowFunction_17.map();
          if (isDirty_binaryMapToDoubleFlowFunction_17) {
            mapDouble2ToDoubleFlowFunction_18.inputUpdated(binaryMapToDoubleFlowFunction_17);
          }
        }
        if (guardCheck_mapDouble2ToDoubleFlowFunction_10()) {
          isDirty_mapDouble2ToDoubleFlowFunction_10 = mapDouble2ToDoubleFlowFunction_10.map();
          if (isDirty_mapDouble2ToDoubleFlowFunction_10) {
            binaryMapToDoubleFlowFunction_13.inputUpdated(mapDouble2ToDoubleFlowFunction_10);
            doublePeekFlowFunction_12.inputUpdated(mapDouble2ToDoubleFlowFunction_10);
          }
        }
        if (guardCheck_binaryMapToDoubleFlowFunction_13()) {
          isDirty_binaryMapToDoubleFlowFunction_13 = binaryMapToDoubleFlowFunction_13.map();
          if (isDirty_binaryMapToDoubleFlowFunction_13) {
            doublePeekFlowFunction_15.inputUpdated(binaryMapToDoubleFlowFunction_13);
          }
        }
        if (guardCheck_mapDouble2ToDoubleFlowFunction_18()) {
          isDirty_mapDouble2ToDoubleFlowFunction_18 = mapDouble2ToDoubleFlowFunction_18.map();
          if (isDirty_mapDouble2ToDoubleFlowFunction_18) {
            doublePeekFlowFunction_20.inputUpdated(mapDouble2ToDoubleFlowFunction_18);
          }
        }
        if (guardCheck_doublePeekFlowFunction_8()) {
          doublePeekFlowFunction_8.peek();
        }
        if (guardCheck_doublePeekFlowFunction_12()) {
          doublePeekFlowFunction_12.peek();
        }
        if (guardCheck_doublePeekFlowFunction_15()) {
          doublePeekFlowFunction_15.peek();
        }
        if (guardCheck_doublePeekFlowFunction_20()) {
          doublePeekFlowFunction_20.peek();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.runtime.event.Signal.DoubleSignal] filterString:[dividends]
      case ("dividends"):
        isDirty_handlerDoubleSignal_dividends = handlerDoubleSignal_dividends.onEvent(typedEvent);
        if (isDirty_handlerDoubleSignal_dividends) {
          mapRef2ToDoubleFlowFunction_1.inputUpdated(handlerDoubleSignal_dividends);
        }
        if (guardCheck_mapRef2ToDoubleFlowFunction_1()) {
          isDirty_mapRef2ToDoubleFlowFunction_1 = mapRef2ToDoubleFlowFunction_1.map();
        }
        if (guardCheck_fairValueAbsolute_21()) {
          isDirty_fairValueAbsolute_21 = fairValueAbsolute_21.calculate();
        }
        if (guardCheck_nodeToFlowFunction_4()) {
          isDirty_nodeToFlowFunction_4 = true;
          nodeToFlowFunction_4.sourceUpdated();
          if (isDirty_nodeToFlowFunction_4) {
            mapRef2ToDoubleFlowFunction_5.inputUpdated(nodeToFlowFunction_4);
          }
        }
        if (guardCheck_mapRef2ToDoubleFlowFunction_5()) {
          isDirty_mapRef2ToDoubleFlowFunction_5 = mapRef2ToDoubleFlowFunction_5.map();
          if (isDirty_mapRef2ToDoubleFlowFunction_5) {
            mapDouble2ToDoubleFlowFunction_6.inputUpdated(mapRef2ToDoubleFlowFunction_5);
          }
        }
        if (guardCheck_mapDouble2ToDoubleFlowFunction_6()) {
          isDirty_mapDouble2ToDoubleFlowFunction_6 = mapDouble2ToDoubleFlowFunction_6.map();
          if (isDirty_mapDouble2ToDoubleFlowFunction_6) {
            binaryMapToDoubleFlowFunction_9.input2Updated(mapDouble2ToDoubleFlowFunction_6);
            binaryMapToDoubleFlowFunction_17.input2Updated(mapDouble2ToDoubleFlowFunction_6);
            doublePeekFlowFunction_8.inputUpdated(mapDouble2ToDoubleFlowFunction_6);
          }
        }
        if (guardCheck_binaryMapToDoubleFlowFunction_9()) {
          isDirty_binaryMapToDoubleFlowFunction_9 = binaryMapToDoubleFlowFunction_9.map();
          if (isDirty_binaryMapToDoubleFlowFunction_9) {
            mapDouble2ToDoubleFlowFunction_10.inputUpdated(binaryMapToDoubleFlowFunction_9);
          }
        }
        if (guardCheck_binaryMapToDoubleFlowFunction_17()) {
          isDirty_binaryMapToDoubleFlowFunction_17 = binaryMapToDoubleFlowFunction_17.map();
          if (isDirty_binaryMapToDoubleFlowFunction_17) {
            mapDouble2ToDoubleFlowFunction_18.inputUpdated(binaryMapToDoubleFlowFunction_17);
          }
        }
        if (guardCheck_mapDouble2ToDoubleFlowFunction_10()) {
          isDirty_mapDouble2ToDoubleFlowFunction_10 = mapDouble2ToDoubleFlowFunction_10.map();
          if (isDirty_mapDouble2ToDoubleFlowFunction_10) {
            binaryMapToDoubleFlowFunction_13.inputUpdated(mapDouble2ToDoubleFlowFunction_10);
            doublePeekFlowFunction_12.inputUpdated(mapDouble2ToDoubleFlowFunction_10);
          }
        }
        if (guardCheck_binaryMapToDoubleFlowFunction_13()) {
          isDirty_binaryMapToDoubleFlowFunction_13 = binaryMapToDoubleFlowFunction_13.map();
          if (isDirty_binaryMapToDoubleFlowFunction_13) {
            doublePeekFlowFunction_15.inputUpdated(binaryMapToDoubleFlowFunction_13);
          }
        }
        if (guardCheck_mapDouble2ToDoubleFlowFunction_18()) {
          isDirty_mapDouble2ToDoubleFlowFunction_18 = mapDouble2ToDoubleFlowFunction_18.map();
          if (isDirty_mapDouble2ToDoubleFlowFunction_18) {
            doublePeekFlowFunction_20.inputUpdated(mapDouble2ToDoubleFlowFunction_18);
          }
        }
        if (guardCheck_doublePeekFlowFunction_8()) {
          doublePeekFlowFunction_8.peek();
        }
        if (guardCheck_doublePeekFlowFunction_12()) {
          doublePeekFlowFunction_12.peek();
        }
        if (guardCheck_doublePeekFlowFunction_15()) {
          doublePeekFlowFunction_15.peek();
        }
        if (guardCheck_doublePeekFlowFunction_20()) {
          doublePeekFlowFunction_20.peek();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.runtime.event.Signal.DoubleSignal] filterString:[futuresPrice]
      case ("futuresPrice"):
        isDirty_handlerDoubleSignal_futuresPrice =
            handlerDoubleSignal_futuresPrice.onEvent(typedEvent);
        if (isDirty_handlerDoubleSignal_futuresPrice) {
          mapRef2ToDoubleFlowFunction_16.inputUpdated(handlerDoubleSignal_futuresPrice);
        }
        if (guardCheck_mapRef2ToDoubleFlowFunction_16()) {
          isDirty_mapRef2ToDoubleFlowFunction_16 = mapRef2ToDoubleFlowFunction_16.map();
          if (isDirty_mapRef2ToDoubleFlowFunction_16) {
            binaryMapToDoubleFlowFunction_17.inputUpdated(mapRef2ToDoubleFlowFunction_16);
          }
        }
        if (guardCheck_binaryMapToDoubleFlowFunction_17()) {
          isDirty_binaryMapToDoubleFlowFunction_17 = binaryMapToDoubleFlowFunction_17.map();
          if (isDirty_binaryMapToDoubleFlowFunction_17) {
            mapDouble2ToDoubleFlowFunction_18.inputUpdated(binaryMapToDoubleFlowFunction_17);
          }
        }
        if (guardCheck_mapDouble2ToDoubleFlowFunction_18()) {
          isDirty_mapDouble2ToDoubleFlowFunction_18 = mapDouble2ToDoubleFlowFunction_18.map();
          if (isDirty_mapDouble2ToDoubleFlowFunction_18) {
            doublePeekFlowFunction_20.inputUpdated(mapDouble2ToDoubleFlowFunction_18);
          }
        }
        if (guardCheck_doublePeekFlowFunction_20()) {
          doublePeekFlowFunction_20.peek();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.runtime.event.Signal.DoubleSignal] filterString:[interestRate]
      case ("interestRate"):
        isDirty_handlerDoubleSignal_interestRate =
            handlerDoubleSignal_interestRate.onEvent(typedEvent);
        if (isDirty_handlerDoubleSignal_interestRate) {
          mapRef2ToDoubleFlowFunction_2.inputUpdated(handlerDoubleSignal_interestRate);
        }
        if (guardCheck_mapRef2ToDoubleFlowFunction_2()) {
          isDirty_mapRef2ToDoubleFlowFunction_2 = mapRef2ToDoubleFlowFunction_2.map();
        }
        if (guardCheck_fairValueAbsolute_21()) {
          isDirty_fairValueAbsolute_21 = fairValueAbsolute_21.calculate();
        }
        if (guardCheck_nodeToFlowFunction_4()) {
          isDirty_nodeToFlowFunction_4 = true;
          nodeToFlowFunction_4.sourceUpdated();
          if (isDirty_nodeToFlowFunction_4) {
            mapRef2ToDoubleFlowFunction_5.inputUpdated(nodeToFlowFunction_4);
          }
        }
        if (guardCheck_mapRef2ToDoubleFlowFunction_5()) {
          isDirty_mapRef2ToDoubleFlowFunction_5 = mapRef2ToDoubleFlowFunction_5.map();
          if (isDirty_mapRef2ToDoubleFlowFunction_5) {
            mapDouble2ToDoubleFlowFunction_6.inputUpdated(mapRef2ToDoubleFlowFunction_5);
          }
        }
        if (guardCheck_mapDouble2ToDoubleFlowFunction_6()) {
          isDirty_mapDouble2ToDoubleFlowFunction_6 = mapDouble2ToDoubleFlowFunction_6.map();
          if (isDirty_mapDouble2ToDoubleFlowFunction_6) {
            binaryMapToDoubleFlowFunction_9.input2Updated(mapDouble2ToDoubleFlowFunction_6);
            binaryMapToDoubleFlowFunction_17.input2Updated(mapDouble2ToDoubleFlowFunction_6);
            doublePeekFlowFunction_8.inputUpdated(mapDouble2ToDoubleFlowFunction_6);
          }
        }
        if (guardCheck_binaryMapToDoubleFlowFunction_9()) {
          isDirty_binaryMapToDoubleFlowFunction_9 = binaryMapToDoubleFlowFunction_9.map();
          if (isDirty_binaryMapToDoubleFlowFunction_9) {
            mapDouble2ToDoubleFlowFunction_10.inputUpdated(binaryMapToDoubleFlowFunction_9);
          }
        }
        if (guardCheck_binaryMapToDoubleFlowFunction_17()) {
          isDirty_binaryMapToDoubleFlowFunction_17 = binaryMapToDoubleFlowFunction_17.map();
          if (isDirty_binaryMapToDoubleFlowFunction_17) {
            mapDouble2ToDoubleFlowFunction_18.inputUpdated(binaryMapToDoubleFlowFunction_17);
          }
        }
        if (guardCheck_mapDouble2ToDoubleFlowFunction_10()) {
          isDirty_mapDouble2ToDoubleFlowFunction_10 = mapDouble2ToDoubleFlowFunction_10.map();
          if (isDirty_mapDouble2ToDoubleFlowFunction_10) {
            binaryMapToDoubleFlowFunction_13.inputUpdated(mapDouble2ToDoubleFlowFunction_10);
            doublePeekFlowFunction_12.inputUpdated(mapDouble2ToDoubleFlowFunction_10);
          }
        }
        if (guardCheck_binaryMapToDoubleFlowFunction_13()) {
          isDirty_binaryMapToDoubleFlowFunction_13 = binaryMapToDoubleFlowFunction_13.map();
          if (isDirty_binaryMapToDoubleFlowFunction_13) {
            doublePeekFlowFunction_15.inputUpdated(binaryMapToDoubleFlowFunction_13);
          }
        }
        if (guardCheck_mapDouble2ToDoubleFlowFunction_18()) {
          isDirty_mapDouble2ToDoubleFlowFunction_18 = mapDouble2ToDoubleFlowFunction_18.map();
          if (isDirty_mapDouble2ToDoubleFlowFunction_18) {
            doublePeekFlowFunction_20.inputUpdated(mapDouble2ToDoubleFlowFunction_18);
          }
        }
        if (guardCheck_doublePeekFlowFunction_8()) {
          doublePeekFlowFunction_8.peek();
        }
        if (guardCheck_doublePeekFlowFunction_12()) {
          doublePeekFlowFunction_12.peek();
        }
        if (guardCheck_doublePeekFlowFunction_15()) {
          doublePeekFlowFunction_15.peek();
        }
        if (guardCheck_doublePeekFlowFunction_20()) {
          doublePeekFlowFunction_20.peek();
        }
        afterEvent();
        return;
        //Event Class:[com.fluxtion.runtime.event.Signal.DoubleSignal] filterString:[spot]
      case ("spot"):
        isDirty_handlerDoubleSignal_spot = handlerDoubleSignal_spot.onEvent(typedEvent);
        if (isDirty_handlerDoubleSignal_spot) {
          mapRef2ToDoubleFlowFunction_0.inputUpdated(handlerDoubleSignal_spot);
        }
        if (guardCheck_mapRef2ToDoubleFlowFunction_0()) {
          isDirty_mapRef2ToDoubleFlowFunction_0 = mapRef2ToDoubleFlowFunction_0.map();
          if (isDirty_mapRef2ToDoubleFlowFunction_0) {
            binaryMapToDoubleFlowFunction_9.inputUpdated(mapRef2ToDoubleFlowFunction_0);
            binaryMapToDoubleFlowFunction_13.input2Updated(mapRef2ToDoubleFlowFunction_0);
          }
        }
        if (guardCheck_fairValueAbsolute_21()) {
          isDirty_fairValueAbsolute_21 = fairValueAbsolute_21.calculate();
        }
        if (guardCheck_nodeToFlowFunction_4()) {
          isDirty_nodeToFlowFunction_4 = true;
          nodeToFlowFunction_4.sourceUpdated();
          if (isDirty_nodeToFlowFunction_4) {
            mapRef2ToDoubleFlowFunction_5.inputUpdated(nodeToFlowFunction_4);
          }
        }
        if (guardCheck_mapRef2ToDoubleFlowFunction_5()) {
          isDirty_mapRef2ToDoubleFlowFunction_5 = mapRef2ToDoubleFlowFunction_5.map();
          if (isDirty_mapRef2ToDoubleFlowFunction_5) {
            mapDouble2ToDoubleFlowFunction_6.inputUpdated(mapRef2ToDoubleFlowFunction_5);
          }
        }
        if (guardCheck_mapDouble2ToDoubleFlowFunction_6()) {
          isDirty_mapDouble2ToDoubleFlowFunction_6 = mapDouble2ToDoubleFlowFunction_6.map();
          if (isDirty_mapDouble2ToDoubleFlowFunction_6) {
            binaryMapToDoubleFlowFunction_9.input2Updated(mapDouble2ToDoubleFlowFunction_6);
            binaryMapToDoubleFlowFunction_17.input2Updated(mapDouble2ToDoubleFlowFunction_6);
            doublePeekFlowFunction_8.inputUpdated(mapDouble2ToDoubleFlowFunction_6);
          }
        }
        if (guardCheck_binaryMapToDoubleFlowFunction_9()) {
          isDirty_binaryMapToDoubleFlowFunction_9 = binaryMapToDoubleFlowFunction_9.map();
          if (isDirty_binaryMapToDoubleFlowFunction_9) {
            mapDouble2ToDoubleFlowFunction_10.inputUpdated(binaryMapToDoubleFlowFunction_9);
          }
        }
        if (guardCheck_binaryMapToDoubleFlowFunction_17()) {
          isDirty_binaryMapToDoubleFlowFunction_17 = binaryMapToDoubleFlowFunction_17.map();
          if (isDirty_binaryMapToDoubleFlowFunction_17) {
            mapDouble2ToDoubleFlowFunction_18.inputUpdated(binaryMapToDoubleFlowFunction_17);
          }
        }
        if (guardCheck_mapDouble2ToDoubleFlowFunction_10()) {
          isDirty_mapDouble2ToDoubleFlowFunction_10 = mapDouble2ToDoubleFlowFunction_10.map();
          if (isDirty_mapDouble2ToDoubleFlowFunction_10) {
            binaryMapToDoubleFlowFunction_13.inputUpdated(mapDouble2ToDoubleFlowFunction_10);
            doublePeekFlowFunction_12.inputUpdated(mapDouble2ToDoubleFlowFunction_10);
          }
        }
        if (guardCheck_binaryMapToDoubleFlowFunction_13()) {
          isDirty_binaryMapToDoubleFlowFunction_13 = binaryMapToDoubleFlowFunction_13.map();
          if (isDirty_binaryMapToDoubleFlowFunction_13) {
            doublePeekFlowFunction_15.inputUpdated(binaryMapToDoubleFlowFunction_13);
          }
        }
        if (guardCheck_mapDouble2ToDoubleFlowFunction_18()) {
          isDirty_mapDouble2ToDoubleFlowFunction_18 = mapDouble2ToDoubleFlowFunction_18.map();
          if (isDirty_mapDouble2ToDoubleFlowFunction_18) {
            doublePeekFlowFunction_20.inputUpdated(mapDouble2ToDoubleFlowFunction_18);
          }
        }
        if (guardCheck_doublePeekFlowFunction_8()) {
          doublePeekFlowFunction_8.peek();
        }
        if (guardCheck_doublePeekFlowFunction_12()) {
          doublePeekFlowFunction_12.peek();
        }
        if (guardCheck_doublePeekFlowFunction_15()) {
          doublePeekFlowFunction_15.peek();
        }
        if (guardCheck_doublePeekFlowFunction_20()) {
          doublePeekFlowFunction_20.peek();
        }
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

  public void bufferEvent(Object event) {
    buffering = true;
    if (event instanceof com.fluxtion.runtime.event.Signal.DoubleSignal) {
      DoubleSignal typedEvent = (DoubleSignal) event;
      auditEvent(typedEvent);
      switch (typedEvent.filterString()) {
          //Event Class:[com.fluxtion.runtime.event.Signal.DoubleSignal] filterString:[daysToExpiry]
        case ("daysToExpiry"):
          isDirty_handlerDoubleSignal_daysToExpiry =
              handlerDoubleSignal_daysToExpiry.onEvent(typedEvent);
          if (isDirty_handlerDoubleSignal_daysToExpiry) {
            mapRef2ToDoubleFlowFunction_3.inputUpdated(handlerDoubleSignal_daysToExpiry);
          }
          afterEvent();
          return;
          //Event Class:[com.fluxtion.runtime.event.Signal.DoubleSignal] filterString:[dividends]
        case ("dividends"):
          isDirty_handlerDoubleSignal_dividends = handlerDoubleSignal_dividends.onEvent(typedEvent);
          if (isDirty_handlerDoubleSignal_dividends) {
            mapRef2ToDoubleFlowFunction_1.inputUpdated(handlerDoubleSignal_dividends);
          }
          afterEvent();
          return;
          //Event Class:[com.fluxtion.runtime.event.Signal.DoubleSignal] filterString:[futuresPrice]
        case ("futuresPrice"):
          isDirty_handlerDoubleSignal_futuresPrice =
              handlerDoubleSignal_futuresPrice.onEvent(typedEvent);
          if (isDirty_handlerDoubleSignal_futuresPrice) {
            mapRef2ToDoubleFlowFunction_16.inputUpdated(handlerDoubleSignal_futuresPrice);
          }
          afterEvent();
          return;
          //Event Class:[com.fluxtion.runtime.event.Signal.DoubleSignal] filterString:[interestRate]
        case ("interestRate"):
          isDirty_handlerDoubleSignal_interestRate =
              handlerDoubleSignal_interestRate.onEvent(typedEvent);
          if (isDirty_handlerDoubleSignal_interestRate) {
            mapRef2ToDoubleFlowFunction_2.inputUpdated(handlerDoubleSignal_interestRate);
          }
          afterEvent();
          return;
          //Event Class:[com.fluxtion.runtime.event.Signal.DoubleSignal] filterString:[spot]
        case ("spot"):
          isDirty_handlerDoubleSignal_spot = handlerDoubleSignal_spot.onEvent(typedEvent);
          if (isDirty_handlerDoubleSignal_spot) {
            mapRef2ToDoubleFlowFunction_0.inputUpdated(handlerDoubleSignal_spot);
          }
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

  public void triggerCalculation() {
    buffering = false;
    String typedEvent = "No event information - buffered dispatch";
    if (guardCheck_mapRef2ToDoubleFlowFunction_0()) {
      isDirty_mapRef2ToDoubleFlowFunction_0 = mapRef2ToDoubleFlowFunction_0.map();
      if (isDirty_mapRef2ToDoubleFlowFunction_0) {
        binaryMapToDoubleFlowFunction_9.inputUpdated(mapRef2ToDoubleFlowFunction_0);
        binaryMapToDoubleFlowFunction_13.input2Updated(mapRef2ToDoubleFlowFunction_0);
      }
    }
    if (guardCheck_mapRef2ToDoubleFlowFunction_1()) {
      isDirty_mapRef2ToDoubleFlowFunction_1 = mapRef2ToDoubleFlowFunction_1.map();
    }
    if (guardCheck_mapRef2ToDoubleFlowFunction_2()) {
      isDirty_mapRef2ToDoubleFlowFunction_2 = mapRef2ToDoubleFlowFunction_2.map();
    }
    if (guardCheck_mapRef2ToDoubleFlowFunction_3()) {
      isDirty_mapRef2ToDoubleFlowFunction_3 = mapRef2ToDoubleFlowFunction_3.map();
    }
    if (guardCheck_fairValueAbsolute_21()) {
      isDirty_fairValueAbsolute_21 = fairValueAbsolute_21.calculate();
    }
    if (guardCheck_mapRef2ToDoubleFlowFunction_16()) {
      isDirty_mapRef2ToDoubleFlowFunction_16 = mapRef2ToDoubleFlowFunction_16.map();
      if (isDirty_mapRef2ToDoubleFlowFunction_16) {
        binaryMapToDoubleFlowFunction_17.inputUpdated(mapRef2ToDoubleFlowFunction_16);
      }
    }
    if (guardCheck_nodeToFlowFunction_4()) {
      isDirty_nodeToFlowFunction_4 = true;
      nodeToFlowFunction_4.sourceUpdated();
      if (isDirty_nodeToFlowFunction_4) {
        mapRef2ToDoubleFlowFunction_5.inputUpdated(nodeToFlowFunction_4);
      }
    }
    if (guardCheck_mapRef2ToDoubleFlowFunction_5()) {
      isDirty_mapRef2ToDoubleFlowFunction_5 = mapRef2ToDoubleFlowFunction_5.map();
      if (isDirty_mapRef2ToDoubleFlowFunction_5) {
        mapDouble2ToDoubleFlowFunction_6.inputUpdated(mapRef2ToDoubleFlowFunction_5);
      }
    }
    if (guardCheck_mapDouble2ToDoubleFlowFunction_6()) {
      isDirty_mapDouble2ToDoubleFlowFunction_6 = mapDouble2ToDoubleFlowFunction_6.map();
      if (isDirty_mapDouble2ToDoubleFlowFunction_6) {
        binaryMapToDoubleFlowFunction_9.input2Updated(mapDouble2ToDoubleFlowFunction_6);
        binaryMapToDoubleFlowFunction_17.input2Updated(mapDouble2ToDoubleFlowFunction_6);
        doublePeekFlowFunction_8.inputUpdated(mapDouble2ToDoubleFlowFunction_6);
      }
    }
    if (guardCheck_binaryMapToDoubleFlowFunction_9()) {
      isDirty_binaryMapToDoubleFlowFunction_9 = binaryMapToDoubleFlowFunction_9.map();
      if (isDirty_binaryMapToDoubleFlowFunction_9) {
        mapDouble2ToDoubleFlowFunction_10.inputUpdated(binaryMapToDoubleFlowFunction_9);
      }
    }
    if (guardCheck_binaryMapToDoubleFlowFunction_17()) {
      isDirty_binaryMapToDoubleFlowFunction_17 = binaryMapToDoubleFlowFunction_17.map();
      if (isDirty_binaryMapToDoubleFlowFunction_17) {
        mapDouble2ToDoubleFlowFunction_18.inputUpdated(binaryMapToDoubleFlowFunction_17);
      }
    }
    if (guardCheck_mapDouble2ToDoubleFlowFunction_10()) {
      isDirty_mapDouble2ToDoubleFlowFunction_10 = mapDouble2ToDoubleFlowFunction_10.map();
      if (isDirty_mapDouble2ToDoubleFlowFunction_10) {
        binaryMapToDoubleFlowFunction_13.inputUpdated(mapDouble2ToDoubleFlowFunction_10);
        doublePeekFlowFunction_12.inputUpdated(mapDouble2ToDoubleFlowFunction_10);
      }
    }
    if (guardCheck_binaryMapToDoubleFlowFunction_13()) {
      isDirty_binaryMapToDoubleFlowFunction_13 = binaryMapToDoubleFlowFunction_13.map();
      if (isDirty_binaryMapToDoubleFlowFunction_13) {
        doublePeekFlowFunction_15.inputUpdated(binaryMapToDoubleFlowFunction_13);
      }
    }
    if (guardCheck_mapDouble2ToDoubleFlowFunction_18()) {
      isDirty_mapDouble2ToDoubleFlowFunction_18 = mapDouble2ToDoubleFlowFunction_18.map();
      if (isDirty_mapDouble2ToDoubleFlowFunction_18) {
        doublePeekFlowFunction_20.inputUpdated(mapDouble2ToDoubleFlowFunction_18);
      }
    }
    if (guardCheck_doublePeekFlowFunction_8()) {
      doublePeekFlowFunction_8.peek();
    }
    if (guardCheck_doublePeekFlowFunction_12()) {
      doublePeekFlowFunction_12.peek();
    }
    if (guardCheck_doublePeekFlowFunction_15()) {
      doublePeekFlowFunction_15.peek();
    }
    if (guardCheck_doublePeekFlowFunction_20()) {
      doublePeekFlowFunction_20.peek();
    }
    afterEvent();
  }

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
    auditor.nodeRegistered(fairValueAbsolute_21, "fairValueAbsolute_21");
    auditor.nodeRegistered(callbackDispatcher, "callbackDispatcher");
    auditor.nodeRegistered(binaryMapToDoubleFlowFunction_9, "binaryMapToDoubleFlowFunction_9");
    auditor.nodeRegistered(binaryMapToDoubleFlowFunction_13, "binaryMapToDoubleFlowFunction_13");
    auditor.nodeRegistered(binaryMapToDoubleFlowFunction_17, "binaryMapToDoubleFlowFunction_17");
    auditor.nodeRegistered(mapDouble2ToDoubleFlowFunction_6, "mapDouble2ToDoubleFlowFunction_6");
    auditor.nodeRegistered(mapDouble2ToDoubleFlowFunction_10, "mapDouble2ToDoubleFlowFunction_10");
    auditor.nodeRegistered(mapDouble2ToDoubleFlowFunction_18, "mapDouble2ToDoubleFlowFunction_18");
    auditor.nodeRegistered(mapRef2ToDoubleFlowFunction_0, "mapRef2ToDoubleFlowFunction_0");
    auditor.nodeRegistered(mapRef2ToDoubleFlowFunction_1, "mapRef2ToDoubleFlowFunction_1");
    auditor.nodeRegistered(mapRef2ToDoubleFlowFunction_2, "mapRef2ToDoubleFlowFunction_2");
    auditor.nodeRegistered(mapRef2ToDoubleFlowFunction_3, "mapRef2ToDoubleFlowFunction_3");
    auditor.nodeRegistered(mapRef2ToDoubleFlowFunction_5, "mapRef2ToDoubleFlowFunction_5");
    auditor.nodeRegistered(mapRef2ToDoubleFlowFunction_16, "mapRef2ToDoubleFlowFunction_16");
    auditor.nodeRegistered(nodeToFlowFunction_4, "nodeToFlowFunction_4");
    auditor.nodeRegistered(doublePeekFlowFunction_8, "doublePeekFlowFunction_8");
    auditor.nodeRegistered(doublePeekFlowFunction_12, "doublePeekFlowFunction_12");
    auditor.nodeRegistered(doublePeekFlowFunction_15, "doublePeekFlowFunction_15");
    auditor.nodeRegistered(doublePeekFlowFunction_20, "doublePeekFlowFunction_20");
    auditor.nodeRegistered(templateMessage_7, "templateMessage_7");
    auditor.nodeRegistered(templateMessage_11, "templateMessage_11");
    auditor.nodeRegistered(templateMessage_14, "templateMessage_14");
    auditor.nodeRegistered(templateMessage_19, "templateMessage_19");
    auditor.nodeRegistered(subscriptionManager, "subscriptionManager");
    auditor.nodeRegistered(handlerDoubleSignal_daysToExpiry, "handlerDoubleSignal_daysToExpiry");
    auditor.nodeRegistered(handlerDoubleSignal_dividends, "handlerDoubleSignal_dividends");
    auditor.nodeRegistered(handlerDoubleSignal_futuresPrice, "handlerDoubleSignal_futuresPrice");
    auditor.nodeRegistered(handlerDoubleSignal_interestRate, "handlerDoubleSignal_interestRate");
    auditor.nodeRegistered(handlerDoubleSignal_spot, "handlerDoubleSignal_spot");
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
    isDirty_binaryMapToDoubleFlowFunction_9 = false;
    isDirty_binaryMapToDoubleFlowFunction_13 = false;
    isDirty_binaryMapToDoubleFlowFunction_17 = false;
    isDirty_clock = false;
    isDirty_fairValueAbsolute_21 = false;
    isDirty_handlerDoubleSignal_daysToExpiry = false;
    isDirty_handlerDoubleSignal_dividends = false;
    isDirty_handlerDoubleSignal_futuresPrice = false;
    isDirty_handlerDoubleSignal_interestRate = false;
    isDirty_handlerDoubleSignal_spot = false;
    isDirty_mapDouble2ToDoubleFlowFunction_6 = false;
    isDirty_mapDouble2ToDoubleFlowFunction_10 = false;
    isDirty_mapDouble2ToDoubleFlowFunction_18 = false;
    isDirty_mapRef2ToDoubleFlowFunction_0 = false;
    isDirty_mapRef2ToDoubleFlowFunction_1 = false;
    isDirty_mapRef2ToDoubleFlowFunction_2 = false;
    isDirty_mapRef2ToDoubleFlowFunction_3 = false;
    isDirty_mapRef2ToDoubleFlowFunction_5 = false;
    isDirty_mapRef2ToDoubleFlowFunction_16 = false;
    isDirty_nodeToFlowFunction_4 = false;
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
          binaryMapToDoubleFlowFunction_13, () -> isDirty_binaryMapToDoubleFlowFunction_13);
      dirtyFlagSupplierMap.put(
          binaryMapToDoubleFlowFunction_17, () -> isDirty_binaryMapToDoubleFlowFunction_17);
      dirtyFlagSupplierMap.put(
          binaryMapToDoubleFlowFunction_9, () -> isDirty_binaryMapToDoubleFlowFunction_9);
      dirtyFlagSupplierMap.put(clock, () -> isDirty_clock);
      dirtyFlagSupplierMap.put(fairValueAbsolute_21, () -> isDirty_fairValueAbsolute_21);
      dirtyFlagSupplierMap.put(
          handlerDoubleSignal_daysToExpiry, () -> isDirty_handlerDoubleSignal_daysToExpiry);
      dirtyFlagSupplierMap.put(
          handlerDoubleSignal_dividends, () -> isDirty_handlerDoubleSignal_dividends);
      dirtyFlagSupplierMap.put(
          handlerDoubleSignal_futuresPrice, () -> isDirty_handlerDoubleSignal_futuresPrice);
      dirtyFlagSupplierMap.put(
          handlerDoubleSignal_interestRate, () -> isDirty_handlerDoubleSignal_interestRate);
      dirtyFlagSupplierMap.put(handlerDoubleSignal_spot, () -> isDirty_handlerDoubleSignal_spot);
      dirtyFlagSupplierMap.put(
          mapDouble2ToDoubleFlowFunction_10, () -> isDirty_mapDouble2ToDoubleFlowFunction_10);
      dirtyFlagSupplierMap.put(
          mapDouble2ToDoubleFlowFunction_18, () -> isDirty_mapDouble2ToDoubleFlowFunction_18);
      dirtyFlagSupplierMap.put(
          mapDouble2ToDoubleFlowFunction_6, () -> isDirty_mapDouble2ToDoubleFlowFunction_6);
      dirtyFlagSupplierMap.put(
          mapRef2ToDoubleFlowFunction_0, () -> isDirty_mapRef2ToDoubleFlowFunction_0);
      dirtyFlagSupplierMap.put(
          mapRef2ToDoubleFlowFunction_1, () -> isDirty_mapRef2ToDoubleFlowFunction_1);
      dirtyFlagSupplierMap.put(
          mapRef2ToDoubleFlowFunction_16, () -> isDirty_mapRef2ToDoubleFlowFunction_16);
      dirtyFlagSupplierMap.put(
          mapRef2ToDoubleFlowFunction_2, () -> isDirty_mapRef2ToDoubleFlowFunction_2);
      dirtyFlagSupplierMap.put(
          mapRef2ToDoubleFlowFunction_3, () -> isDirty_mapRef2ToDoubleFlowFunction_3);
      dirtyFlagSupplierMap.put(
          mapRef2ToDoubleFlowFunction_5, () -> isDirty_mapRef2ToDoubleFlowFunction_5);
      dirtyFlagSupplierMap.put(nodeToFlowFunction_4, () -> isDirty_nodeToFlowFunction_4);
    }
    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(
          binaryMapToDoubleFlowFunction_13, (b) -> isDirty_binaryMapToDoubleFlowFunction_13 = b);
      dirtyFlagUpdateMap.put(
          binaryMapToDoubleFlowFunction_17, (b) -> isDirty_binaryMapToDoubleFlowFunction_17 = b);
      dirtyFlagUpdateMap.put(
          binaryMapToDoubleFlowFunction_9, (b) -> isDirty_binaryMapToDoubleFlowFunction_9 = b);
      dirtyFlagUpdateMap.put(clock, (b) -> isDirty_clock = b);
      dirtyFlagUpdateMap.put(fairValueAbsolute_21, (b) -> isDirty_fairValueAbsolute_21 = b);
      dirtyFlagUpdateMap.put(
          handlerDoubleSignal_daysToExpiry, (b) -> isDirty_handlerDoubleSignal_daysToExpiry = b);
      dirtyFlagUpdateMap.put(
          handlerDoubleSignal_dividends, (b) -> isDirty_handlerDoubleSignal_dividends = b);
      dirtyFlagUpdateMap.put(
          handlerDoubleSignal_futuresPrice, (b) -> isDirty_handlerDoubleSignal_futuresPrice = b);
      dirtyFlagUpdateMap.put(
          handlerDoubleSignal_interestRate, (b) -> isDirty_handlerDoubleSignal_interestRate = b);
      dirtyFlagUpdateMap.put(handlerDoubleSignal_spot, (b) -> isDirty_handlerDoubleSignal_spot = b);
      dirtyFlagUpdateMap.put(
          mapDouble2ToDoubleFlowFunction_10, (b) -> isDirty_mapDouble2ToDoubleFlowFunction_10 = b);
      dirtyFlagUpdateMap.put(
          mapDouble2ToDoubleFlowFunction_18, (b) -> isDirty_mapDouble2ToDoubleFlowFunction_18 = b);
      dirtyFlagUpdateMap.put(
          mapDouble2ToDoubleFlowFunction_6, (b) -> isDirty_mapDouble2ToDoubleFlowFunction_6 = b);
      dirtyFlagUpdateMap.put(
          mapRef2ToDoubleFlowFunction_0, (b) -> isDirty_mapRef2ToDoubleFlowFunction_0 = b);
      dirtyFlagUpdateMap.put(
          mapRef2ToDoubleFlowFunction_1, (b) -> isDirty_mapRef2ToDoubleFlowFunction_1 = b);
      dirtyFlagUpdateMap.put(
          mapRef2ToDoubleFlowFunction_16, (b) -> isDirty_mapRef2ToDoubleFlowFunction_16 = b);
      dirtyFlagUpdateMap.put(
          mapRef2ToDoubleFlowFunction_2, (b) -> isDirty_mapRef2ToDoubleFlowFunction_2 = b);
      dirtyFlagUpdateMap.put(
          mapRef2ToDoubleFlowFunction_3, (b) -> isDirty_mapRef2ToDoubleFlowFunction_3 = b);
      dirtyFlagUpdateMap.put(
          mapRef2ToDoubleFlowFunction_5, (b) -> isDirty_mapRef2ToDoubleFlowFunction_5 = b);
      dirtyFlagUpdateMap.put(nodeToFlowFunction_4, (b) -> isDirty_nodeToFlowFunction_4 = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_fairValueAbsolute_21() {
    return isDirty_mapRef2ToDoubleFlowFunction_0
        | isDirty_mapRef2ToDoubleFlowFunction_1
        | isDirty_mapRef2ToDoubleFlowFunction_2
        | isDirty_mapRef2ToDoubleFlowFunction_3;
  }

  private boolean guardCheck_binaryMapToDoubleFlowFunction_9() {
    return isDirty_mapDouble2ToDoubleFlowFunction_6 | isDirty_mapRef2ToDoubleFlowFunction_0;
  }

  private boolean guardCheck_binaryMapToDoubleFlowFunction_13() {
    return isDirty_mapDouble2ToDoubleFlowFunction_10 | isDirty_mapRef2ToDoubleFlowFunction_0;
  }

  private boolean guardCheck_binaryMapToDoubleFlowFunction_17() {
    return isDirty_mapDouble2ToDoubleFlowFunction_6 | isDirty_mapRef2ToDoubleFlowFunction_16;
  }

  private boolean guardCheck_mapDouble2ToDoubleFlowFunction_6() {
    return isDirty_mapRef2ToDoubleFlowFunction_5;
  }

  private boolean guardCheck_mapDouble2ToDoubleFlowFunction_10() {
    return isDirty_binaryMapToDoubleFlowFunction_9;
  }

  private boolean guardCheck_mapDouble2ToDoubleFlowFunction_18() {
    return isDirty_binaryMapToDoubleFlowFunction_17;
  }

  private boolean guardCheck_mapRef2ToDoubleFlowFunction_0() {
    return isDirty_handlerDoubleSignal_spot;
  }

  private boolean guardCheck_mapRef2ToDoubleFlowFunction_1() {
    return isDirty_handlerDoubleSignal_dividends;
  }

  private boolean guardCheck_mapRef2ToDoubleFlowFunction_2() {
    return isDirty_handlerDoubleSignal_interestRate;
  }

  private boolean guardCheck_mapRef2ToDoubleFlowFunction_3() {
    return isDirty_handlerDoubleSignal_daysToExpiry;
  }

  private boolean guardCheck_mapRef2ToDoubleFlowFunction_5() {
    return isDirty_nodeToFlowFunction_4;
  }

  private boolean guardCheck_mapRef2ToDoubleFlowFunction_16() {
    return isDirty_handlerDoubleSignal_futuresPrice;
  }

  private boolean guardCheck_nodeToFlowFunction_4() {
    return isDirty_fairValueAbsolute_21;
  }

  private boolean guardCheck_doublePeekFlowFunction_8() {
    return isDirty_mapDouble2ToDoubleFlowFunction_6;
  }

  private boolean guardCheck_doublePeekFlowFunction_12() {
    return isDirty_mapDouble2ToDoubleFlowFunction_10;
  }

  private boolean guardCheck_doublePeekFlowFunction_15() {
    return isDirty_binaryMapToDoubleFlowFunction_13;
  }

  private boolean guardCheck_doublePeekFlowFunction_20() {
    return isDirty_mapDouble2ToDoubleFlowFunction_18;
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
  public FVGraphPricer newInstance() {
    return new FVGraphPricer();
  }

  @Override
  public FVGraphPricer newInstance(Map<Object, Object> contextMap) {
    return new FVGraphPricer();
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
