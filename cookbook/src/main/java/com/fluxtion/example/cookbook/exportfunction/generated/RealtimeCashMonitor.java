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
package com.fluxtion.example.cookbook.exportfunction.generated;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.example.cookbook.exportfunction.CashMonitor;
import com.fluxtion.example.cookbook.exportfunction.events.FxRate;
import com.fluxtion.example.cookbook.exportfunction.nodes.BankAlert;
import com.fluxtion.example.cookbook.exportfunction.nodes.SalesTracker;
import com.fluxtion.example.cookbook.exportfunction.nodes.StockTracker;
import com.fluxtion.example.cookbook.exportfunction.nodes.TradingPosition;
import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.audit.EventLogManager;
import com.fluxtion.runtime.audit.NodeNameAuditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.callback.ExportFunctionTrigger;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_0;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_1;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_2;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_3;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_4;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_5;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.input.EventFeed;
import com.fluxtion.runtime.input.SubscriptionManagerNode;
import com.fluxtion.runtime.node.ForkedTriggerTask;
import com.fluxtion.runtime.node.MutableEventProcessorContext;
import com.fluxtion.runtime.node.MutableEventProcessorContext;
import java.util.Arrays;
import java.util.Map;

import java.util.IdentityHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 *
 *
 * <pre>
 * generation time                 : 2023-06-12T12:09:45.729551
 * eventProcessorGenerator version : 9.0.8
 * api version                     : 9.0.8
 * </pre>
 *
 * Event classes supported:
 *
 * <ul>
 *   <li>com.fluxtion.example.cookbook.exportfunction.events.FxRate
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_0
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_1
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_2
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_3
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_4
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_5
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public class RealtimeCashMonitor
    implements EventProcessor<RealtimeCashMonitor>,
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle,
        CashMonitor {

  //Node declarations
  private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  public final NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final SalesTracker salesTracker_3 = new SalesTracker();
  private final StockTracker stockTracker_2 = new StockTracker();
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_2 =
      new ExportFunctionTrigger(ExportFunctionTriggerEvent_2.class);
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_3 =
      new ExportFunctionTrigger(ExportFunctionTriggerEvent_3.class);
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_4 =
      new ExportFunctionTrigger(ExportFunctionTriggerEvent_4.class);
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_5 =
      new ExportFunctionTrigger(ExportFunctionTriggerEvent_5.class);
  private final SubscriptionManagerNode subscriptionManager = new SubscriptionManagerNode();
  private final MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  private final TradingPosition tradingPosition_1 =
      new TradingPosition(stockTracker_2, salesTracker_3);
  private final BankAlert bankAlert_0 = new BankAlert(tradingPosition_1);
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_0 =
      new ExportFunctionTrigger(ExportFunctionTriggerEvent_0.class);
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_1 =
      new ExportFunctionTrigger(ExportFunctionTriggerEvent_1.class);
  //Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(9);
  private final IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(9);

  private boolean isDirty_handlerExportFunctionTriggerEvent_0 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_1 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_2 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_3 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_4 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_5 = false;
  private boolean isDirty_salesTracker_3 = false;
  private boolean isDirty_stockTracker_2 = false;
  private boolean isDirty_tradingPosition_1 = false;
  //Forked declarations

  //Filter constants

  public RealtimeCashMonitor(Map<Object, Object> contextMap) {
    context.replaceMappings(contextMap);
    salesTracker_3.setTriggered(false);
    stockTracker_2.setTriggered(false);
    tradingPosition_1.setTriggered(false);
    handlerExportFunctionTriggerEvent_0.setFunctionPointerList(
        Arrays.asList(tradingPosition_1, tradingPosition_1));
    handlerExportFunctionTriggerEvent_1.setFunctionPointerList(
        Arrays.asList(tradingPosition_1, tradingPosition_1));
    handlerExportFunctionTriggerEvent_2.setFunctionPointerList(
        Arrays.asList(stockTracker_2, stockTracker_2));
    handlerExportFunctionTriggerEvent_3.setFunctionPointerList(
        Arrays.asList(stockTracker_2, stockTracker_2, salesTracker_3, salesTracker_3));
    handlerExportFunctionTriggerEvent_4.setFunctionPointerList(
        Arrays.asList(stockTracker_2, stockTracker_2));
    handlerExportFunctionTriggerEvent_5.setFunctionPointerList(
        Arrays.asList(stockTracker_2, stockTracker_2));
    //node auditors
    initialiseAuditor(nodeNameLookup);
    subscriptionManager.setSubscribingEventProcessor(this);
    context.setEventProcessorCallback(this);
  }

  public RealtimeCashMonitor() {
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
    if (event instanceof com.fluxtion.example.cookbook.exportfunction.events.FxRate) {
      FxRate typedEvent = (FxRate) event;
      handleEvent(typedEvent);
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_0) {
      ExportFunctionTriggerEvent_0 typedEvent = (ExportFunctionTriggerEvent_0) event;
      handleEvent(typedEvent);
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_1) {
      ExportFunctionTriggerEvent_1 typedEvent = (ExportFunctionTriggerEvent_1) event;
      handleEvent(typedEvent);
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_2) {
      ExportFunctionTriggerEvent_2 typedEvent = (ExportFunctionTriggerEvent_2) event;
      handleEvent(typedEvent);
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_3) {
      ExportFunctionTriggerEvent_3 typedEvent = (ExportFunctionTriggerEvent_3) event;
      handleEvent(typedEvent);
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_4) {
      ExportFunctionTriggerEvent_4 typedEvent = (ExportFunctionTriggerEvent_4) event;
      handleEvent(typedEvent);
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_5) {
      ExportFunctionTriggerEvent_5 typedEvent = (ExportFunctionTriggerEvent_5) event;
      handleEvent(typedEvent);
    }
  }

  public void addCash(java.lang.String arg0, java.util.Date arg1, double arg2) {
    tradingPosition_1.setTriggered(tradingPosition_1.addCash(arg0, arg1, arg2));
    onEvent(handlerExportFunctionTriggerEvent_0.getEvent());
  }

  public void electronicStockUpdate(
      com.fluxtion.example.cookbook.exportfunction.data.StockDelivery<
              com.fluxtion.example.cookbook.exportfunction.data.Electronic>
          arg0) {
    stockTracker_2.setTriggered(stockTracker_2.addStockElectronic(arg0));
    onEvent(handlerExportFunctionTriggerEvent_4.getEvent());
  }

  public void foodStockUpdate(
      com.fluxtion.example.cookbook.exportfunction.data.StockDelivery<
              com.fluxtion.example.cookbook.exportfunction.data.Food>
          arg0) {
    stockTracker_2.setTriggered(stockTracker_2.addStock(arg0));
    onEvent(handlerExportFunctionTriggerEvent_2.getEvent());
  }

  public void furnitureStockUpdate(
      com.fluxtion.example.cookbook.exportfunction.data.StockDelivery<
              com.fluxtion.example.cookbook.exportfunction.data.Furniture>
          arg0) {
    stockTracker_2.setTriggered(stockTracker_2.addStockFurniture(arg0));
    onEvent(handlerExportFunctionTriggerEvent_5.getEvent());
  }

  public void payBill(java.lang.String arg0, java.util.Date arg1, double arg2) {
    tradingPosition_1.setTriggered(tradingPosition_1.payBill(arg0, arg1, arg2));
    onEvent(handlerExportFunctionTriggerEvent_1.getEvent());
  }

  public void saleUpdate(java.lang.String arg0, int arg1, double arg2) {
    stockTracker_2.setTriggered(stockTracker_2.updateStockLevels(arg0, arg1, arg2));
    salesTracker_3.setTriggered(salesTracker_3.updateSalesIncome(arg0, arg1, arg2));
    onEvent(handlerExportFunctionTriggerEvent_3.getEvent());
  }

  public void handleEvent(FxRate typedEvent) {
    auditEvent(typedEvent);
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.example.cookbook.exportfunction.events.FxRate] filterString:[GBPUSD]
      case ("GBPUSD"):
        isDirty_salesTracker_3 = salesTracker_3.fxChange(typedEvent);
        if (isDirty_salesTracker_3) {
          tradingPosition_1.salesChange(salesTracker_3);
        }
        isDirty_stockTracker_2 = stockTracker_2.fxChange(typedEvent);
        if (isDirty_stockTracker_2) {
          tradingPosition_1.stockChange(stockTracker_2);
        }
        if (guardCheck_tradingPosition_1()) {
          isDirty_tradingPosition_1 = tradingPosition_1.triggered();
        }
        if (guardCheck_bankAlert_0()) {
          bankAlert_0.checkTradePosition();
        }
        afterEvent();
        return;
    }
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_0 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_0 =
        handlerExportFunctionTriggerEvent_0.onEvent(typedEvent);
    if (guardCheck_tradingPosition_1()) {
      isDirty_tradingPosition_1 = tradingPosition_1.triggered();
    }
    if (guardCheck_bankAlert_0()) {
      bankAlert_0.checkTradePosition();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_1 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_1 =
        handlerExportFunctionTriggerEvent_1.onEvent(typedEvent);
    if (guardCheck_tradingPosition_1()) {
      isDirty_tradingPosition_1 = tradingPosition_1.triggered();
    }
    if (guardCheck_bankAlert_0()) {
      bankAlert_0.checkTradePosition();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_2 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_2 =
        handlerExportFunctionTriggerEvent_2.onEvent(typedEvent);
    if (guardCheck_stockTracker_2()) {
      isDirty_stockTracker_2 = stockTracker_2.triggered();
      if (isDirty_stockTracker_2) {
        tradingPosition_1.stockChange(stockTracker_2);
      }
    }
    if (guardCheck_tradingPosition_1()) {
      isDirty_tradingPosition_1 = tradingPosition_1.triggered();
    }
    if (guardCheck_bankAlert_0()) {
      bankAlert_0.checkTradePosition();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_3 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_3 =
        handlerExportFunctionTriggerEvent_3.onEvent(typedEvent);
    if (guardCheck_salesTracker_3()) {
      isDirty_salesTracker_3 = salesTracker_3.triggered();
      if (isDirty_salesTracker_3) {
        tradingPosition_1.salesChange(salesTracker_3);
      }
    }
    if (guardCheck_stockTracker_2()) {
      isDirty_stockTracker_2 = stockTracker_2.triggered();
      if (isDirty_stockTracker_2) {
        tradingPosition_1.stockChange(stockTracker_2);
      }
    }
    if (guardCheck_tradingPosition_1()) {
      isDirty_tradingPosition_1 = tradingPosition_1.triggered();
    }
    if (guardCheck_bankAlert_0()) {
      bankAlert_0.checkTradePosition();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_4 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_4 =
        handlerExportFunctionTriggerEvent_4.onEvent(typedEvent);
    if (guardCheck_stockTracker_2()) {
      isDirty_stockTracker_2 = stockTracker_2.triggered();
      if (isDirty_stockTracker_2) {
        tradingPosition_1.stockChange(stockTracker_2);
      }
    }
    if (guardCheck_tradingPosition_1()) {
      isDirty_tradingPosition_1 = tradingPosition_1.triggered();
    }
    if (guardCheck_bankAlert_0()) {
      bankAlert_0.checkTradePosition();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_5 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_5 =
        handlerExportFunctionTriggerEvent_5.onEvent(typedEvent);
    if (guardCheck_stockTracker_2()) {
      isDirty_stockTracker_2 = stockTracker_2.triggered();
      if (isDirty_stockTracker_2) {
        tradingPosition_1.stockChange(stockTracker_2);
      }
    }
    if (guardCheck_tradingPosition_1()) {
      isDirty_tradingPosition_1 = tradingPosition_1.triggered();
    }
    if (guardCheck_bankAlert_0()) {
      bankAlert_0.checkTradePosition();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void bufferEvent(Object event) {
    buffering = true;
    if (event instanceof com.fluxtion.example.cookbook.exportfunction.events.FxRate) {
      FxRate typedEvent = (FxRate) event;
      auditEvent(typedEvent);
      switch (typedEvent.filterString()) {
          //Event Class:[com.fluxtion.example.cookbook.exportfunction.events.FxRate] filterString:[GBPUSD]
        case ("GBPUSD"):
          isDirty_salesTracker_3 = salesTracker_3.fxChange(typedEvent);
          if (isDirty_salesTracker_3) {
            tradingPosition_1.salesChange(salesTracker_3);
          }
          isDirty_stockTracker_2 = stockTracker_2.fxChange(typedEvent);
          if (isDirty_stockTracker_2) {
            tradingPosition_1.stockChange(stockTracker_2);
          }
          //event stack unwind callbacks
          afterEvent();
          return;
      }
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_0) {
      ExportFunctionTriggerEvent_0 typedEvent = (ExportFunctionTriggerEvent_0) event;
      auditEvent(typedEvent);
      isDirty_handlerExportFunctionTriggerEvent_0 =
          handlerExportFunctionTriggerEvent_0.onEvent(typedEvent);
      //event stack unwind callbacks
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_1) {
      ExportFunctionTriggerEvent_1 typedEvent = (ExportFunctionTriggerEvent_1) event;
      auditEvent(typedEvent);
      isDirty_handlerExportFunctionTriggerEvent_1 =
          handlerExportFunctionTriggerEvent_1.onEvent(typedEvent);
      //event stack unwind callbacks
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_2) {
      ExportFunctionTriggerEvent_2 typedEvent = (ExportFunctionTriggerEvent_2) event;
      auditEvent(typedEvent);
      isDirty_handlerExportFunctionTriggerEvent_2 =
          handlerExportFunctionTriggerEvent_2.onEvent(typedEvent);
      //event stack unwind callbacks
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_3) {
      ExportFunctionTriggerEvent_3 typedEvent = (ExportFunctionTriggerEvent_3) event;
      auditEvent(typedEvent);
      isDirty_handlerExportFunctionTriggerEvent_3 =
          handlerExportFunctionTriggerEvent_3.onEvent(typedEvent);
      //event stack unwind callbacks
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_4) {
      ExportFunctionTriggerEvent_4 typedEvent = (ExportFunctionTriggerEvent_4) event;
      auditEvent(typedEvent);
      isDirty_handlerExportFunctionTriggerEvent_4 =
          handlerExportFunctionTriggerEvent_4.onEvent(typedEvent);
      //event stack unwind callbacks
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_5) {
      ExportFunctionTriggerEvent_5 typedEvent = (ExportFunctionTriggerEvent_5) event;
      auditEvent(typedEvent);
      isDirty_handlerExportFunctionTriggerEvent_5 =
          handlerExportFunctionTriggerEvent_5.onEvent(typedEvent);
      //event stack unwind callbacks
    }
  }

  public void triggerCalculation() {
    buffering = false;
    String typedEvent = "No event information - buffered dispatch";
    if (guardCheck_salesTracker_3()) {
      isDirty_salesTracker_3 = salesTracker_3.triggered();
      if (isDirty_salesTracker_3) {
        tradingPosition_1.salesChange(salesTracker_3);
      }
    }
    if (guardCheck_stockTracker_2()) {
      isDirty_stockTracker_2 = stockTracker_2.triggered();
      if (isDirty_stockTracker_2) {
        tradingPosition_1.stockChange(stockTracker_2);
      }
    }
    if (guardCheck_tradingPosition_1()) {
      isDirty_tradingPosition_1 = tradingPosition_1.triggered();
    }
    if (guardCheck_bankAlert_0()) {
      bankAlert_0.checkTradePosition();
    }
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
    auditor.nodeRegistered(bankAlert_0, "bankAlert_0");
    auditor.nodeRegistered(salesTracker_3, "salesTracker_3");
    auditor.nodeRegistered(stockTracker_2, "stockTracker_2");
    auditor.nodeRegistered(tradingPosition_1, "tradingPosition_1");
    auditor.nodeRegistered(callbackDispatcher, "callbackDispatcher");
    auditor.nodeRegistered(
        handlerExportFunctionTriggerEvent_0, "handlerExportFunctionTriggerEvent_0");
    auditor.nodeRegistered(
        handlerExportFunctionTriggerEvent_1, "handlerExportFunctionTriggerEvent_1");
    auditor.nodeRegistered(
        handlerExportFunctionTriggerEvent_2, "handlerExportFunctionTriggerEvent_2");
    auditor.nodeRegistered(
        handlerExportFunctionTriggerEvent_3, "handlerExportFunctionTriggerEvent_3");
    auditor.nodeRegistered(
        handlerExportFunctionTriggerEvent_4, "handlerExportFunctionTriggerEvent_4");
    auditor.nodeRegistered(
        handlerExportFunctionTriggerEvent_5, "handlerExportFunctionTriggerEvent_5");
    auditor.nodeRegistered(subscriptionManager, "subscriptionManager");
    auditor.nodeRegistered(context, "context");
  }

  private void afterEvent() {
    tradingPosition_1.afterEvent();
    stockTracker_2.afterEvent();
    salesTracker_3.afterEvent();

    nodeNameLookup.processingComplete();
    isDirty_handlerExportFunctionTriggerEvent_0 = false;
    isDirty_handlerExportFunctionTriggerEvent_1 = false;
    isDirty_handlerExportFunctionTriggerEvent_2 = false;
    isDirty_handlerExportFunctionTriggerEvent_3 = false;
    isDirty_handlerExportFunctionTriggerEvent_4 = false;
    isDirty_handlerExportFunctionTriggerEvent_5 = false;
    isDirty_salesTracker_3 = false;
    isDirty_stockTracker_2 = false;
    isDirty_tradingPosition_1 = false;
  }

  @Override
  public void init() {
    initCalled = true;
    auditEvent(Lifecycle.LifecycleEvent.Init);
    //initialise dirty lookup map
    isDirty("test");
    salesTracker_3.init();
    stockTracker_2.init();
    tradingPosition_1.init();
    afterEvent();
  }

  @Override
  public void start() {
    if (!initCalled) {
      throw new RuntimeException("init() must be called before start()");
    }
    processing = true;
    auditEvent(Lifecycle.LifecycleEvent.Start);
    salesTracker_3.start();
    stockTracker_2.start();
    tradingPosition_1.start();
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
    subscriptionManager.tearDown();
    afterEvent();
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

  public BooleanSupplier dirtySupplier(Object node) {
    if (dirtyFlagSupplierMap.isEmpty()) {
      dirtyFlagSupplierMap.put(
          handlerExportFunctionTriggerEvent_0, () -> isDirty_handlerExportFunctionTriggerEvent_0);
      dirtyFlagSupplierMap.put(
          handlerExportFunctionTriggerEvent_1, () -> isDirty_handlerExportFunctionTriggerEvent_1);
      dirtyFlagSupplierMap.put(
          handlerExportFunctionTriggerEvent_2, () -> isDirty_handlerExportFunctionTriggerEvent_2);
      dirtyFlagSupplierMap.put(
          handlerExportFunctionTriggerEvent_3, () -> isDirty_handlerExportFunctionTriggerEvent_3);
      dirtyFlagSupplierMap.put(
          handlerExportFunctionTriggerEvent_4, () -> isDirty_handlerExportFunctionTriggerEvent_4);
      dirtyFlagSupplierMap.put(
          handlerExportFunctionTriggerEvent_5, () -> isDirty_handlerExportFunctionTriggerEvent_5);
      dirtyFlagSupplierMap.put(salesTracker_3, () -> isDirty_salesTracker_3);
      dirtyFlagSupplierMap.put(stockTracker_2, () -> isDirty_stockTracker_2);
      dirtyFlagSupplierMap.put(tradingPosition_1, () -> isDirty_tradingPosition_1);
    }
    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(
          handlerExportFunctionTriggerEvent_0,
          (b) -> isDirty_handlerExportFunctionTriggerEvent_0 = b);
      dirtyFlagUpdateMap.put(
          handlerExportFunctionTriggerEvent_1,
          (b) -> isDirty_handlerExportFunctionTriggerEvent_1 = b);
      dirtyFlagUpdateMap.put(
          handlerExportFunctionTriggerEvent_2,
          (b) -> isDirty_handlerExportFunctionTriggerEvent_2 = b);
      dirtyFlagUpdateMap.put(
          handlerExportFunctionTriggerEvent_3,
          (b) -> isDirty_handlerExportFunctionTriggerEvent_3 = b);
      dirtyFlagUpdateMap.put(
          handlerExportFunctionTriggerEvent_4,
          (b) -> isDirty_handlerExportFunctionTriggerEvent_4 = b);
      dirtyFlagUpdateMap.put(
          handlerExportFunctionTriggerEvent_5,
          (b) -> isDirty_handlerExportFunctionTriggerEvent_5 = b);
      dirtyFlagUpdateMap.put(salesTracker_3, (b) -> isDirty_salesTracker_3 = b);
      dirtyFlagUpdateMap.put(stockTracker_2, (b) -> isDirty_stockTracker_2 = b);
      dirtyFlagUpdateMap.put(tradingPosition_1, (b) -> isDirty_tradingPosition_1 = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_bankAlert_0() {
    return isDirty_tradingPosition_1;
  }

  private boolean guardCheck_salesTracker_3() {
    return isDirty_handlerExportFunctionTriggerEvent_3;
  }

  private boolean guardCheck_stockTracker_2() {
    return isDirty_handlerExportFunctionTriggerEvent_2
        | isDirty_handlerExportFunctionTriggerEvent_3
        | isDirty_handlerExportFunctionTriggerEvent_4
        | isDirty_handlerExportFunctionTriggerEvent_5;
  }

  private boolean guardCheck_tradingPosition_1() {
    return isDirty_handlerExportFunctionTriggerEvent_0
        | isDirty_handlerExportFunctionTriggerEvent_1
        | isDirty_salesTracker_3
        | isDirty_stockTracker_2;
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
  public RealtimeCashMonitor newInstance() {
    return new RealtimeCashMonitor();
  }

  @Override
  public RealtimeCashMonitor newInstance(Map<Object, Object> contextMap) {
    return new RealtimeCashMonitor();
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
