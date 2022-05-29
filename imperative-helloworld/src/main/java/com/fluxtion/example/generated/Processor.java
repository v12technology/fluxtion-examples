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
package com.fluxtion.example.generated;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.example.Main.Data1;
import com.fluxtion.example.Main.Data1handler;
import com.fluxtion.example.Main.Data2;
import com.fluxtion.example.Main.Data2handler;
import com.fluxtion.example.Main.DataAddition;
import com.fluxtion.example.Main.SumLogger;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.event.Event;

/*
 * <pre>
 * generation time   : 2022-05-29T19:45:20.155133100
 * generator version : 5.0.8
 * api version       : 5.0.8
 * </pre>
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public class Processor implements EventProcessor, StaticEventProcessor, BatchHandler, Lifecycle {

  //Node declarations
  private final Data1handler data1handler_2 = new Data1handler();
  private final Data2handler data2handler_3 = new Data2handler();
  private final DataAddition dataAddition_1 = new DataAddition(data1handler_2, data2handler_3);
  private final SumLogger sumLogger_0 = new SumLogger(dataAddition_1);
  public final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  //Dirty flags
  private boolean processing = false;
  private boolean isDirty_data1handler_2 = false;
  private boolean isDirty_data2handler_3 = false;
  private boolean isDirty_dataAddition_1 = false;
  //Filter constants

  public Processor() {
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
      case ("com.fluxtion.example.Main$Data1"):
        {
          Data1 typedEvent = (Data1) event;
          handleEvent(typedEvent);
          break;
        }
      case ("com.fluxtion.example.Main$Data2"):
        {
          Data2 typedEvent = (Data2) event;
          handleEvent(typedEvent);
          break;
        }
    }
  }

  public void handleEvent(Data1 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_data1handler_2 = data1handler_2.data1Update(typedEvent);
    if (isDirty_data1handler_2 | isDirty_data2handler_3) {
      isDirty_dataAddition_1 = dataAddition_1.calculate();
    }
    if (isDirty_dataAddition_1) {
      sumLogger_0.printWarning();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(Data2 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_data2handler_3 = data2handler_3.data1Update(typedEvent);
    if (isDirty_data1handler_2 | isDirty_data2handler_3) {
      isDirty_dataAddition_1 = dataAddition_1.calculate();
    }
    if (isDirty_dataAddition_1) {
      sumLogger_0.printWarning();
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
    auditor.nodeRegistered(data1handler_2, "data1handler_2");
    auditor.nodeRegistered(data2handler_3, "data2handler_3");
    auditor.nodeRegistered(dataAddition_1, "dataAddition_1");
    auditor.nodeRegistered(sumLogger_0, "sumLogger_0");
  }

  private void afterEvent() {
    callbackDispatcher.processingComplete();
    isDirty_data1handler_2 = false;
    isDirty_data2handler_3 = false;
    isDirty_dataAddition_1 = false;
  }

  @Override
  public void init() {}

  @Override
  public void tearDown() {
    callbackDispatcher.tearDown();
  }

  @Override
  public void batchPause() {}

  @Override
  public void batchEnd() {}
}
