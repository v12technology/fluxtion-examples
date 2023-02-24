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
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
package com.fluxtion.example.aot.imperative.generator.myroot;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.example.aot.imperative.MyRootClass;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.event.DefaultFilteredEventHandler;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.stream.FilterEventStream;
import com.fluxtion.runtime.stream.MapEventStream.MapInt2ToIntEventStream;
import com.fluxtion.runtime.stream.MapEventStream.MapRef2ToIntEventStream;
import com.fluxtion.runtime.stream.WrappingEventSupplier.WrappingIntEventSupplier;
import com.fluxtion.runtime.stream.aggregate.functions.AggregateIntSum;
import com.fluxtion.runtime.stream.helpers.Mappers;
import com.fluxtion.runtime.stream.helpers.Predicates;

/*
 * <pre>
 * generation time   : 2022-06-14T16:27:40.908053400
 * eventProcessorGenerator version : 6.0.2-SNAPSHOT
 * api version       : 6.0.2-SNAPSHOT
 * </pre>
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public class Processor implements EventProcessor, StaticEventProcessor, BatchHandler, Lifecycle {

  //Node declarations
  private final AggregateIntSum aggregateIntSum_3 = new AggregateIntSum();
  private final DefaultFilteredEventHandler handlerString =
      new DefaultFilteredEventHandler<>(2147483647, "", String.class);
  private final FilterEventStream filterEventStream_1 =
      new FilterEventStream<>(handlerString, Predicates::isInteger);
  private final MapRef2ToIntEventStream mapRef2ToIntEventStream_2 =
      new MapRef2ToIntEventStream<>(filterEventStream_1, Mappers::parseInt);
  private final MapInt2ToIntEventStream mapInt2ToIntEventStream_4 =
      new MapInt2ToIntEventStream(mapRef2ToIntEventStream_2, aggregateIntSum_3::aggregateInt);
  private final WrappingIntEventSupplier wrappingIntEventSupplier_5 =
      new WrappingIntEventSupplier(mapInt2ToIntEventStream_4);
  public final MyRootClass myRoot = new MyRootClass();
  public final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  //Dirty flags
  private boolean processing = false;
  private boolean isDirty_filterEventStream_1 = false;
  private boolean isDirty_handlerString = false;
  private boolean isDirty_mapInt2ToIntEventStream_4 = false;
  private boolean isDirty_mapRef2ToIntEventStream_2 = false;
  private boolean isDirty_wrappingIntEventSupplier_5 = false;
  //Filter constants

  public Processor() {
    myRoot.intEventSupplier = wrappingIntEventSupplier_5;
    //node auditors
    initialiseAuditor(callbackDispatcher);
    //callback wiring
    callbackDispatcher.processor = this::onEventInternal;
  }

  @Override
  public void onEvent(Object event) {
    if (processing) {
      callbackDispatcher.processEvent(event);
    } else {
      processing = true;
      onEventInternal(event);
      callbackDispatcher.dispatchQueuedCallbacks();
      processing = false;
    }
  }

  private void onEventInternal(Object event) {
    switch (event.getClass().getName()) {
      case ("java.lang.String"):
        {
          String typedEvent = (String) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(String typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerString = true;
    handlerString.onEvent(typedEvent);
    if (isDirty_handlerString) {
      filterEventStream_1.inputUpdated(handlerString);
    }
    if (isDirty_handlerString) {
      isDirty_filterEventStream_1 = filterEventStream_1.filter();
      if (isDirty_filterEventStream_1) {
        mapRef2ToIntEventStream_2.inputUpdated(filterEventStream_1);
      }
    }
    if (isDirty_filterEventStream_1) {
      isDirty_mapRef2ToIntEventStream_2 = mapRef2ToIntEventStream_2.map();
      if (isDirty_mapRef2ToIntEventStream_2) {
        mapInt2ToIntEventStream_4.inputUpdated(mapRef2ToIntEventStream_2);
      }
    }
    if (isDirty_mapRef2ToIntEventStream_2) {
      isDirty_mapInt2ToIntEventStream_4 = mapInt2ToIntEventStream_4.map();
    }
    if (isDirty_mapInt2ToIntEventStream_4) {
      isDirty_wrappingIntEventSupplier_5 = wrappingIntEventSupplier_5.triggered();
    }
    myRoot.onString(typedEvent);
    if (isDirty_wrappingIntEventSupplier_5) {
      myRoot.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  private void auditEvent(Object typedEvent) {
    callbackDispatcher.eventReceived(typedEvent);
  }

  private void auditEvent(Event typedEvent) {
    callbackDispatcher.eventReceived(typedEvent);
  }

  private void initialiseAuditor(Auditor auditor) {
    auditor.init();
    auditor.nodeRegistered(myRoot, "myRoot");
    auditor.nodeRegistered(handlerString, "handlerString");
    auditor.nodeRegistered(filterEventStream_1, "filterEventStream_1");
    auditor.nodeRegistered(mapInt2ToIntEventStream_4, "mapInt2ToIntEventStream_4");
    auditor.nodeRegistered(mapRef2ToIntEventStream_2, "mapRef2ToIntEventStream_2");
    auditor.nodeRegistered(wrappingIntEventSupplier_5, "wrappingIntEventSupplier_5");
    auditor.nodeRegistered(aggregateIntSum_3, "aggregateIntSum_3");
  }

  private void afterEvent() {
    wrappingIntEventSupplier_5.afterEvent();
    callbackDispatcher.processingComplete();
    isDirty_filterEventStream_1 = false;
    isDirty_handlerString = false;
    isDirty_mapInt2ToIntEventStream_4 = false;
    isDirty_mapRef2ToIntEventStream_2 = false;
    isDirty_wrappingIntEventSupplier_5 = false;
  }

  @Override
  public void init() {
    filterEventStream_1.initialiseEventStream();
    mapRef2ToIntEventStream_2.initialiseEventStream();
    mapInt2ToIntEventStream_4.initialiseEventStream();
    wrappingIntEventSupplier_5.init();
  }

  @Override
  public void tearDown() {
    callbackDispatcher.tearDown();
  }

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
