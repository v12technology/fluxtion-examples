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
package com.fluxtion.example.aot.imperative.generator.sampleprocessor;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.event.DefaultFilteredEventHandler;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.stream.MapEventStream.MapInt2ToIntEventStream;
import com.fluxtion.runtime.stream.MapEventStream.MapRef2ToIntEventStream;
import com.fluxtion.runtime.stream.PeekEventStream.IntPeekEventStream;
import com.fluxtion.runtime.stream.aggregate.functions.AggregateIntSum;
import com.fluxtion.runtime.stream.helpers.Peekers.TemplateMessage;
import com.fluxtion.runtime.time.Clock;
import com.fluxtion.runtime.time.ClockStrategy.ClockStrategyEvent;

/*
 * <pre>
 * generation time   : 2022-06-14T16:27:39.296052100
 * eventProcessorGenerator version : 6.0.2-SNAPSHOT
 * api version       : 6.0.2-SNAPSHOT
 * </pre>
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public class Processor implements EventProcessor, StaticEventProcessor, BatchHandler, Lifecycle {

  //Node declarations
  private final AggregateIntSum aggregateIntSum_1 = new AggregateIntSum();
  public final Clock clock = new Clock();
  private final DefaultFilteredEventHandler handlerString =
      new DefaultFilteredEventHandler<>(2147483647, "", String.class);
  private final MapRef2ToIntEventStream mapRef2ToIntEventStream_0 =
      new MapRef2ToIntEventStream<>(handlerString, Integer::decode);
  private final MapInt2ToIntEventStream mapInt2ToIntEventStream_2 =
      new MapInt2ToIntEventStream(mapRef2ToIntEventStream_0, aggregateIntSum_1::aggregateInt);
  private final TemplateMessage templateMessage_3 = new TemplateMessage<>("cumSum -> {}");
  private final IntPeekEventStream intPeekEventStream_4 =
      new IntPeekEventStream(mapInt2ToIntEventStream_2, templateMessage_3::templateAndLogToConsole);
  public final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  //Dirty flags
  private boolean processing = false;
  private boolean isDirty_handlerString = false;
  private boolean isDirty_mapInt2ToIntEventStream_2 = false;
  private boolean isDirty_mapRef2ToIntEventStream_0 = false;
  //Filter constants

  public Processor() {
    templateMessage_3.clock = clock;
    //node auditors
    initialiseAuditor(clock);
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
      case ("com.fluxtion.runtime.time.ClockStrategy$ClockStrategyEvent"):
        {
          ClockStrategyEvent typedEvent = (ClockStrategyEvent) event;
          handleEvent(typedEvent);
          break;
        }
      case ("java.lang.String"):
        {
          String typedEvent = (String) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(ClockStrategyEvent typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    clock.setClockStrategy(typedEvent);
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(String typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerString = true;
    handlerString.onEvent(typedEvent);
    if (isDirty_handlerString) {
      mapRef2ToIntEventStream_0.inputUpdated(handlerString);
    }
    if (isDirty_handlerString) {
      isDirty_mapRef2ToIntEventStream_0 = mapRef2ToIntEventStream_0.map();
      if (isDirty_mapRef2ToIntEventStream_0) {
        mapInt2ToIntEventStream_2.inputUpdated(mapRef2ToIntEventStream_0);
      }
    }
    if (isDirty_mapRef2ToIntEventStream_0) {
      isDirty_mapInt2ToIntEventStream_2 = mapInt2ToIntEventStream_2.map();
      if (isDirty_mapInt2ToIntEventStream_2) {
        intPeekEventStream_4.inputUpdated(mapInt2ToIntEventStream_2);
      }
    }
    if (isDirty_mapInt2ToIntEventStream_2) {
      intPeekEventStream_4.peek();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  private void auditEvent(Object typedEvent) {
    clock.eventReceived(typedEvent);
    callbackDispatcher.eventReceived(typedEvent);
  }

  private void auditEvent(Event typedEvent) {
    clock.eventReceived(typedEvent);
    callbackDispatcher.eventReceived(typedEvent);
  }

  private void initialiseAuditor(Auditor auditor) {
    auditor.init();
    auditor.nodeRegistered(handlerString, "handlerString");
    auditor.nodeRegistered(mapInt2ToIntEventStream_2, "mapInt2ToIntEventStream_2");
    auditor.nodeRegistered(mapRef2ToIntEventStream_0, "mapRef2ToIntEventStream_0");
    auditor.nodeRegistered(intPeekEventStream_4, "intPeekEventStream_4");
    auditor.nodeRegistered(aggregateIntSum_1, "aggregateIntSum_1");
    auditor.nodeRegistered(templateMessage_3, "templateMessage_3");
  }

  private void afterEvent() {
    clock.processingComplete();
    callbackDispatcher.processingComplete();
    isDirty_handlerString = false;
    isDirty_mapInt2ToIntEventStream_2 = false;
    isDirty_mapRef2ToIntEventStream_0 = false;
  }

  @Override
  public void init() {
    clock.init();
    mapRef2ToIntEventStream_0.initialiseEventStream();
    mapInt2ToIntEventStream_2.initialiseEventStream();
    intPeekEventStream_4.initialiseEventStream();
  }

  @Override
  public void tearDown() {
    callbackDispatcher.tearDown();
    clock.tearDown();
  }

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
