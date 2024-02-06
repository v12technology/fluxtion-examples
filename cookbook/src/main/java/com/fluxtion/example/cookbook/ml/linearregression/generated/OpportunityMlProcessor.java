/*
* Copyright (C) 2024 gregory higgins
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
package com.fluxtion.example.cookbook.ml.linearregression.generated;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.example.cookbook.ml.linearregression.AreaFeature;
import com.fluxtion.example.cookbook.ml.linearregression.HouseDetails;
import com.fluxtion.example.cookbook.ml.linearregression.HouseFilters;
import com.fluxtion.example.cookbook.ml.linearregression.OpportunityNotifier;
import com.fluxtion.example.cookbook.ml.linearregression.OpportunityNotifierNode;
import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.audit.EventLogManager;
import com.fluxtion.runtime.audit.NodeNameAuditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.callback.ExportFunctionAuditEvent;
import com.fluxtion.runtime.dataflow.function.FilterFlowFunction;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.input.EventFeed;
import com.fluxtion.runtime.input.SubscriptionManagerNode;
import com.fluxtion.runtime.ml.CalibrationProcessor;
import com.fluxtion.runtime.ml.Feature;
import com.fluxtion.runtime.ml.PredictiveLinearRegressionModel;
import com.fluxtion.runtime.node.DefaultEventHandlerNode;
import com.fluxtion.runtime.node.ForkedTriggerTask;
import com.fluxtion.runtime.node.MutableEventProcessorContext;
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
 * eventProcessorGenerator version : 9.2.1-SNAPSHOT
 * api version                     : 9.2.1-SNAPSHOT
 * </pre>
 *
 * Event classes supported:
 *
 * <ul>
 *   <li>com.fluxtion.compiler.generation.model.ExportFunctionMarker
 *   <li>com.fluxtion.example.cookbook.ml.linearregression.HouseDetails
 *   <li>com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public class OpportunityMlProcessor
    implements EventProcessor<OpportunityMlProcessor>,
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle,
        CalibrationProcessor,
        OpportunityNotifier {

  //Node declarations
  private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  public final NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final SubscriptionManagerNode subscriptionManager = new SubscriptionManagerNode();
  private final MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  private final DefaultEventHandlerNode handlerHouseDetails =
      new DefaultEventHandlerNode<>(
          2147483647,
          "",
          com.fluxtion.example.cookbook.ml.linearregression.HouseDetails.class,
          "handlerHouseDetails",
          context);
  private final FilterFlowFunction filterFlowFunction_1 =
      new FilterFlowFunction<>(handlerHouseDetails, HouseFilters::bedroomWithinRange);
  private final AreaFeature AreaFeature_0 = new AreaFeature(filterFlowFunction_1);
  private final PredictiveLinearRegressionModel predictiveLinearRegressionModel_2 =
      new PredictiveLinearRegressionModel(new Feature[] {AreaFeature_0});
  private final OpportunityNotifierNode opportunityNotifierNode_0 =
      new OpportunityNotifierNode(predictiveLinearRegressionModel_2);
  public final Clock clock = new Clock();
  private ExportFunctionAuditEvent functionAudit = new ExportFunctionAuditEvent();
  //Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(4);
  private final IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(4);

  private boolean isDirty_AreaFeature_0 = false;
  private boolean isDirty_filterFlowFunction_1 = false;
  private boolean isDirty_handlerHouseDetails = false;
  private boolean isDirty_predictiveLinearRegressionModel_2 = false;
  //Forked declarations

  //Filter constants

  public OpportunityMlProcessor(Map<Object, Object> contextMap) {
    context.replaceMappings(contextMap);
    filterFlowFunction_1.setEventProcessorContext(context);
    //node auditors
    initialiseAuditor(clock);
    initialiseAuditor(nodeNameLookup);
    subscriptionManager.setSubscribingEventProcessor(this);
    context.setEventProcessorCallback(this);
  }

  public OpportunityMlProcessor() {
    this(null);
  }

  @Override
  public void init() {
    initCalled = true;
    auditEvent(Lifecycle.LifecycleEvent.Init);
    //initialise dirty lookup map
    isDirty("test");
    handlerHouseDetails.init();
    filterFlowFunction_1.initialiseEventStream();
    AreaFeature_0.init();
    predictiveLinearRegressionModel_2.init();
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
    handlerHouseDetails.tearDown();
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
    if (event instanceof com.fluxtion.example.cookbook.ml.linearregression.HouseDetails) {
      HouseDetails typedEvent = (HouseDetails) event;
      handleEvent(typedEvent);
    } else if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      handleEvent(typedEvent);
    }
  }

  public void handleEvent(HouseDetails typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerHouseDetails = handlerHouseDetails.onEvent(typedEvent);
    if (isDirty_handlerHouseDetails) {
      filterFlowFunction_1.inputUpdated(handlerHouseDetails);
    }
    if (guardCheck_filterFlowFunction_1()) {
      isDirty_filterFlowFunction_1 = filterFlowFunction_1.filter();
    }
    if (guardCheck_AreaFeature_0()) {
      isDirty_AreaFeature_0 = AreaFeature_0.processRecord();
      if (isDirty_AreaFeature_0) {
        predictiveLinearRegressionModel_2.featureUpdated(AreaFeature_0);
      }
    }
    if (guardCheck_predictiveLinearRegressionModel_2()) {
      isDirty_predictiveLinearRegressionModel_2 =
          predictiveLinearRegressionModel_2.calculateInference();
    }
    if (guardCheck_opportunityNotifierNode_0()) {
      opportunityNotifierNode_0.predictionUpdated();
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

  //EXPORTED SERVICE FUNCTIONS - START
  @Override
  public boolean setCalibration(java.util.List<com.fluxtion.runtime.ml.Calibration> arg0) {
    beforeServiceCall(
        "public boolean com.fluxtion.runtime.ml.AbstractFeature.setCalibration(java.util.List<com.fluxtion.runtime.ml.Calibration>)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    isDirty_AreaFeature_0 = AreaFeature_0.setCalibration(arg0);
    if (isDirty_AreaFeature_0) {
      predictiveLinearRegressionModel_2.featureUpdated(AreaFeature_0);
    }
    isDirty_predictiveLinearRegressionModel_2 =
        predictiveLinearRegressionModel_2.setCalibration(arg0);
    afterServiceCall();
    return true;
  }

  @Override
  public void publishOff() {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.ml.linearregression.OpportunityNotifierNode.publishOff()");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    opportunityNotifierNode_0.publishOff();
    afterServiceCall();
  }

  @Override
  public void publishOn() {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.ml.linearregression.OpportunityNotifierNode.publishOn()");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    opportunityNotifierNode_0.publishOn();
    afterServiceCall();
  }
  //EXPORTED SERVICE FUNCTIONS - END

  public void bufferEvent(Object event) {
    buffering = true;
    if (event instanceof com.fluxtion.example.cookbook.ml.linearregression.HouseDetails) {
      HouseDetails typedEvent = (HouseDetails) event;
      auditEvent(typedEvent);
      isDirty_handlerHouseDetails = handlerHouseDetails.onEvent(typedEvent);
      if (isDirty_handlerHouseDetails) {
        filterFlowFunction_1.inputUpdated(handlerHouseDetails);
      }
    } else if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      auditEvent(typedEvent);
      clock.setClockStrategy(typedEvent);
    }
  }

  public void triggerCalculation() {
    buffering = false;
    String typedEvent = "No event information - buffered dispatch";
    if (guardCheck_filterFlowFunction_1()) {
      isDirty_filterFlowFunction_1 = filterFlowFunction_1.filter();
    }
    if (guardCheck_AreaFeature_0()) {
      isDirty_AreaFeature_0 = AreaFeature_0.processRecord();
      if (isDirty_AreaFeature_0) {
        predictiveLinearRegressionModel_2.featureUpdated(AreaFeature_0);
      }
    }
    if (guardCheck_predictiveLinearRegressionModel_2()) {
      isDirty_predictiveLinearRegressionModel_2 =
          predictiveLinearRegressionModel_2.calculateInference();
    }
    if (guardCheck_opportunityNotifierNode_0()) {
      opportunityNotifierNode_0.predictionUpdated();
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
    auditor.nodeRegistered(AreaFeature_0, "AreaFeature_0");
    auditor.nodeRegistered(opportunityNotifierNode_0, "opportunityNotifierNode_0");
    auditor.nodeRegistered(callbackDispatcher, "callbackDispatcher");
    auditor.nodeRegistered(filterFlowFunction_1, "filterFlowFunction_1");
    auditor.nodeRegistered(subscriptionManager, "subscriptionManager");
    auditor.nodeRegistered(predictiveLinearRegressionModel_2, "predictiveLinearRegressionModel_2");
    auditor.nodeRegistered(handlerHouseDetails, "handlerHouseDetails");
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
    isDirty_AreaFeature_0 = false;
    isDirty_filterFlowFunction_1 = false;
    isDirty_handlerHouseDetails = false;
    isDirty_predictiveLinearRegressionModel_2 = false;
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
      dirtyFlagSupplierMap.put(AreaFeature_0, () -> isDirty_AreaFeature_0);
      dirtyFlagSupplierMap.put(filterFlowFunction_1, () -> isDirty_filterFlowFunction_1);
      dirtyFlagSupplierMap.put(handlerHouseDetails, () -> isDirty_handlerHouseDetails);
      dirtyFlagSupplierMap.put(
          predictiveLinearRegressionModel_2, () -> isDirty_predictiveLinearRegressionModel_2);
    }
    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(AreaFeature_0, (b) -> isDirty_AreaFeature_0 = b);
      dirtyFlagUpdateMap.put(filterFlowFunction_1, (b) -> isDirty_filterFlowFunction_1 = b);
      dirtyFlagUpdateMap.put(handlerHouseDetails, (b) -> isDirty_handlerHouseDetails = b);
      dirtyFlagUpdateMap.put(
          predictiveLinearRegressionModel_2, (b) -> isDirty_predictiveLinearRegressionModel_2 = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_AreaFeature_0() {
    return isDirty_filterFlowFunction_1;
  }

  private boolean guardCheck_opportunityNotifierNode_0() {
    return isDirty_predictiveLinearRegressionModel_2;
  }

  private boolean guardCheck_filterFlowFunction_1() {
    return isDirty_handlerHouseDetails;
  }

  private boolean guardCheck_predictiveLinearRegressionModel_2() {
    return isDirty_AreaFeature_0;
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
  public OpportunityMlProcessor newInstance() {
    return new OpportunityMlProcessor();
  }

  @Override
  public OpportunityMlProcessor newInstance(Map<Object, Object> contextMap) {
    return new OpportunityMlProcessor();
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
