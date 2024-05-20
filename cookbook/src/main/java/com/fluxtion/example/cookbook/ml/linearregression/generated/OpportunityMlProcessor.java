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
import com.fluxtion.example.cookbook.ml.linearregression.pipeline.HousePipelineFunctions;
import com.fluxtion.example.cookbook.ml.linearregression.pipeline.LocationCategoryFeature;
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
import com.fluxtion.runtime.ml.MapPropertyToFeature;
import com.fluxtion.runtime.ml.PredictiveLinearRegressionModel;
import com.fluxtion.runtime.ml.PropertyToFeature;
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
 * eventProcessorGenerator version : 9.3.10
 * api version                     : 9.3.10
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
        /*--- @ExportService start ---*/
        CalibrationProcessor,
        HouseSalesMonitor,
        OpportunityNotifier,
        /*--- @ExportService end ---*/
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle {

  // Node declarations
  private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  private final LiveHouseSalesCache liveHouseSalesCache_4 = new LiveHouseSalesCache();
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
  private final FilterFlowFunction filterFlowFunction_1 =
      new FilterFlowFunction<>(
          handlerHouseSaleDetails, HousePipelineFunctions::bedroomWithinRangeFilter);
  private final FilterFlowFunction filterFlowFunction_2 =
      new FilterFlowFunction<>(filterFlowFunction_1, HousePipelineFunctions::correctLocationFilter);
  private final PropertyToFeature area =
      new PropertyToFeature<>("area", filterFlowFunction_2, HouseSaleDetails::getArea);
  private final MapPropertyToFeature areaSquared =
      new MapPropertyToFeature<>(
          "areaSquared",
          filterFlowFunction_2,
          HouseSaleDetails::getArea,
          HousePipelineFunctions::squared);
  private final PropertyToFeature bedroom =
      new PropertyToFeature<>("bedroom", filterFlowFunction_2, HouseSaleDetails::getBedrooms);
  private final LocationCategoryFeature locationCategoryFeature =
      new LocationCategoryFeature(filterFlowFunction_2);
  private final PropertyToFeature offerPrice =
      new PropertyToFeature<>("offerPrice", filterFlowFunction_2, HouseSaleDetails::getOfferPrice);
  private final PredictiveLinearRegressionModel predictiveLinearRegressionModel_3 =
      new PredictiveLinearRegressionModel(
          new Feature[] {offerPrice, area, areaSquared, locationCategoryFeature, bedroom});
  private final OpportunityNotifierNode opportunityNotifierNode_0 =
      new OpportunityNotifierNode(predictiveLinearRegressionModel_3, liveHouseSalesCache_4);
  public final Clock clock = new Clock();
  private final ExportFunctionAuditEvent functionAudit = new ExportFunctionAuditEvent();
  // Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(9);
  private final IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(9);

  private boolean isDirty_area = false;
  private boolean isDirty_areaSquared = false;
  private boolean isDirty_bedroom = false;
  private boolean isDirty_filterFlowFunction_1 = false;
  private boolean isDirty_filterFlowFunction_2 = false;
  private boolean isDirty_handlerHouseSaleDetails = false;
  private boolean isDirty_locationCategoryFeature = false;
  private boolean isDirty_offerPrice = false;
  private boolean isDirty_predictiveLinearRegressionModel_3 = false;

  // Forked declarations

  // Filter constants

  // unknown event handler
  private Consumer unKnownEventHandler = (e) -> {};

  public OpportunityMlProcessor(Map<Object, Object> contextMap) {
    if (context != null) {
      context.replaceMappings(contextMap);
    }
    liveHouseSalesCache_4.setDispatcher(callbackDispatcher);
    filterFlowFunction_1.setEventProcessorContext(context);
    filterFlowFunction_2.setEventProcessorContext(context);
    // node auditors
    initialiseAuditor(clock);
    initialiseAuditor(nodeNameLookup);
    if (subscriptionManager != null) {
      subscriptionManager.setSubscribingEventProcessor(this);
    }
    if (context != null) {
      context.setEventProcessorCallback(this);
    }
  }

  public OpportunityMlProcessor() {
    this(null);
  }

  @Override
  public void init() {
    initCalled = true;
    auditEvent(Lifecycle.LifecycleEvent.Init);
    // initialise dirty lookup map
    isDirty("test");
    handlerHouseSaleDetails.init();
    filterFlowFunction_1.initialiseEventStream();
    filterFlowFunction_2.initialiseEventStream();
    area.init();
    areaSquared.init();
    bedroom.init();
    locationCategoryFeature.init();
    offerPrice.init();
    predictiveLinearRegressionModel_3.init();
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

  // EVENT DISPATCH - START
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
    } else {
      unKnownEventHandler(event);
    }
  }

  public void handleEvent(HouseSaleDetails typedEvent) {
    auditEvent(typedEvent);
    // Default, no filter methods
    liveHouseSalesCache_4.newHouseForSaleAdvert(typedEvent);
    isDirty_handlerHouseSaleDetails = handlerHouseSaleDetails.onEvent(typedEvent);
    if (isDirty_handlerHouseSaleDetails) {
      filterFlowFunction_1.inputUpdated(handlerHouseSaleDetails);
    }
    if (guardCheck_filterFlowFunction_1()) {
      isDirty_filterFlowFunction_1 = filterFlowFunction_1.filter();
      if (isDirty_filterFlowFunction_1) {
        filterFlowFunction_2.inputUpdated(filterFlowFunction_1);
      }
    }
    if (guardCheck_filterFlowFunction_2()) {
      isDirty_filterFlowFunction_2 = filterFlowFunction_2.filter();
    }
    if (guardCheck_area()) {
      isDirty_area = area.calculateFeature();
    }
    if (guardCheck_areaSquared()) {
      isDirty_areaSquared = areaSquared.calculateFeature();
    }
    if (guardCheck_bedroom()) {
      isDirty_bedroom = bedroom.calculateFeature();
    }
    if (guardCheck_locationCategoryFeature()) {
      isDirty_locationCategoryFeature = locationCategoryFeature.calculateFeature();
    }
    if (guardCheck_offerPrice()) {
      isDirty_offerPrice = offerPrice.calculateFeature();
    }
    if (guardCheck_predictiveLinearRegressionModel_3()) {
      isDirty_predictiveLinearRegressionModel_3 =
          predictiveLinearRegressionModel_3.calculateInference();
    }
    if (guardCheck_opportunityNotifierNode_0()) {
      opportunityNotifierNode_0.predictionUpdated();
    }
    afterEvent();
  }

  public void handleEvent(ReCalibrationCompleteEvent typedEvent) {
    auditEvent(typedEvent);
    // Default, no filter methods
    opportunityNotifierNode_0.recalibrationComplete(typedEvent);
    afterEvent();
  }

  public void handleEvent(ClockStrategyEvent typedEvent) {
    auditEvent(typedEvent);
    // Default, no filter methods
    clock.setClockStrategy(typedEvent);
    afterEvent();
  }
  // EVENT DISPATCH - END

  // EXPORTED SERVICE FUNCTIONS - START
  @Override
  public boolean resetToOne() {
    beforeServiceCall(
        "public default boolean com.fluxtion.runtime.ml.CalibrationProcessor.resetToOne()");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    liveHouseSalesCache_4.resetToOne();
    isDirty_area = area.resetToOne();
    isDirty_areaSquared = areaSquared.resetToOne();
    isDirty_bedroom = bedroom.resetToOne();
    isDirty_locationCategoryFeature = locationCategoryFeature.resetToOne();
    isDirty_offerPrice = offerPrice.resetToOne();
    isDirty_predictiveLinearRegressionModel_3 = predictiveLinearRegressionModel_3.resetToOne();
    opportunityNotifierNode_0.resetToOne();
    afterServiceCall();
    return true;
  }

  @Override
  public boolean resetToZero() {
    beforeServiceCall(
        "public default boolean com.fluxtion.runtime.ml.CalibrationProcessor.resetToZero()");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    liveHouseSalesCache_4.resetToZero();
    isDirty_area = area.resetToZero();
    isDirty_areaSquared = areaSquared.resetToZero();
    isDirty_bedroom = bedroom.resetToZero();
    isDirty_locationCategoryFeature = locationCategoryFeature.resetToZero();
    isDirty_offerPrice = offerPrice.resetToZero();
    isDirty_predictiveLinearRegressionModel_3 = predictiveLinearRegressionModel_3.resetToZero();
    opportunityNotifierNode_0.resetToZero();
    afterServiceCall();
    return true;
  }

  @Override
  public boolean setCalibration(java.util.List<com.fluxtion.runtime.ml.Calibration> arg0) {
    beforeServiceCall(
        "public boolean com.fluxtion.example.cookbook.ml.linearregression.node.LiveHouseSalesCache.setCalibration(java.util.List<com.fluxtion.runtime.ml.Calibration>)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    liveHouseSalesCache_4.setCalibration(arg0);
    isDirty_area = area.setCalibration(arg0);
    isDirty_areaSquared = areaSquared.setCalibration(arg0);
    isDirty_bedroom = bedroom.setCalibration(arg0);
    isDirty_locationCategoryFeature = locationCategoryFeature.setCalibration(arg0);
    isDirty_offerPrice = offerPrice.setCalibration(arg0);
    isDirty_predictiveLinearRegressionModel_3 =
        predictiveLinearRegressionModel_3.setCalibration(arg0);
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
    liveHouseSalesCache_4.houseSold(arg0);
    opportunityNotifierNode_0.houseSold(arg0);
    afterServiceCall();
  }

  @Override
  public void removeAllSales() {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.ml.linearregression.node.LiveHouseSalesCache.removeAllSales()");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    liveHouseSalesCache_4.removeAllSales();
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
  // EXPORTED SERVICE FUNCTIONS - END

  public void bufferEvent(Object event) {
    buffering = true;
    if (event instanceof com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails) {
      HouseSaleDetails typedEvent = (HouseSaleDetails) event;
      auditEvent(typedEvent);
      liveHouseSalesCache_4.newHouseForSaleAdvert(typedEvent);
      isDirty_handlerHouseSaleDetails = handlerHouseSaleDetails.onEvent(typedEvent);
      if (isDirty_handlerHouseSaleDetails) {
        filterFlowFunction_1.inputUpdated(handlerHouseSaleDetails);
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
    if (guardCheck_filterFlowFunction_1()) {
      isDirty_filterFlowFunction_1 = filterFlowFunction_1.filter();
      if (isDirty_filterFlowFunction_1) {
        filterFlowFunction_2.inputUpdated(filterFlowFunction_1);
      }
    }
    if (guardCheck_filterFlowFunction_2()) {
      isDirty_filterFlowFunction_2 = filterFlowFunction_2.filter();
    }
    if (guardCheck_area()) {
      isDirty_area = area.calculateFeature();
    }
    if (guardCheck_areaSquared()) {
      isDirty_areaSquared = areaSquared.calculateFeature();
    }
    if (guardCheck_bedroom()) {
      isDirty_bedroom = bedroom.calculateFeature();
    }
    if (guardCheck_locationCategoryFeature()) {
      isDirty_locationCategoryFeature = locationCategoryFeature.calculateFeature();
    }
    if (guardCheck_offerPrice()) {
      isDirty_offerPrice = offerPrice.calculateFeature();
    }
    if (guardCheck_predictiveLinearRegressionModel_3()) {
      isDirty_predictiveLinearRegressionModel_3 =
          predictiveLinearRegressionModel_3.calculateInference();
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
    auditor.nodeRegistered(liveHouseSalesCache_4, "liveHouseSalesCache_4");
    auditor.nodeRegistered(opportunityNotifierNode_0, "opportunityNotifierNode_0");
    auditor.nodeRegistered(locationCategoryFeature, "locationCategoryFeature");
    auditor.nodeRegistered(callbackDispatcher, "callbackDispatcher");
    auditor.nodeRegistered(filterFlowFunction_1, "filterFlowFunction_1");
    auditor.nodeRegistered(filterFlowFunction_2, "filterFlowFunction_2");
    auditor.nodeRegistered(subscriptionManager, "subscriptionManager");
    auditor.nodeRegistered(areaSquared, "areaSquared");
    auditor.nodeRegistered(predictiveLinearRegressionModel_3, "predictiveLinearRegressionModel_3");
    auditor.nodeRegistered(area, "area");
    auditor.nodeRegistered(bedroom, "bedroom");
    auditor.nodeRegistered(offerPrice, "offerPrice");
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
    isDirty_area = false;
    isDirty_areaSquared = false;
    isDirty_bedroom = false;
    isDirty_filterFlowFunction_1 = false;
    isDirty_filterFlowFunction_2 = false;
    isDirty_handlerHouseSaleDetails = false;
    isDirty_locationCategoryFeature = false;
    isDirty_offerPrice = false;
    isDirty_predictiveLinearRegressionModel_3 = false;
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
      dirtyFlagSupplierMap.put(area, () -> isDirty_area);
      dirtyFlagSupplierMap.put(areaSquared, () -> isDirty_areaSquared);
      dirtyFlagSupplierMap.put(bedroom, () -> isDirty_bedroom);
      dirtyFlagSupplierMap.put(filterFlowFunction_1, () -> isDirty_filterFlowFunction_1);
      dirtyFlagSupplierMap.put(filterFlowFunction_2, () -> isDirty_filterFlowFunction_2);
      dirtyFlagSupplierMap.put(handlerHouseSaleDetails, () -> isDirty_handlerHouseSaleDetails);
      dirtyFlagSupplierMap.put(locationCategoryFeature, () -> isDirty_locationCategoryFeature);
      dirtyFlagSupplierMap.put(offerPrice, () -> isDirty_offerPrice);
      dirtyFlagSupplierMap.put(
          predictiveLinearRegressionModel_3, () -> isDirty_predictiveLinearRegressionModel_3);
    }
    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(area, (b) -> isDirty_area = b);
      dirtyFlagUpdateMap.put(areaSquared, (b) -> isDirty_areaSquared = b);
      dirtyFlagUpdateMap.put(bedroom, (b) -> isDirty_bedroom = b);
      dirtyFlagUpdateMap.put(filterFlowFunction_1, (b) -> isDirty_filterFlowFunction_1 = b);
      dirtyFlagUpdateMap.put(filterFlowFunction_2, (b) -> isDirty_filterFlowFunction_2 = b);
      dirtyFlagUpdateMap.put(handlerHouseSaleDetails, (b) -> isDirty_handlerHouseSaleDetails = b);
      dirtyFlagUpdateMap.put(locationCategoryFeature, (b) -> isDirty_locationCategoryFeature = b);
      dirtyFlagUpdateMap.put(offerPrice, (b) -> isDirty_offerPrice = b);
      dirtyFlagUpdateMap.put(
          predictiveLinearRegressionModel_3, (b) -> isDirty_predictiveLinearRegressionModel_3 = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_opportunityNotifierNode_0() {
    return isDirty_predictiveLinearRegressionModel_3;
  }

  private boolean guardCheck_locationCategoryFeature() {
    return isDirty_filterFlowFunction_2;
  }

  private boolean guardCheck_filterFlowFunction_1() {
    return isDirty_handlerHouseSaleDetails;
  }

  private boolean guardCheck_filterFlowFunction_2() {
    return isDirty_filterFlowFunction_1;
  }

  private boolean guardCheck_areaSquared() {
    return isDirty_filterFlowFunction_2;
  }

  private boolean guardCheck_predictiveLinearRegressionModel_3() {
    return isDirty_area
        | isDirty_areaSquared
        | isDirty_bedroom
        | isDirty_locationCategoryFeature
        | isDirty_offerPrice;
  }

  private boolean guardCheck_area() {
    return isDirty_filterFlowFunction_2;
  }

  private boolean guardCheck_bedroom() {
    return isDirty_filterFlowFunction_2;
  }

  private boolean guardCheck_offerPrice() {
    return isDirty_filterFlowFunction_2;
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

  public void unKnownEventHandler(Object object) {
    unKnownEventHandler.accept(object);
  }

  @Override
  public <T> void setUnKnownEventHandler(Consumer<T> consumer) {
    unKnownEventHandler = consumer;
  }
}
