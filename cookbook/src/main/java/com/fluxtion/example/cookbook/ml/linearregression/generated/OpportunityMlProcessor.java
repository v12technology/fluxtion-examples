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
import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails;
import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSalesMonitor;
import com.fluxtion.example.cookbook.ml.linearregression.api.OpportunityNotifier;
import com.fluxtion.example.cookbook.ml.linearregression.node.LiveHouseSalesCache;
import com.fluxtion.example.cookbook.ml.linearregression.node.OpportunityNotifierNode;
import com.fluxtion.example.cookbook.ml.linearregression.node.ReCalibrationCompleteEvent;
import com.fluxtion.example.cookbook.ml.linearregression.pipeline.AreaFeature;
import com.fluxtion.example.cookbook.ml.linearregression.pipeline.HouseFilters;
import com.fluxtion.example.cookbook.ml.linearregression.pipeline.HouseTransformer;
import com.fluxtion.example.cookbook.ml.linearregression.pipeline.PreProcessPipeline;
import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.audit.EventLogManager;
import com.fluxtion.runtime.audit.NodeNameAuditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.callback.ExportFunctionAuditEvent;
import com.fluxtion.runtime.dataflow.function.FilterFlowFunction;
import com.fluxtion.runtime.dataflow.function.MapFlowFunction.MapRef2RefFlowFunction;
import com.fluxtion.runtime.dataflow.function.PeekFlowFunction;
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
 * eventProcessorGenerator version : 9.2.3
 * api version                     : 9.2.3
 * </pre>
 *
 * Event classes supported:
 *
 * <ul>
 *   <li>com.fluxtion.compiler.generation.model.ExportFunctionMarker
 *   <li>com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails
 *   <li>com.fluxtion.example.cookbook.ml.linearregression.node.ReCalibrationCompleteEvent
 *   <li>com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class OpportunityMlProcessor
    implements EventProcessor<OpportunityMlProcessor>,
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle,
        CalibrationProcessor,
        HouseSalesMonitor,
        OpportunityNotifier {

  //Node declarations
  private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  private final LiveHouseSalesCache liveHouseSalesCache_7 = new LiveHouseSalesCache();
  public final NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final SubscriptionManagerNode subscriptionManager = new SubscriptionManagerNode();
  private final MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  private final DefaultEventHandlerNode handlerHouseSaleDetails =
      new DefaultEventHandlerNode<>(
          2147483647,
          "",
          com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails.class,
          "handlerHouseSaleDetails",
          context);
  private final PeekFlowFunction peekFlowFunction_1 =
      new PeekFlowFunction<>(handlerHouseSaleDetails, PreProcessPipeline::logIncoming);
  private final MapRef2RefFlowFunction mapRef2RefFlowFunction_2 =
      new MapRef2RefFlowFunction<>(peekFlowFunction_1, HouseTransformer::asPostProcess);
  private final FilterFlowFunction filterFlowFunction_3 =
      new FilterFlowFunction<>(mapRef2RefFlowFunction_2, HouseFilters::bedroomWithinRange);
  private final FilterFlowFunction filterFlowFunction_4 =
      new FilterFlowFunction<>(filterFlowFunction_3, HouseFilters::correctLocation);
  private final PeekFlowFunction peekFlowFunction_5 =
      new PeekFlowFunction<>(filterFlowFunction_4, PreProcessPipeline::logValid);
  private final AreaFeature AreaFeature_0 = new AreaFeature(peekFlowFunction_5);
  private final PredictiveLinearRegressionModel predictiveLinearRegressionModel_6 =
      new PredictiveLinearRegressionModel(new Feature[] {AreaFeature_0});
  private final OpportunityNotifierNode opportunityNotifierNode_0 =
      new OpportunityNotifierNode(predictiveLinearRegressionModel_6, liveHouseSalesCache_7);
  public final Clock clock = new Clock();
  private final ExportFunctionAuditEvent functionAudit = new ExportFunctionAuditEvent();
  //Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(8);
  private final IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(8);

  private boolean isDirty_AreaFeature_0 = false;
  private boolean isDirty_filterFlowFunction_3 = false;
  private boolean isDirty_filterFlowFunction_4 = false;
  private boolean isDirty_handlerHouseSaleDetails = false;
  private boolean isDirty_mapRef2RefFlowFunction_2 = false;
  private boolean isDirty_peekFlowFunction_1 = false;
  private boolean isDirty_peekFlowFunction_5 = false;
  private boolean isDirty_predictiveLinearRegressionModel_6 = false;
  //Forked declarations

  //Filter constants

  public OpportunityMlProcessor(Map<Object, Object> contextMap) {
    context.replaceMappings(contextMap);
    liveHouseSalesCache_7.setDispatcher(callbackDispatcher);
    filterFlowFunction_3.setEventProcessorContext(context);
    filterFlowFunction_4.setEventProcessorContext(context);
    mapRef2RefFlowFunction_2.setEventProcessorContext(context);
    peekFlowFunction_1.setEventProcessorContext(context);
    peekFlowFunction_5.setEventProcessorContext(context);
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
    handlerHouseSaleDetails.init();
    peekFlowFunction_1.initialiseEventStream();
    mapRef2RefFlowFunction_2.initialiseEventStream();
    filterFlowFunction_3.initialiseEventStream();
    filterFlowFunction_4.initialiseEventStream();
    peekFlowFunction_5.initialiseEventStream();
    AreaFeature_0.init();
    predictiveLinearRegressionModel_6.init();
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
    handlerHouseSaleDetails.tearDown();
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
    if (event instanceof com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails) {
      HouseSaleDetails typedEvent = (HouseSaleDetails) event;
      handleEvent(typedEvent);
    } else if (event
        instanceof
        com.fluxtion.example.cookbook.ml.linearregression.node.ReCalibrationCompleteEvent) {
      ReCalibrationCompleteEvent typedEvent = (ReCalibrationCompleteEvent) event;
      handleEvent(typedEvent);
    } else if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      handleEvent(typedEvent);
    }
  }

  public void handleEvent(HouseSaleDetails typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    liveHouseSalesCache_7.newHouseForSaleAdvert(typedEvent);
    isDirty_handlerHouseSaleDetails = handlerHouseSaleDetails.onEvent(typedEvent);
    if (isDirty_handlerHouseSaleDetails) {
      peekFlowFunction_1.inputUpdated(handlerHouseSaleDetails);
    }
    if (guardCheck_peekFlowFunction_1()) {
      isDirty_peekFlowFunction_1 = true;
      peekFlowFunction_1.peek();
      if (isDirty_peekFlowFunction_1) {
        mapRef2RefFlowFunction_2.inputUpdated(peekFlowFunction_1);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_2()) {
      isDirty_mapRef2RefFlowFunction_2 = mapRef2RefFlowFunction_2.map();
      if (isDirty_mapRef2RefFlowFunction_2) {
        filterFlowFunction_3.inputUpdated(mapRef2RefFlowFunction_2);
      }
    }
    if (guardCheck_filterFlowFunction_3()) {
      isDirty_filterFlowFunction_3 = filterFlowFunction_3.filter();
      if (isDirty_filterFlowFunction_3) {
        filterFlowFunction_4.inputUpdated(filterFlowFunction_3);
      }
    }
    if (guardCheck_filterFlowFunction_4()) {
      isDirty_filterFlowFunction_4 = filterFlowFunction_4.filter();
      if (isDirty_filterFlowFunction_4) {
        peekFlowFunction_5.inputUpdated(filterFlowFunction_4);
      }
    }
    if (guardCheck_peekFlowFunction_5()) {
      isDirty_peekFlowFunction_5 = true;
      peekFlowFunction_5.peek();
    }
    if (guardCheck_AreaFeature_0()) {
      isDirty_AreaFeature_0 = AreaFeature_0.processRecord();
      if (isDirty_AreaFeature_0) {
        predictiveLinearRegressionModel_6.featureUpdated(AreaFeature_0);
      }
    }
    if (guardCheck_predictiveLinearRegressionModel_6()) {
      isDirty_predictiveLinearRegressionModel_6 =
          predictiveLinearRegressionModel_6.calculateInference();
    }
    if (guardCheck_opportunityNotifierNode_0()) {
      opportunityNotifierNode_0.predictionUpdated();
    }
    afterEvent();
  }

  public void handleEvent(ReCalibrationCompleteEvent typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    opportunityNotifierNode_0.recalibrationComplete(typedEvent);
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
        "public boolean com.fluxtion.example.cookbook.ml.linearregression.node.LiveHouseSalesCache.setCalibration(java.util.List<com.fluxtion.runtime.ml.Calibration>)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    liveHouseSalesCache_7.setCalibration(arg0);
    isDirty_AreaFeature_0 = AreaFeature_0.setCalibration(arg0);
    if (isDirty_AreaFeature_0) {
      predictiveLinearRegressionModel_6.featureUpdated(AreaFeature_0);
    }
    isDirty_predictiveLinearRegressionModel_6 =
        predictiveLinearRegressionModel_6.setCalibration(arg0);
    opportunityNotifierNode_0.setCalibration(arg0);
    afterServiceCall();
    return true;
  }

  @Override
  public void houseSold(
      com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails arg0) {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.ml.linearregression.node.LiveHouseSalesCache.houseSold(com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    liveHouseSalesCache_7.houseSold(arg0);
    opportunityNotifierNode_0.houseSold(arg0);
    afterServiceCall();
  }

  @Override
  public void removeAllSales() {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.ml.linearregression.node.LiveHouseSalesCache.removeAllSales()");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    liveHouseSalesCache_7.removeAllSales();
    opportunityNotifierNode_0.removeAllSales();
    afterServiceCall();
  }

  @Override
  public void setEnableNotifications(boolean arg0) {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.ml.linearregression.node.OpportunityNotifierNode.setEnableNotifications(boolean)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    opportunityNotifierNode_0.setEnableNotifications(arg0);
    afterServiceCall();
  }

  @Override
  public void setNotificationSink(
      java.util.function.Consumer<
              java.util.Collection<
                  com.fluxtion.example.cookbook.ml.linearregression.api.PotentialOpportunity>>
          arg0) {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.ml.linearregression.node.OpportunityNotifierNode.setNotificationSink(java.util.function.Consumer<java.util.Collection<com.fluxtion.example.cookbook.ml.linearregression.api.PotentialOpportunity>>)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    opportunityNotifierNode_0.setNotificationSink(arg0);
    afterServiceCall();
  }

  @Override
  public void setProfitTrigger(double arg0) {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.ml.linearregression.node.OpportunityNotifierNode.setProfitTrigger(double)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    opportunityNotifierNode_0.setProfitTrigger(arg0);
    afterServiceCall();
  }
  //EXPORTED SERVICE FUNCTIONS - END

  public void bufferEvent(Object event) {
    buffering = true;
    if (event instanceof com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails) {
      HouseSaleDetails typedEvent = (HouseSaleDetails) event;
      auditEvent(typedEvent);
      liveHouseSalesCache_7.newHouseForSaleAdvert(typedEvent);
      isDirty_handlerHouseSaleDetails = handlerHouseSaleDetails.onEvent(typedEvent);
      if (isDirty_handlerHouseSaleDetails) {
        peekFlowFunction_1.inputUpdated(handlerHouseSaleDetails);
      }
    } else if (event
        instanceof
        com.fluxtion.example.cookbook.ml.linearregression.node.ReCalibrationCompleteEvent) {
      ReCalibrationCompleteEvent typedEvent = (ReCalibrationCompleteEvent) event;
      auditEvent(typedEvent);
      opportunityNotifierNode_0.recalibrationComplete(typedEvent);
    } else if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      auditEvent(typedEvent);
      clock.setClockStrategy(typedEvent);
    }
  }

  public void triggerCalculation() {
    buffering = false;
    String typedEvent = "No event information - buffered dispatch";
    if (guardCheck_peekFlowFunction_1()) {
      isDirty_peekFlowFunction_1 = true;
      peekFlowFunction_1.peek();
      if (isDirty_peekFlowFunction_1) {
        mapRef2RefFlowFunction_2.inputUpdated(peekFlowFunction_1);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_2()) {
      isDirty_mapRef2RefFlowFunction_2 = mapRef2RefFlowFunction_2.map();
      if (isDirty_mapRef2RefFlowFunction_2) {
        filterFlowFunction_3.inputUpdated(mapRef2RefFlowFunction_2);
      }
    }
    if (guardCheck_filterFlowFunction_3()) {
      isDirty_filterFlowFunction_3 = filterFlowFunction_3.filter();
      if (isDirty_filterFlowFunction_3) {
        filterFlowFunction_4.inputUpdated(filterFlowFunction_3);
      }
    }
    if (guardCheck_filterFlowFunction_4()) {
      isDirty_filterFlowFunction_4 = filterFlowFunction_4.filter();
      if (isDirty_filterFlowFunction_4) {
        peekFlowFunction_5.inputUpdated(filterFlowFunction_4);
      }
    }
    if (guardCheck_peekFlowFunction_5()) {
      isDirty_peekFlowFunction_5 = true;
      peekFlowFunction_5.peek();
    }
    if (guardCheck_AreaFeature_0()) {
      isDirty_AreaFeature_0 = AreaFeature_0.processRecord();
      if (isDirty_AreaFeature_0) {
        predictiveLinearRegressionModel_6.featureUpdated(AreaFeature_0);
      }
    }
    if (guardCheck_predictiveLinearRegressionModel_6()) {
      isDirty_predictiveLinearRegressionModel_6 =
          predictiveLinearRegressionModel_6.calculateInference();
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
    auditor.nodeRegistered(liveHouseSalesCache_7, "liveHouseSalesCache_7");
    auditor.nodeRegistered(opportunityNotifierNode_0, "opportunityNotifierNode_0");
    auditor.nodeRegistered(AreaFeature_0, "AreaFeature_0");
    auditor.nodeRegistered(callbackDispatcher, "callbackDispatcher");
    auditor.nodeRegistered(filterFlowFunction_3, "filterFlowFunction_3");
    auditor.nodeRegistered(filterFlowFunction_4, "filterFlowFunction_4");
    auditor.nodeRegistered(mapRef2RefFlowFunction_2, "mapRef2RefFlowFunction_2");
    auditor.nodeRegistered(peekFlowFunction_1, "peekFlowFunction_1");
    auditor.nodeRegistered(peekFlowFunction_5, "peekFlowFunction_5");
    auditor.nodeRegistered(subscriptionManager, "subscriptionManager");
    auditor.nodeRegistered(predictiveLinearRegressionModel_6, "predictiveLinearRegressionModel_6");
    auditor.nodeRegistered(handlerHouseSaleDetails, "handlerHouseSaleDetails");
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
    isDirty_filterFlowFunction_3 = false;
    isDirty_filterFlowFunction_4 = false;
    isDirty_handlerHouseSaleDetails = false;
    isDirty_mapRef2RefFlowFunction_2 = false;
    isDirty_peekFlowFunction_1 = false;
    isDirty_peekFlowFunction_5 = false;
    isDirty_predictiveLinearRegressionModel_6 = false;
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
      dirtyFlagSupplierMap.put(filterFlowFunction_3, () -> isDirty_filterFlowFunction_3);
      dirtyFlagSupplierMap.put(filterFlowFunction_4, () -> isDirty_filterFlowFunction_4);
      dirtyFlagSupplierMap.put(handlerHouseSaleDetails, () -> isDirty_handlerHouseSaleDetails);
      dirtyFlagSupplierMap.put(mapRef2RefFlowFunction_2, () -> isDirty_mapRef2RefFlowFunction_2);
      dirtyFlagSupplierMap.put(peekFlowFunction_1, () -> isDirty_peekFlowFunction_1);
      dirtyFlagSupplierMap.put(peekFlowFunction_5, () -> isDirty_peekFlowFunction_5);
      dirtyFlagSupplierMap.put(
          predictiveLinearRegressionModel_6, () -> isDirty_predictiveLinearRegressionModel_6);
    }
    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(AreaFeature_0, (b) -> isDirty_AreaFeature_0 = b);
      dirtyFlagUpdateMap.put(filterFlowFunction_3, (b) -> isDirty_filterFlowFunction_3 = b);
      dirtyFlagUpdateMap.put(filterFlowFunction_4, (b) -> isDirty_filterFlowFunction_4 = b);
      dirtyFlagUpdateMap.put(handlerHouseSaleDetails, (b) -> isDirty_handlerHouseSaleDetails = b);
      dirtyFlagUpdateMap.put(mapRef2RefFlowFunction_2, (b) -> isDirty_mapRef2RefFlowFunction_2 = b);
      dirtyFlagUpdateMap.put(peekFlowFunction_1, (b) -> isDirty_peekFlowFunction_1 = b);
      dirtyFlagUpdateMap.put(peekFlowFunction_5, (b) -> isDirty_peekFlowFunction_5 = b);
      dirtyFlagUpdateMap.put(
          predictiveLinearRegressionModel_6, (b) -> isDirty_predictiveLinearRegressionModel_6 = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_opportunityNotifierNode_0() {
    return isDirty_predictiveLinearRegressionModel_6;
  }

  private boolean guardCheck_AreaFeature_0() {
    return isDirty_peekFlowFunction_5;
  }

  private boolean guardCheck_filterFlowFunction_3() {
    return isDirty_mapRef2RefFlowFunction_2;
  }

  private boolean guardCheck_filterFlowFunction_4() {
    return isDirty_filterFlowFunction_3;
  }

  private boolean guardCheck_mapRef2RefFlowFunction_2() {
    return isDirty_peekFlowFunction_1;
  }

  private boolean guardCheck_peekFlowFunction_1() {
    return isDirty_handlerHouseSaleDetails;
  }

  private boolean guardCheck_peekFlowFunction_5() {
    return isDirty_filterFlowFunction_4;
  }

  private boolean guardCheck_predictiveLinearRegressionModel_6() {
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
