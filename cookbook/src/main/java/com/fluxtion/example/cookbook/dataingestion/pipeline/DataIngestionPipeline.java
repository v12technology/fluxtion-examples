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
package com.fluxtion.example.cookbook.dataingestion.pipeline;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestComponent;
import com.fluxtion.example.cookbook.dataingestion.api.DataIngestStats;
import com.fluxtion.example.cookbook.dataingestion.function.CsvToHouseRecordSerializer;
import com.fluxtion.example.cookbook.dataingestion.function.HouseRecordTransformer;
import com.fluxtion.example.cookbook.dataingestion.function.HouseRecordValidator;
import com.fluxtion.example.cookbook.dataingestion.function.InvalidLogWriter;
import com.fluxtion.example.cookbook.dataingestion.function.PostProcessBinaryWriter;
import com.fluxtion.example.cookbook.dataingestion.function.PostProcessCsvWriter;
import com.fluxtion.example.cookbook.dataingestion.function.ProcessingStats;
import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.annotations.ExportService;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.audit.EventLogManager;
import com.fluxtion.runtime.audit.NodeNameAuditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.callback.ExportFunctionAuditEvent;
import com.fluxtion.runtime.dataflow.function.FilterFlowFunction;
import com.fluxtion.runtime.dataflow.function.MapFlowFunction.MapRef2RefFlowFunction;
import com.fluxtion.runtime.dataflow.function.PushFlowFunction;
import com.fluxtion.runtime.event.Event;
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
 * eventProcessorGenerator version : 9.7.2
 * api version                     : 9.7.2
 * </pre>
 *
 * Event classes supported:
 *
 * <ul>
 *   <li>com.fluxtion.compiler.generation.model.ExportFunctionMarker
 *   <li>com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent
 *   <li>java.lang.String
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class DataIngestionPipeline
    implements EventProcessor<DataIngestionPipeline>,
        /*--- @ExportService start ---*/
        @ExportService DataIngestComponent,
        @ExportService DataIngestStats,
        @ExportService ServiceListener,
        /*--- @ExportService end ---*/
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle {

  // Node declarations
  private final transient CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  public final transient Clock clock = new Clock();
  private final transient CsvToHouseRecordSerializer csvToHouseRecordSerializer_0 =
      new CsvToHouseRecordSerializer();
  private final transient HouseRecordTransformer houseRecordTransformer_3 =
      new HouseRecordTransformer();
  private final transient HouseRecordValidator houseRecordValidator_5 = new HouseRecordValidator();
  private final transient InvalidLogWriter invalidLogWriter_47 = new InvalidLogWriter();
  public final transient NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final transient PostProcessBinaryWriter postProcessBinaryWriter_35 =
      new PostProcessBinaryWriter();
  private final transient PostProcessCsvWriter postProcessCsvWriter_31 = new PostProcessCsvWriter();
  private final transient ProcessingStats processingStats_51 = new ProcessingStats();
  private final transient SubscriptionManagerNode subscriptionManager =
      new SubscriptionManagerNode();
  private final transient MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  private final transient DefaultEventHandlerNode handlerString =
      new DefaultEventHandlerNode<>(
          2147483647, "", java.lang.String.class, "handlerString", context);
  private final transient MapRef2RefFlowFunction mapRef2RefFlowFunction_1 =
      new MapRef2RefFlowFunction<>(handlerString, csvToHouseRecordSerializer_0::marshall);
  private final transient FilterFlowFunction filterFlowFunction_11 =
      new FilterFlowFunction<>(
          mapRef2RefFlowFunction_1, CsvToHouseRecordSerializer::isBadCsvMessage);
  private final transient MapRef2RefFlowFunction mapRef2RefFlowFunction_2 =
      new MapRef2RefFlowFunction<>(
          mapRef2RefFlowFunction_1, CsvToHouseRecordSerializer::getHouseRecord);
  private final transient MapRef2RefFlowFunction mapRef2RefFlowFunction_4 =
      new MapRef2RefFlowFunction<>(mapRef2RefFlowFunction_2, houseRecordTransformer_3::transform);
  private final transient MapRef2RefFlowFunction mapRef2RefFlowFunction_6 =
      new MapRef2RefFlowFunction<>(mapRef2RefFlowFunction_4, houseRecordValidator_5::validate);
  private final transient FilterFlowFunction filterFlowFunction_14 =
      new FilterFlowFunction<>(mapRef2RefFlowFunction_6, HouseRecordValidator::isInValidRecord);
  private final transient MapRef2RefFlowFunction mapRef2RefFlowFunction_7 =
      new MapRef2RefFlowFunction<>(
          mapRef2RefFlowFunction_6, HouseRecordValidator::getValidHouseRecord);
  private final transient PushFlowFunction pushFlowFunction_8 =
      new PushFlowFunction<>(mapRef2RefFlowFunction_7, processingStats_51::validHouseRecord);
  private final transient PushFlowFunction pushFlowFunction_9 =
      new PushFlowFunction<>(mapRef2RefFlowFunction_7, postProcessCsvWriter_31::validHouseRecord);
  private final transient PushFlowFunction pushFlowFunction_10 =
      new PushFlowFunction<>(
          mapRef2RefFlowFunction_7, postProcessBinaryWriter_35::validHouseRecord);
  private final transient PushFlowFunction pushFlowFunction_12 =
      new PushFlowFunction<>(filterFlowFunction_11, invalidLogWriter_47::badCsvRecord);
  private final transient PushFlowFunction pushFlowFunction_13 =
      new PushFlowFunction<>(filterFlowFunction_11, processingStats_51::badCsvRecord);
  private final transient PushFlowFunction pushFlowFunction_15 =
      new PushFlowFunction<>(filterFlowFunction_14, invalidLogWriter_47::invalidHouseRecord);
  private final transient PushFlowFunction pushFlowFunction_16 =
      new PushFlowFunction<>(filterFlowFunction_14, processingStats_51::invalidHouseRecord);
  public final transient ServiceRegistryNode serviceRegistry = new ServiceRegistryNode();
  private final transient ExportFunctionAuditEvent functionAudit = new ExportFunctionAuditEvent();
  // Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final transient IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(16);
  private final transient IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(16);

  private boolean isDirty_clock = false;
  private boolean isDirty_filterFlowFunction_11 = false;
  private boolean isDirty_filterFlowFunction_14 = false;
  private boolean isDirty_handlerString = false;
  private boolean isDirty_mapRef2RefFlowFunction_1 = false;
  private boolean isDirty_mapRef2RefFlowFunction_2 = false;
  private boolean isDirty_mapRef2RefFlowFunction_4 = false;
  private boolean isDirty_mapRef2RefFlowFunction_6 = false;
  private boolean isDirty_mapRef2RefFlowFunction_7 = false;
  private boolean isDirty_pushFlowFunction_8 = false;
  private boolean isDirty_pushFlowFunction_9 = false;
  private boolean isDirty_pushFlowFunction_10 = false;
  private boolean isDirty_pushFlowFunction_12 = false;
  private boolean isDirty_pushFlowFunction_13 = false;
  private boolean isDirty_pushFlowFunction_15 = false;
  private boolean isDirty_pushFlowFunction_16 = false;

  // Forked declarations

  // Filter constants

  // unknown event handler
  private Consumer unKnownEventHandler = (e) -> {};

  public DataIngestionPipeline(Map<Object, Object> contextMap) {
    if (context != null) {
      context.replaceMappings(contextMap);
    }
    filterFlowFunction_11.setEventProcessorContext(context);
    filterFlowFunction_14.setEventProcessorContext(context);
    mapRef2RefFlowFunction_1.setEventProcessorContext(context);
    mapRef2RefFlowFunction_2.setEventProcessorContext(context);
    mapRef2RefFlowFunction_4.setEventProcessorContext(context);
    mapRef2RefFlowFunction_6.setEventProcessorContext(context);
    mapRef2RefFlowFunction_7.setEventProcessorContext(context);
    pushFlowFunction_8.setEventProcessorContext(context);
    pushFlowFunction_9.setEventProcessorContext(context);
    pushFlowFunction_10.setEventProcessorContext(context);
    pushFlowFunction_12.setEventProcessorContext(context);
    pushFlowFunction_13.setEventProcessorContext(context);
    pushFlowFunction_15.setEventProcessorContext(context);
    pushFlowFunction_16.setEventProcessorContext(context);
    context.setClock(clock);
    serviceRegistry.setEventProcessorContext(context);
    // node auditors
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

  public DataIngestionPipeline() {
    this(null);
  }

  @Override
  public void init() {
    initCalled = true;
    auditEvent(Lifecycle.LifecycleEvent.Init);
    // initialise dirty lookup map
    isDirty("test");
    clock.init();
    csvToHouseRecordSerializer_0.init();
    houseRecordValidator_5.init();
    invalidLogWriter_47.init();
    postProcessBinaryWriter_35.init();
    postProcessCsvWriter_31.init();
    processingStats_51.init();
    handlerString.init();
    mapRef2RefFlowFunction_1.initialiseEventStream();
    filterFlowFunction_11.initialiseEventStream();
    mapRef2RefFlowFunction_2.initialiseEventStream();
    mapRef2RefFlowFunction_4.initialiseEventStream();
    mapRef2RefFlowFunction_6.initialiseEventStream();
    filterFlowFunction_14.initialiseEventStream();
    mapRef2RefFlowFunction_7.initialiseEventStream();
    pushFlowFunction_8.initialiseEventStream();
    pushFlowFunction_9.initialiseEventStream();
    pushFlowFunction_10.initialiseEventStream();
    pushFlowFunction_12.initialiseEventStream();
    pushFlowFunction_13.initialiseEventStream();
    pushFlowFunction_15.initialiseEventStream();
    pushFlowFunction_16.initialiseEventStream();
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
    handlerString.tearDown();
    subscriptionManager.tearDown();
    processingStats_51.tearDown();
    postProcessCsvWriter_31.tearDown();
    postProcessBinaryWriter_35.tearDown();
    invalidLogWriter_47.tearDown();
    houseRecordValidator_5.tearDown();
    csvToHouseRecordSerializer_0.tearDown();
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
    if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      handleEvent(typedEvent);
    } else if (event instanceof java.lang.String) {
      String typedEvent = (String) event;
      handleEvent(typedEvent);
    } else {
      unKnownEventHandler(event);
    }
  }

  public void handleEvent(ClockStrategyEvent typedEvent) {
    auditEvent(typedEvent);
    // Default, no filter methods
    isDirty_clock = true;
    clock.setClockStrategy(typedEvent);
    afterEvent();
  }

  public void handleEvent(String typedEvent) {
    auditEvent(typedEvent);
    // Default, no filter methods
    isDirty_handlerString = handlerString.onEvent(typedEvent);
    if (isDirty_handlerString) {
      mapRef2RefFlowFunction_1.inputUpdated(handlerString);
    }
    if (guardCheck_mapRef2RefFlowFunction_1()) {
      isDirty_mapRef2RefFlowFunction_1 = mapRef2RefFlowFunction_1.map();
      if (isDirty_mapRef2RefFlowFunction_1) {
        filterFlowFunction_11.inputUpdated(mapRef2RefFlowFunction_1);
        mapRef2RefFlowFunction_2.inputUpdated(mapRef2RefFlowFunction_1);
      }
    }
    if (guardCheck_filterFlowFunction_11()) {
      isDirty_filterFlowFunction_11 = filterFlowFunction_11.filter();
      if (isDirty_filterFlowFunction_11) {
        pushFlowFunction_12.inputUpdated(filterFlowFunction_11);
        pushFlowFunction_13.inputUpdated(filterFlowFunction_11);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_2()) {
      isDirty_mapRef2RefFlowFunction_2 = mapRef2RefFlowFunction_2.map();
      if (isDirty_mapRef2RefFlowFunction_2) {
        mapRef2RefFlowFunction_4.inputUpdated(mapRef2RefFlowFunction_2);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_4()) {
      isDirty_mapRef2RefFlowFunction_4 = mapRef2RefFlowFunction_4.map();
      if (isDirty_mapRef2RefFlowFunction_4) {
        mapRef2RefFlowFunction_6.inputUpdated(mapRef2RefFlowFunction_4);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_6()) {
      isDirty_mapRef2RefFlowFunction_6 = mapRef2RefFlowFunction_6.map();
      if (isDirty_mapRef2RefFlowFunction_6) {
        filterFlowFunction_14.inputUpdated(mapRef2RefFlowFunction_6);
        mapRef2RefFlowFunction_7.inputUpdated(mapRef2RefFlowFunction_6);
      }
    }
    if (guardCheck_filterFlowFunction_14()) {
      isDirty_filterFlowFunction_14 = filterFlowFunction_14.filter();
      if (isDirty_filterFlowFunction_14) {
        pushFlowFunction_15.inputUpdated(filterFlowFunction_14);
        pushFlowFunction_16.inputUpdated(filterFlowFunction_14);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_7()) {
      isDirty_mapRef2RefFlowFunction_7 = mapRef2RefFlowFunction_7.map();
      if (isDirty_mapRef2RefFlowFunction_7) {
        pushFlowFunction_8.inputUpdated(mapRef2RefFlowFunction_7);
        pushFlowFunction_9.inputUpdated(mapRef2RefFlowFunction_7);
        pushFlowFunction_10.inputUpdated(mapRef2RefFlowFunction_7);
      }
    }
    if (guardCheck_pushFlowFunction_8()) {
      isDirty_pushFlowFunction_8 = pushFlowFunction_8.push();
    }
    if (guardCheck_pushFlowFunction_9()) {
      isDirty_pushFlowFunction_9 = pushFlowFunction_9.push();
    }
    if (guardCheck_pushFlowFunction_10()) {
      isDirty_pushFlowFunction_10 = pushFlowFunction_10.push();
    }
    if (guardCheck_pushFlowFunction_12()) {
      isDirty_pushFlowFunction_12 = pushFlowFunction_12.push();
    }
    if (guardCheck_pushFlowFunction_13()) {
      isDirty_pushFlowFunction_13 = pushFlowFunction_13.push();
    }
    if (guardCheck_pushFlowFunction_15()) {
      isDirty_pushFlowFunction_15 = pushFlowFunction_15.push();
    }
    if (guardCheck_pushFlowFunction_16()) {
      isDirty_pushFlowFunction_16 = pushFlowFunction_16.push();
    }
    afterEvent();
  }
  // EVENT DISPATCH - END

  // EXPORTED SERVICE FUNCTIONS - START
  @Override
  public boolean configUpdate(
      com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfig arg0) {
    beforeServiceCall(
        "public boolean com.fluxtion.example.cookbook.dataingestion.function.HouseRecordTransformer.configUpdate(com.fluxtion.example.cookbook.dataingestion.api.DataIngestConfig)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    houseRecordTransformer_3.configUpdate(arg0);
    houseRecordValidator_5.configUpdate(arg0);
    postProcessCsvWriter_31.configUpdate(arg0);
    postProcessBinaryWriter_35.configUpdate(arg0);
    invalidLogWriter_47.configUpdate(arg0);
    processingStats_51.configUpdate(arg0);
    afterServiceCall();
    return true;
  }

  @Override
  public void clearStats() {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.dataingestion.function.ProcessingStats.clearStats()");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    processingStats_51.clearStats();
    afterServiceCall();
  }

  @Override
  public void currentStats(java.util.function.Consumer<String> arg0) {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.dataingestion.function.ProcessingStats.currentStats(java.util.function.Consumer<java.lang.String>)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    processingStats_51.currentStats(arg0);
    afterServiceCall();
  }

  @Override
  public void deRegisterService(com.fluxtion.runtime.service.Service<?> arg0) {
    beforeServiceCall(
        "public void com.fluxtion.runtime.service.ServiceRegistryNode.deRegisterService(com.fluxtion.runtime.service.Service<?>)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    serviceRegistry.deRegisterService(arg0);
    afterServiceCall();
  }

  @Override
  public void publishStats() {
    beforeServiceCall(
        "public void com.fluxtion.example.cookbook.dataingestion.function.ProcessingStats.publishStats()");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    processingStats_51.publishStats();
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
  // EXPORTED SERVICE FUNCTIONS - END

  // EVENT BUFFERING - START
  public void bufferEvent(Object event) {
    buffering = true;
    if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      auditEvent(typedEvent);
      isDirty_clock = true;
      clock.setClockStrategy(typedEvent);
    } else if (event instanceof java.lang.String) {
      String typedEvent = (String) event;
      auditEvent(typedEvent);
      isDirty_handlerString = handlerString.onEvent(typedEvent);
      if (isDirty_handlerString) {
        mapRef2RefFlowFunction_1.inputUpdated(handlerString);
      }
    }
  }

  public void triggerCalculation() {
    buffering = false;
    String typedEvent = "No event information - buffered dispatch";
    if (guardCheck_mapRef2RefFlowFunction_1()) {
      isDirty_mapRef2RefFlowFunction_1 = mapRef2RefFlowFunction_1.map();
      if (isDirty_mapRef2RefFlowFunction_1) {
        filterFlowFunction_11.inputUpdated(mapRef2RefFlowFunction_1);
        mapRef2RefFlowFunction_2.inputUpdated(mapRef2RefFlowFunction_1);
      }
    }
    if (guardCheck_filterFlowFunction_11()) {
      isDirty_filterFlowFunction_11 = filterFlowFunction_11.filter();
      if (isDirty_filterFlowFunction_11) {
        pushFlowFunction_12.inputUpdated(filterFlowFunction_11);
        pushFlowFunction_13.inputUpdated(filterFlowFunction_11);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_2()) {
      isDirty_mapRef2RefFlowFunction_2 = mapRef2RefFlowFunction_2.map();
      if (isDirty_mapRef2RefFlowFunction_2) {
        mapRef2RefFlowFunction_4.inputUpdated(mapRef2RefFlowFunction_2);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_4()) {
      isDirty_mapRef2RefFlowFunction_4 = mapRef2RefFlowFunction_4.map();
      if (isDirty_mapRef2RefFlowFunction_4) {
        mapRef2RefFlowFunction_6.inputUpdated(mapRef2RefFlowFunction_4);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_6()) {
      isDirty_mapRef2RefFlowFunction_6 = mapRef2RefFlowFunction_6.map();
      if (isDirty_mapRef2RefFlowFunction_6) {
        filterFlowFunction_14.inputUpdated(mapRef2RefFlowFunction_6);
        mapRef2RefFlowFunction_7.inputUpdated(mapRef2RefFlowFunction_6);
      }
    }
    if (guardCheck_filterFlowFunction_14()) {
      isDirty_filterFlowFunction_14 = filterFlowFunction_14.filter();
      if (isDirty_filterFlowFunction_14) {
        pushFlowFunction_15.inputUpdated(filterFlowFunction_14);
        pushFlowFunction_16.inputUpdated(filterFlowFunction_14);
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_7()) {
      isDirty_mapRef2RefFlowFunction_7 = mapRef2RefFlowFunction_7.map();
      if (isDirty_mapRef2RefFlowFunction_7) {
        pushFlowFunction_8.inputUpdated(mapRef2RefFlowFunction_7);
        pushFlowFunction_9.inputUpdated(mapRef2RefFlowFunction_7);
        pushFlowFunction_10.inputUpdated(mapRef2RefFlowFunction_7);
      }
    }
    if (guardCheck_pushFlowFunction_8()) {
      isDirty_pushFlowFunction_8 = pushFlowFunction_8.push();
    }
    if (guardCheck_pushFlowFunction_9()) {
      isDirty_pushFlowFunction_9 = pushFlowFunction_9.push();
    }
    if (guardCheck_pushFlowFunction_10()) {
      isDirty_pushFlowFunction_10 = pushFlowFunction_10.push();
    }
    if (guardCheck_pushFlowFunction_12()) {
      isDirty_pushFlowFunction_12 = pushFlowFunction_12.push();
    }
    if (guardCheck_pushFlowFunction_13()) {
      isDirty_pushFlowFunction_13 = pushFlowFunction_13.push();
    }
    if (guardCheck_pushFlowFunction_15()) {
      isDirty_pushFlowFunction_15 = pushFlowFunction_15.push();
    }
    if (guardCheck_pushFlowFunction_16()) {
      isDirty_pushFlowFunction_16 = pushFlowFunction_16.push();
    }
    afterEvent();
  }
  // EVENT BUFFERING - END

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
    auditor.nodeRegistered(csvToHouseRecordSerializer_0, "csvToHouseRecordSerializer_0");
    auditor.nodeRegistered(houseRecordTransformer_3, "houseRecordTransformer_3");
    auditor.nodeRegistered(houseRecordValidator_5, "houseRecordValidator_5");
    auditor.nodeRegistered(invalidLogWriter_47, "invalidLogWriter_47");
    auditor.nodeRegistered(postProcessBinaryWriter_35, "postProcessBinaryWriter_35");
    auditor.nodeRegistered(postProcessCsvWriter_31, "postProcessCsvWriter_31");
    auditor.nodeRegistered(processingStats_51, "processingStats_51");
    auditor.nodeRegistered(callbackDispatcher, "callbackDispatcher");
    auditor.nodeRegistered(filterFlowFunction_11, "filterFlowFunction_11");
    auditor.nodeRegistered(filterFlowFunction_14, "filterFlowFunction_14");
    auditor.nodeRegistered(mapRef2RefFlowFunction_1, "mapRef2RefFlowFunction_1");
    auditor.nodeRegistered(mapRef2RefFlowFunction_2, "mapRef2RefFlowFunction_2");
    auditor.nodeRegistered(mapRef2RefFlowFunction_4, "mapRef2RefFlowFunction_4");
    auditor.nodeRegistered(mapRef2RefFlowFunction_6, "mapRef2RefFlowFunction_6");
    auditor.nodeRegistered(mapRef2RefFlowFunction_7, "mapRef2RefFlowFunction_7");
    auditor.nodeRegistered(pushFlowFunction_8, "pushFlowFunction_8");
    auditor.nodeRegistered(pushFlowFunction_9, "pushFlowFunction_9");
    auditor.nodeRegistered(pushFlowFunction_10, "pushFlowFunction_10");
    auditor.nodeRegistered(pushFlowFunction_12, "pushFlowFunction_12");
    auditor.nodeRegistered(pushFlowFunction_13, "pushFlowFunction_13");
    auditor.nodeRegistered(pushFlowFunction_15, "pushFlowFunction_15");
    auditor.nodeRegistered(pushFlowFunction_16, "pushFlowFunction_16");
    auditor.nodeRegistered(subscriptionManager, "subscriptionManager");
    auditor.nodeRegistered(handlerString, "handlerString");
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
    isDirty_clock = false;
    isDirty_filterFlowFunction_11 = false;
    isDirty_filterFlowFunction_14 = false;
    isDirty_handlerString = false;
    isDirty_mapRef2RefFlowFunction_1 = false;
    isDirty_mapRef2RefFlowFunction_2 = false;
    isDirty_mapRef2RefFlowFunction_4 = false;
    isDirty_mapRef2RefFlowFunction_6 = false;
    isDirty_mapRef2RefFlowFunction_7 = false;
    isDirty_pushFlowFunction_8 = false;
    isDirty_pushFlowFunction_9 = false;
    isDirty_pushFlowFunction_10 = false;
    isDirty_pushFlowFunction_12 = false;
    isDirty_pushFlowFunction_13 = false;
    isDirty_pushFlowFunction_15 = false;
    isDirty_pushFlowFunction_16 = false;
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
      dirtyFlagSupplierMap.put(clock, () -> isDirty_clock);
      dirtyFlagSupplierMap.put(filterFlowFunction_11, () -> isDirty_filterFlowFunction_11);
      dirtyFlagSupplierMap.put(filterFlowFunction_14, () -> isDirty_filterFlowFunction_14);
      dirtyFlagSupplierMap.put(handlerString, () -> isDirty_handlerString);
      dirtyFlagSupplierMap.put(mapRef2RefFlowFunction_1, () -> isDirty_mapRef2RefFlowFunction_1);
      dirtyFlagSupplierMap.put(mapRef2RefFlowFunction_2, () -> isDirty_mapRef2RefFlowFunction_2);
      dirtyFlagSupplierMap.put(mapRef2RefFlowFunction_4, () -> isDirty_mapRef2RefFlowFunction_4);
      dirtyFlagSupplierMap.put(mapRef2RefFlowFunction_6, () -> isDirty_mapRef2RefFlowFunction_6);
      dirtyFlagSupplierMap.put(mapRef2RefFlowFunction_7, () -> isDirty_mapRef2RefFlowFunction_7);
      dirtyFlagSupplierMap.put(pushFlowFunction_10, () -> isDirty_pushFlowFunction_10);
      dirtyFlagSupplierMap.put(pushFlowFunction_12, () -> isDirty_pushFlowFunction_12);
      dirtyFlagSupplierMap.put(pushFlowFunction_13, () -> isDirty_pushFlowFunction_13);
      dirtyFlagSupplierMap.put(pushFlowFunction_15, () -> isDirty_pushFlowFunction_15);
      dirtyFlagSupplierMap.put(pushFlowFunction_16, () -> isDirty_pushFlowFunction_16);
      dirtyFlagSupplierMap.put(pushFlowFunction_8, () -> isDirty_pushFlowFunction_8);
      dirtyFlagSupplierMap.put(pushFlowFunction_9, () -> isDirty_pushFlowFunction_9);
    }
    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(clock, (b) -> isDirty_clock = b);
      dirtyFlagUpdateMap.put(filterFlowFunction_11, (b) -> isDirty_filterFlowFunction_11 = b);
      dirtyFlagUpdateMap.put(filterFlowFunction_14, (b) -> isDirty_filterFlowFunction_14 = b);
      dirtyFlagUpdateMap.put(handlerString, (b) -> isDirty_handlerString = b);
      dirtyFlagUpdateMap.put(mapRef2RefFlowFunction_1, (b) -> isDirty_mapRef2RefFlowFunction_1 = b);
      dirtyFlagUpdateMap.put(mapRef2RefFlowFunction_2, (b) -> isDirty_mapRef2RefFlowFunction_2 = b);
      dirtyFlagUpdateMap.put(mapRef2RefFlowFunction_4, (b) -> isDirty_mapRef2RefFlowFunction_4 = b);
      dirtyFlagUpdateMap.put(mapRef2RefFlowFunction_6, (b) -> isDirty_mapRef2RefFlowFunction_6 = b);
      dirtyFlagUpdateMap.put(mapRef2RefFlowFunction_7, (b) -> isDirty_mapRef2RefFlowFunction_7 = b);
      dirtyFlagUpdateMap.put(pushFlowFunction_10, (b) -> isDirty_pushFlowFunction_10 = b);
      dirtyFlagUpdateMap.put(pushFlowFunction_12, (b) -> isDirty_pushFlowFunction_12 = b);
      dirtyFlagUpdateMap.put(pushFlowFunction_13, (b) -> isDirty_pushFlowFunction_13 = b);
      dirtyFlagUpdateMap.put(pushFlowFunction_15, (b) -> isDirty_pushFlowFunction_15 = b);
      dirtyFlagUpdateMap.put(pushFlowFunction_16, (b) -> isDirty_pushFlowFunction_16 = b);
      dirtyFlagUpdateMap.put(pushFlowFunction_8, (b) -> isDirty_pushFlowFunction_8 = b);
      dirtyFlagUpdateMap.put(pushFlowFunction_9, (b) -> isDirty_pushFlowFunction_9 = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_invalidLogWriter_47() {
    return isDirty_pushFlowFunction_12 | isDirty_pushFlowFunction_15;
  }

  private boolean guardCheck_postProcessBinaryWriter_35() {
    return isDirty_pushFlowFunction_10;
  }

  private boolean guardCheck_postProcessCsvWriter_31() {
    return isDirty_pushFlowFunction_9;
  }

  private boolean guardCheck_processingStats_51() {
    return isDirty_pushFlowFunction_8 | isDirty_pushFlowFunction_13 | isDirty_pushFlowFunction_16;
  }

  private boolean guardCheck_filterFlowFunction_11() {
    return isDirty_mapRef2RefFlowFunction_1;
  }

  private boolean guardCheck_filterFlowFunction_14() {
    return isDirty_mapRef2RefFlowFunction_6;
  }

  private boolean guardCheck_mapRef2RefFlowFunction_1() {
    return isDirty_handlerString;
  }

  private boolean guardCheck_mapRef2RefFlowFunction_2() {
    return isDirty_mapRef2RefFlowFunction_1;
  }

  private boolean guardCheck_mapRef2RefFlowFunction_4() {
    return isDirty_mapRef2RefFlowFunction_2;
  }

  private boolean guardCheck_mapRef2RefFlowFunction_6() {
    return isDirty_mapRef2RefFlowFunction_4;
  }

  private boolean guardCheck_mapRef2RefFlowFunction_7() {
    return isDirty_mapRef2RefFlowFunction_6;
  }

  private boolean guardCheck_pushFlowFunction_8() {
    return isDirty_mapRef2RefFlowFunction_7;
  }

  private boolean guardCheck_pushFlowFunction_9() {
    return isDirty_mapRef2RefFlowFunction_7;
  }

  private boolean guardCheck_pushFlowFunction_10() {
    return isDirty_mapRef2RefFlowFunction_7;
  }

  private boolean guardCheck_pushFlowFunction_12() {
    return isDirty_filterFlowFunction_11;
  }

  private boolean guardCheck_pushFlowFunction_13() {
    return isDirty_filterFlowFunction_11;
  }

  private boolean guardCheck_pushFlowFunction_15() {
    return isDirty_filterFlowFunction_14;
  }

  private boolean guardCheck_pushFlowFunction_16() {
    return isDirty_filterFlowFunction_14;
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
  public DataIngestionPipeline newInstance() {
    return new DataIngestionPipeline();
  }

  @Override
  public DataIngestionPipeline newInstance(Map<Object, Object> contextMap) {
    return new DataIngestionPipeline();
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
