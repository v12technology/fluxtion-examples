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
package com.fluxtion.example.cookbook.dataingestion.generated;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.example.cookbook.dataingestion.node.CsvRecordValidator;
import com.fluxtion.example.cookbook.dataingestion.node.InvalidLog;
import com.fluxtion.example.cookbook.dataingestion.node.ProcessingStats;
import com.fluxtion.example.cookbook.dataingestion.node.RecordBinaryWriter;
import com.fluxtion.example.cookbook.dataingestion.node.RecordCsvWriter;
import com.fluxtion.example.cookbook.dataingestion.node.RecordTransformer;
import com.fluxtion.example.cookbook.dataingestion.node.RecordValidator;
import com.fluxtion.runtime.EventProcessorContext;
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
import com.fluxtion.runtime.input.SubscriptionManagerNode;
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
 * eventProcessorGenerator version : 9.2.18
 * api version                     : 9.2.18
 * </pre>
 *
 * Event classes supported:
 *
 * <ul>
 *   <li>com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent
 *   <li>java.lang.String
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class DataIngestion
    implements EventProcessor<DataIngestion>,
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle {

  // Node declarations
  private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  private final CsvRecordValidator csvRecordValidator_0 = new CsvRecordValidator();
  private final InvalidLog invalidLog_56 = new InvalidLog();
  public final NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final ProcessingStats processingStats_55 = new ProcessingStats();
  private final RecordBinaryWriter recordBinaryWriter_36 = new RecordBinaryWriter();
  private final RecordCsvWriter recordCsvWriter_37 = new RecordCsvWriter();
  private final RecordTransformer recordTransformer_3 = new RecordTransformer();
  private final RecordValidator recordValidator_5 = new RecordValidator();
  private final SubscriptionManagerNode subscriptionManager = new SubscriptionManagerNode();
  private final MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  private final DefaultEventHandlerNode handlerString =
      new DefaultEventHandlerNode<>(
          2147483647, "", java.lang.String.class, "handlerString", context);
  private final MapRef2RefFlowFunction mapRef2RefFlowFunction_1 =
      new MapRef2RefFlowFunction<>(handlerString, csvRecordValidator_0::marshall);
  private final FilterFlowFunction filterFlowFunction_11 =
      new FilterFlowFunction<>(mapRef2RefFlowFunction_1, CsvRecordValidator::isInValidRecord);
  private final MapRef2RefFlowFunction mapRef2RefFlowFunction_2 =
      new MapRef2RefFlowFunction<>(
          mapRef2RefFlowFunction_1, CsvRecordValidator::getHouseInputRecord);
  private final MapRef2RefFlowFunction mapRef2RefFlowFunction_4 =
      new MapRef2RefFlowFunction<>(mapRef2RefFlowFunction_2, recordTransformer_3::transform);
  private final MapRef2RefFlowFunction mapRef2RefFlowFunction_6 =
      new MapRef2RefFlowFunction<>(mapRef2RefFlowFunction_4, recordValidator_5::validate);
  private final FilterFlowFunction filterFlowFunction_14 =
      new FilterFlowFunction<>(mapRef2RefFlowFunction_6, RecordValidator::isInValidRecord);
  private final MapRef2RefFlowFunction mapRef2RefFlowFunction_7 =
      new MapRef2RefFlowFunction<>(mapRef2RefFlowFunction_6, RecordValidator::getRecord);
  private final PushFlowFunction pushFlowFunction_8 =
      new PushFlowFunction<>(mapRef2RefFlowFunction_7, processingStats_55::validHousingRecord);
  private final PushFlowFunction pushFlowFunction_9 =
      new PushFlowFunction<>(pushFlowFunction_8, recordCsvWriter_37::validHousingRecord);
  private final PushFlowFunction pushFlowFunction_10 =
      new PushFlowFunction<>(pushFlowFunction_9, recordBinaryWriter_36::validHousingRecord);
  private final PushFlowFunction pushFlowFunction_12 =
      new PushFlowFunction<>(filterFlowFunction_11, invalidLog_56::badCsvInput);
  private final PushFlowFunction pushFlowFunction_13 =
      new PushFlowFunction<>(pushFlowFunction_12, processingStats_55::badCsvInput);
  private final PushFlowFunction pushFlowFunction_15 =
      new PushFlowFunction<>(filterFlowFunction_14, invalidLog_56::badHousingRecord);
  private final PushFlowFunction pushFlowFunction_16 =
      new PushFlowFunction<>(pushFlowFunction_15, processingStats_55::badHousingRecord);
  public final Clock clock = new Clock();
  private final ExportFunctionAuditEvent functionAudit = new ExportFunctionAuditEvent();
  // Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(15);
  private final IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(15);

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

  public DataIngestion(Map<Object, Object> contextMap) {
    context.replaceMappings(contextMap);
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
    // node auditors
    initialiseAuditor(clock);
    initialiseAuditor(nodeNameLookup);
    subscriptionManager.setSubscribingEventProcessor(this);
    context.setEventProcessorCallback(this);
  }

  public DataIngestion() {
    this(null);
  }

  @Override
  public void init() {
    initCalled = true;
    auditEvent(Lifecycle.LifecycleEvent.Init);
    // initialise dirty lookup map
    isDirty("test");
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
    handlerString.tearDown();
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
    if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      handleEvent(typedEvent);
    } else if (event instanceof java.lang.String) {
      String typedEvent = (String) event;
      handleEvent(typedEvent);
    }
  }

  public void handleEvent(ClockStrategyEvent typedEvent) {
    auditEvent(typedEvent);
    // Default, no filter methods
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
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_7()) {
      isDirty_mapRef2RefFlowFunction_7 = mapRef2RefFlowFunction_7.map();
      if (isDirty_mapRef2RefFlowFunction_7) {
        pushFlowFunction_8.inputUpdated(mapRef2RefFlowFunction_7);
      }
    }
    if (guardCheck_pushFlowFunction_8()) {
      isDirty_pushFlowFunction_8 = pushFlowFunction_8.push();
      if (isDirty_pushFlowFunction_8) {
        pushFlowFunction_9.inputUpdated(pushFlowFunction_8);
      }
    }
    if (guardCheck_pushFlowFunction_9()) {
      isDirty_pushFlowFunction_9 = pushFlowFunction_9.push();
      if (isDirty_pushFlowFunction_9) {
        pushFlowFunction_10.inputUpdated(pushFlowFunction_9);
      }
    }
    if (guardCheck_pushFlowFunction_10()) {
      isDirty_pushFlowFunction_10 = pushFlowFunction_10.push();
    }
    if (guardCheck_pushFlowFunction_12()) {
      isDirty_pushFlowFunction_12 = pushFlowFunction_12.push();
      if (isDirty_pushFlowFunction_12) {
        pushFlowFunction_13.inputUpdated(pushFlowFunction_12);
      }
    }
    if (guardCheck_pushFlowFunction_13()) {
      isDirty_pushFlowFunction_13 = pushFlowFunction_13.push();
    }
    if (guardCheck_pushFlowFunction_15()) {
      isDirty_pushFlowFunction_15 = pushFlowFunction_15.push();
      if (isDirty_pushFlowFunction_15) {
        pushFlowFunction_16.inputUpdated(pushFlowFunction_15);
      }
    }
    if (guardCheck_pushFlowFunction_16()) {
      isDirty_pushFlowFunction_16 = pushFlowFunction_16.push();
    }
    afterEvent();
  }
  // EVENT DISPATCH - END

  public void bufferEvent(Object event) {
    buffering = true;
    if (event instanceof com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent) {
      ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
      auditEvent(typedEvent);
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
      }
    }
    if (guardCheck_mapRef2RefFlowFunction_7()) {
      isDirty_mapRef2RefFlowFunction_7 = mapRef2RefFlowFunction_7.map();
      if (isDirty_mapRef2RefFlowFunction_7) {
        pushFlowFunction_8.inputUpdated(mapRef2RefFlowFunction_7);
      }
    }
    if (guardCheck_pushFlowFunction_8()) {
      isDirty_pushFlowFunction_8 = pushFlowFunction_8.push();
      if (isDirty_pushFlowFunction_8) {
        pushFlowFunction_9.inputUpdated(pushFlowFunction_8);
      }
    }
    if (guardCheck_pushFlowFunction_9()) {
      isDirty_pushFlowFunction_9 = pushFlowFunction_9.push();
      if (isDirty_pushFlowFunction_9) {
        pushFlowFunction_10.inputUpdated(pushFlowFunction_9);
      }
    }
    if (guardCheck_pushFlowFunction_10()) {
      isDirty_pushFlowFunction_10 = pushFlowFunction_10.push();
    }
    if (guardCheck_pushFlowFunction_12()) {
      isDirty_pushFlowFunction_12 = pushFlowFunction_12.push();
      if (isDirty_pushFlowFunction_12) {
        pushFlowFunction_13.inputUpdated(pushFlowFunction_12);
      }
    }
    if (guardCheck_pushFlowFunction_13()) {
      isDirty_pushFlowFunction_13 = pushFlowFunction_13.push();
    }
    if (guardCheck_pushFlowFunction_15()) {
      isDirty_pushFlowFunction_15 = pushFlowFunction_15.push();
      if (isDirty_pushFlowFunction_15) {
        pushFlowFunction_16.inputUpdated(pushFlowFunction_15);
      }
    }
    if (guardCheck_pushFlowFunction_16()) {
      isDirty_pushFlowFunction_16 = pushFlowFunction_16.push();
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
    auditor.nodeRegistered(csvRecordValidator_0, "csvRecordValidator_0");
    auditor.nodeRegistered(invalidLog_56, "invalidLog_56");
    auditor.nodeRegistered(processingStats_55, "processingStats_55");
    auditor.nodeRegistered(recordBinaryWriter_36, "recordBinaryWriter_36");
    auditor.nodeRegistered(recordCsvWriter_37, "recordCsvWriter_37");
    auditor.nodeRegistered(recordTransformer_3, "recordTransformer_3");
    auditor.nodeRegistered(recordValidator_5, "recordValidator_5");
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

  private boolean guardCheck_invalidLog_56() {
    return isDirty_pushFlowFunction_12 | isDirty_pushFlowFunction_15;
  }

  private boolean guardCheck_processingStats_55() {
    return isDirty_pushFlowFunction_8 | isDirty_pushFlowFunction_13 | isDirty_pushFlowFunction_16;
  }

  private boolean guardCheck_recordBinaryWriter_36() {
    return isDirty_pushFlowFunction_10;
  }

  private boolean guardCheck_recordCsvWriter_37() {
    return isDirty_pushFlowFunction_9;
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
    return isDirty_pushFlowFunction_8;
  }

  private boolean guardCheck_pushFlowFunction_10() {
    return isDirty_pushFlowFunction_9;
  }

  private boolean guardCheck_pushFlowFunction_12() {
    return isDirty_filterFlowFunction_11;
  }

  private boolean guardCheck_pushFlowFunction_13() {
    return isDirty_pushFlowFunction_12;
  }

  private boolean guardCheck_pushFlowFunction_15() {
    return isDirty_filterFlowFunction_14;
  }

  private boolean guardCheck_pushFlowFunction_16() {
    return isDirty_pushFlowFunction_15;
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
  public DataIngestion newInstance() {
    return new DataIngestion();
  }

  @Override
  public DataIngestion newInstance(Map<Object, Object> contextMap) {
    return new DataIngestion();
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
