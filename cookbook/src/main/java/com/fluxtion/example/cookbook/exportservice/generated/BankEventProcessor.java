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
package com.fluxtion.example.cookbook.exportservice.generated;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.example.cookbook.exportservice.node.BankAccountNode;
import com.fluxtion.example.cookbook.exportservice.node.SpendingMonitorNode;
import com.fluxtion.example.cookbook.exportservice.node.StatementPublisherNode;
import com.fluxtion.example.cookbook.exportservice.node.TransactionStore;
import com.fluxtion.example.cookbook.exportservice.service.BankAccount;
import com.fluxtion.example.cookbook.exportservice.service.ReplaySink;
import com.fluxtion.example.cookbook.exportservice.service.SpendingMonitor;
import com.fluxtion.example.cookbook.exportservice.service.StatementPublisher;
import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.audit.EventLogManager;
import com.fluxtion.runtime.audit.NodeNameAuditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.callback.ExportFunctionTrigger;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_0;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_1;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_10;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_2;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_3;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_4;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_5;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_6;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_7;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_8;
import com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_9;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.input.EventFeed;
import com.fluxtion.runtime.input.SubscriptionManagerNode;
import com.fluxtion.runtime.node.ForkedTriggerTask;
import com.fluxtion.runtime.node.InstanceSupplierNode;
import com.fluxtion.runtime.node.MutableEventProcessorContext;
import com.fluxtion.runtime.node.MutableEventProcessorContext;
import com.fluxtion.runtime.output.SinkDeregister;
import com.fluxtion.runtime.output.SinkPublisher;
import com.fluxtion.runtime.output.SinkRegistration;
import java.io.File;
import java.util.Arrays;
import java.util.Map;

import java.util.IdentityHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 *
 *
 * <pre>
 * generation time                 : 2023-07-19T10:17:49.525842
 * eventProcessorGenerator version : 9.0.22
 * api version                     : 9.0.22
 * </pre>
 *
 * Event classes supported:
 *
 * <ul>
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_0
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_1
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_2
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_3
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_4
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_5
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_6
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_7
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_8
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_9
 *   <li>com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_10
 *   <li>com.fluxtion.runtime.output.SinkDeregister
 *   <li>com.fluxtion.runtime.output.SinkRegistration
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public class BankEventProcessor
    implements EventProcessor<BankEventProcessor>,
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle,
        StatementPublisher,
        BankAccount,
        ReplaySink,
        SpendingMonitor {

  //Node declarations
  private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  public final NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final SinkPublisher responseSink = new SinkPublisher<>("responseSink");
  private final SubscriptionManagerNode subscriptionManager = new SubscriptionManagerNode();
  private final MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  private final InstanceSupplierNode contextService_Writer_categoryWriter1 =
      new InstanceSupplierNode<>(
          "java.io.Writer_categoryWriter", true, context, "contextService_Writer_categoryWriter1");
  private final InstanceSupplierNode contextService_Writer_transactionWriter0 =
      new InstanceSupplierNode<>(
          "java.io.Writer_transactionWriter",
          true,
          context,
          "contextService_Writer_transactionWriter0");
  private final TransactionStore transactionStore_2 = new TransactionStore();
  private final BankAccountNode bankAccountNode_3 = new BankAccountNode();
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_5 =
      new ExportFunctionTrigger(
          com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_5
              .class);
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_6 =
      new ExportFunctionTrigger(
          com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_6
              .class);
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_7 =
      new ExportFunctionTrigger(
          com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_7
              .class);
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_8 =
      new ExportFunctionTrigger(
          com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_8
              .class);
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_9 =
      new ExportFunctionTrigger(
          com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_9
              .class);
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_10 =
      new ExportFunctionTrigger(
          com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_10
              .class);
  private final SpendingMonitorNode spendingMonitorNode_1 = new SpendingMonitorNode();
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_1 =
      new ExportFunctionTrigger(
          com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_1
              .class);
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_2 =
      new ExportFunctionTrigger(
          com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_2
              .class);
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_3 =
      new ExportFunctionTrigger(
          com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_3
              .class);
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_4 =
      new ExportFunctionTrigger(
          com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_4
              .class);
  private final StatementPublisherNode statementPublisherNode_0 =
      new StatementPublisherNode(spendingMonitorNode_1, bankAccountNode_3);
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_0 =
      new ExportFunctionTrigger(
          com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_0
              .class);
  //Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(12);
  private final IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(12);

  private boolean isDirty_handlerExportFunctionTriggerEvent_0 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_1 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_2 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_3 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_4 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_5 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_6 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_7 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_8 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_9 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_10 = false;
  private boolean isDirty_responseSink = false;
  //Forked declarations

  //Filter constants

  public BankEventProcessor(Map<Object, Object> contextMap) {
    context.replaceMappings(contextMap);
    bankAccountNode_3.setTriggered(false);
    bankAccountNode_3.transactionStore = transactionStore_2;
    bankAccountNode_3.sinkPublisher = responseSink;
    spendingMonitorNode_1.setTriggered(false);
    spendingMonitorNode_1.transactionStore = transactionStore_2;
    statementPublisherNode_0.setTriggered(false);
    statementPublisherNode_0.sinkPublisher = responseSink;
    transactionStore_2.transactionWriterSupplier = contextService_Writer_transactionWriter0;
    transactionStore_2.categoryWriterSupplier = contextService_Writer_categoryWriter1;
    handlerExportFunctionTriggerEvent_0.setFunctionPointerList(
        Arrays.asList(statementPublisherNode_0, statementPublisherNode_0));
    handlerExportFunctionTriggerEvent_1.setFunctionPointerList(
        Arrays.asList(spendingMonitorNode_1, spendingMonitorNode_1));
    handlerExportFunctionTriggerEvent_2.setFunctionPointerList(
        Arrays.asList(spendingMonitorNode_1, spendingMonitorNode_1));
    handlerExportFunctionTriggerEvent_3.setFunctionPointerList(
        Arrays.asList(
            spendingMonitorNode_1, spendingMonitorNode_1, bankAccountNode_3, bankAccountNode_3));
    handlerExportFunctionTriggerEvent_4.setFunctionPointerList(
        Arrays.asList(spendingMonitorNode_1, spendingMonitorNode_1));
    handlerExportFunctionTriggerEvent_5.setFunctionPointerList(
        Arrays.asList(bankAccountNode_3, bankAccountNode_3));
    handlerExportFunctionTriggerEvent_6.setFunctionPointerList(
        Arrays.asList(bankAccountNode_3, bankAccountNode_3));
    handlerExportFunctionTriggerEvent_7.setFunctionPointerList(
        Arrays.asList(bankAccountNode_3, bankAccountNode_3));
    handlerExportFunctionTriggerEvent_8.setFunctionPointerList(
        Arrays.asList(bankAccountNode_3, bankAccountNode_3));
    handlerExportFunctionTriggerEvent_9.setFunctionPointerList(
        Arrays.asList(bankAccountNode_3, bankAccountNode_3));
    handlerExportFunctionTriggerEvent_10.setFunctionPointerList(
        Arrays.asList(bankAccountNode_3, bankAccountNode_3));
    //node auditors
    initialiseAuditor(nodeNameLookup);
    subscriptionManager.setSubscribingEventProcessor(this);
    context.setEventProcessorCallback(this);
  }

  public BankEventProcessor() {
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
    if (event
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
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_6) {
      ExportFunctionTriggerEvent_6 typedEvent = (ExportFunctionTriggerEvent_6) event;
      handleEvent(typedEvent);
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_7) {
      ExportFunctionTriggerEvent_7 typedEvent = (ExportFunctionTriggerEvent_7) event;
      handleEvent(typedEvent);
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_8) {
      ExportFunctionTriggerEvent_8 typedEvent = (ExportFunctionTriggerEvent_8) event;
      handleEvent(typedEvent);
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_9) {
      ExportFunctionTriggerEvent_9 typedEvent = (ExportFunctionTriggerEvent_9) event;
      handleEvent(typedEvent);
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_10) {
      ExportFunctionTriggerEvent_10 typedEvent = (ExportFunctionTriggerEvent_10) event;
      handleEvent(typedEvent);
    } else if (event instanceof com.fluxtion.runtime.output.SinkDeregister) {
      SinkDeregister typedEvent = (SinkDeregister) event;
      handleEvent(typedEvent);
    } else if (event instanceof com.fluxtion.runtime.output.SinkRegistration) {
      SinkRegistration typedEvent = (SinkRegistration) event;
      handleEvent(typedEvent);
    }
  }

  public void addBucket(String arg0) {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    spendingMonitorNode_1.addBucket(arg0);
    spendingMonitorNode_1.setTriggered(true);
    handleEvent((ExportFunctionTriggerEvent_4) handlerExportFunctionTriggerEvent_4.getEvent());
    processing = false;
  }

  public void assignToBucket(String arg0, String arg1, String arg2) {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    spendingMonitorNode_1.assignToBucket(arg0, arg1, arg2);
    spendingMonitorNode_1.setTriggered(true);
    handleEvent((ExportFunctionTriggerEvent_1) handlerExportFunctionTriggerEvent_1.getEvent());
    processing = false;
  }

  public void categoryUpdate(com.fluxtion.example.cookbook.exportservice.data.CategoryUpdate arg0) {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    bankAccountNode_3.categoryUpdate(arg0);
    bankAccountNode_3.setTriggered(false);
    handleEvent((ExportFunctionTriggerEvent_9) handlerExportFunctionTriggerEvent_9.getEvent());
    processing = false;
  }

  public void debit(com.fluxtion.example.cookbook.exportservice.data.Transaction arg0) {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    spendingMonitorNode_1.debit(arg0);
    spendingMonitorNode_1.setTriggered(true);
    bankAccountNode_3.debit(arg0);
    bankAccountNode_3.setTriggered(true);
    handleEvent((ExportFunctionTriggerEvent_3) handlerExportFunctionTriggerEvent_3.getEvent());
    processing = false;
  }

  public void deposit(com.fluxtion.example.cookbook.exportservice.data.Transaction arg0) {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    bankAccountNode_3.deposit(arg0);
    bankAccountNode_3.setTriggered(true);
    handleEvent((ExportFunctionTriggerEvent_6) handlerExportFunctionTriggerEvent_6.getEvent());
    processing = false;
  }

  public void publishStatement(java.util.function.Consumer<String> arg0) {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    statementPublisherNode_0.publishStatement(arg0);
    statementPublisherNode_0.setTriggered(true);
    handleEvent((ExportFunctionTriggerEvent_0) handlerExportFunctionTriggerEvent_0.getEvent());
    processing = false;
  }

  public void replayComplete() {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    bankAccountNode_3.replayComplete();
    bankAccountNode_3.setTriggered(false);
    handleEvent((ExportFunctionTriggerEvent_8) handlerExportFunctionTriggerEvent_8.getEvent());
    processing = false;
  }

  public void replayStarted() {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    bankAccountNode_3.replayStarted();
    bankAccountNode_3.setTriggered(false);
    handleEvent((ExportFunctionTriggerEvent_7) handlerExportFunctionTriggerEvent_7.getEvent());
    processing = false;
  }

  public void resetBucket(String arg0) {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    spendingMonitorNode_1.resetBucket(arg0);
    spendingMonitorNode_1.setTriggered(true);
    handleEvent((ExportFunctionTriggerEvent_2) handlerExportFunctionTriggerEvent_2.getEvent());
    processing = false;
  }

  public void setSpendingLimit(double arg0) {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    bankAccountNode_3.setSpendingLimit(arg0);
    bankAccountNode_3.setTriggered(true);
    handleEvent((ExportFunctionTriggerEvent_5) handlerExportFunctionTriggerEvent_5.getEvent());
    processing = false;
  }

  public void transactionUpdate(com.fluxtion.example.cookbook.exportservice.data.Transaction arg0) {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    bankAccountNode_3.transactionUpdate(arg0);
    bankAccountNode_3.setTriggered(false);
    handleEvent((ExportFunctionTriggerEvent_10) handlerExportFunctionTriggerEvent_10.getEvent());
    processing = false;
  }

  public void handleEvent(ExportFunctionTriggerEvent_0 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_0 =
        handlerExportFunctionTriggerEvent_0.onEvent(typedEvent);
    if (guardCheck_statementPublisherNode_0()) {
      statementPublisherNode_0.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_1 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_1 =
        handlerExportFunctionTriggerEvent_1.onEvent(typedEvent);
    if (guardCheck_spendingMonitorNode_1()) {
      spendingMonitorNode_1.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_2 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_2 =
        handlerExportFunctionTriggerEvent_2.onEvent(typedEvent);
    if (guardCheck_spendingMonitorNode_1()) {
      spendingMonitorNode_1.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_3 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_3 =
        handlerExportFunctionTriggerEvent_3.onEvent(typedEvent);
    if (guardCheck_bankAccountNode_3()) {
      bankAccountNode_3.triggered();
    }
    if (guardCheck_spendingMonitorNode_1()) {
      spendingMonitorNode_1.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_4 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_4 =
        handlerExportFunctionTriggerEvent_4.onEvent(typedEvent);
    if (guardCheck_spendingMonitorNode_1()) {
      spendingMonitorNode_1.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_5 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_5 =
        handlerExportFunctionTriggerEvent_5.onEvent(typedEvent);
    if (guardCheck_bankAccountNode_3()) {
      bankAccountNode_3.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_6 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_6 =
        handlerExportFunctionTriggerEvent_6.onEvent(typedEvent);
    if (guardCheck_bankAccountNode_3()) {
      bankAccountNode_3.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_7 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_7 =
        handlerExportFunctionTriggerEvent_7.onEvent(typedEvent);
    if (guardCheck_bankAccountNode_3()) {
      bankAccountNode_3.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_8 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_8 =
        handlerExportFunctionTriggerEvent_8.onEvent(typedEvent);
    if (guardCheck_bankAccountNode_3()) {
      bankAccountNode_3.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_9 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_9 =
        handlerExportFunctionTriggerEvent_9.onEvent(typedEvent);
    if (guardCheck_bankAccountNode_3()) {
      bankAccountNode_3.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_10 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_10 =
        handlerExportFunctionTriggerEvent_10.onEvent(typedEvent);
    if (guardCheck_bankAccountNode_3()) {
      bankAccountNode_3.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(SinkDeregister typedEvent) {
    auditEvent(typedEvent);
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.runtime.output.SinkDeregister] filterString:[responseSink]
      case ("responseSink"):
        isDirty_responseSink = true;
        responseSink.unregisterSink(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  public void handleEvent(SinkRegistration typedEvent) {
    auditEvent(typedEvent);
    switch (typedEvent.filterString()) {
        //Event Class:[com.fluxtion.runtime.output.SinkRegistration] filterString:[responseSink]
      case ("responseSink"):
        isDirty_responseSink = true;
        responseSink.sinkRegistration(typedEvent);
        afterEvent();
        return;
    }
    afterEvent();
  }

  public void bufferEvent(Object event) {
    buffering = true;
    if (event
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
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_6) {
      ExportFunctionTriggerEvent_6 typedEvent = (ExportFunctionTriggerEvent_6) event;
      auditEvent(typedEvent);
      isDirty_handlerExportFunctionTriggerEvent_6 =
          handlerExportFunctionTriggerEvent_6.onEvent(typedEvent);
      //event stack unwind callbacks
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_7) {
      ExportFunctionTriggerEvent_7 typedEvent = (ExportFunctionTriggerEvent_7) event;
      auditEvent(typedEvent);
      isDirty_handlerExportFunctionTriggerEvent_7 =
          handlerExportFunctionTriggerEvent_7.onEvent(typedEvent);
      //event stack unwind callbacks
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_8) {
      ExportFunctionTriggerEvent_8 typedEvent = (ExportFunctionTriggerEvent_8) event;
      auditEvent(typedEvent);
      isDirty_handlerExportFunctionTriggerEvent_8 =
          handlerExportFunctionTriggerEvent_8.onEvent(typedEvent);
      //event stack unwind callbacks
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_9) {
      ExportFunctionTriggerEvent_9 typedEvent = (ExportFunctionTriggerEvent_9) event;
      auditEvent(typedEvent);
      isDirty_handlerExportFunctionTriggerEvent_9 =
          handlerExportFunctionTriggerEvent_9.onEvent(typedEvent);
      //event stack unwind callbacks
    } else if (event
        instanceof
        com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_10) {
      ExportFunctionTriggerEvent_10 typedEvent = (ExportFunctionTriggerEvent_10) event;
      auditEvent(typedEvent);
      isDirty_handlerExportFunctionTriggerEvent_10 =
          handlerExportFunctionTriggerEvent_10.onEvent(typedEvent);
      //event stack unwind callbacks
    } else if (event instanceof com.fluxtion.runtime.output.SinkDeregister) {
      SinkDeregister typedEvent = (SinkDeregister) event;
      auditEvent(typedEvent);
      switch (typedEvent.filterString()) {
          //Event Class:[com.fluxtion.runtime.output.SinkDeregister] filterString:[responseSink]
        case ("responseSink"):
          isDirty_responseSink = true;
          responseSink.unregisterSink(typedEvent);
          //event stack unwind callbacks
          afterEvent();
          return;
      }
    } else if (event instanceof com.fluxtion.runtime.output.SinkRegistration) {
      SinkRegistration typedEvent = (SinkRegistration) event;
      auditEvent(typedEvent);
      switch (typedEvent.filterString()) {
          //Event Class:[com.fluxtion.runtime.output.SinkRegistration] filterString:[responseSink]
        case ("responseSink"):
          isDirty_responseSink = true;
          responseSink.sinkRegistration(typedEvent);
          //event stack unwind callbacks
          afterEvent();
          return;
      }
    }
  }

  public void triggerCalculation() {
    buffering = false;
    String typedEvent = "No event information - buffered dispatch";
    if (guardCheck_bankAccountNode_3()) {
      bankAccountNode_3.triggered();
    }
    if (guardCheck_spendingMonitorNode_1()) {
      spendingMonitorNode_1.triggered();
    }
    if (guardCheck_statementPublisherNode_0()) {
      statementPublisherNode_0.triggered();
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
    auditor.nodeRegistered(bankAccountNode_3, "bankAccountNode_3");
    auditor.nodeRegistered(spendingMonitorNode_1, "spendingMonitorNode_1");
    auditor.nodeRegistered(statementPublisherNode_0, "statementPublisherNode_0");
    auditor.nodeRegistered(transactionStore_2, "transactionStore_2");
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
    auditor.nodeRegistered(
        handlerExportFunctionTriggerEvent_6, "handlerExportFunctionTriggerEvent_6");
    auditor.nodeRegistered(
        handlerExportFunctionTriggerEvent_7, "handlerExportFunctionTriggerEvent_7");
    auditor.nodeRegistered(
        handlerExportFunctionTriggerEvent_8, "handlerExportFunctionTriggerEvent_8");
    auditor.nodeRegistered(
        handlerExportFunctionTriggerEvent_9, "handlerExportFunctionTriggerEvent_9");
    auditor.nodeRegistered(
        handlerExportFunctionTriggerEvent_10, "handlerExportFunctionTriggerEvent_10");
    auditor.nodeRegistered(subscriptionManager, "subscriptionManager");
    auditor.nodeRegistered(
        contextService_Writer_categoryWriter1, "contextService_Writer_categoryWriter1");
    auditor.nodeRegistered(
        contextService_Writer_transactionWriter0, "contextService_Writer_transactionWriter0");
    auditor.nodeRegistered(context, "context");
    auditor.nodeRegistered(responseSink, "responseSink");
  }

  private void afterEvent() {
    statementPublisherNode_0.afterEvent();
    spendingMonitorNode_1.afterEvent();
    bankAccountNode_3.afterEvent();

    nodeNameLookup.processingComplete();
    isDirty_handlerExportFunctionTriggerEvent_0 = false;
    isDirty_handlerExportFunctionTriggerEvent_1 = false;
    isDirty_handlerExportFunctionTriggerEvent_2 = false;
    isDirty_handlerExportFunctionTriggerEvent_3 = false;
    isDirty_handlerExportFunctionTriggerEvent_4 = false;
    isDirty_handlerExportFunctionTriggerEvent_5 = false;
    isDirty_handlerExportFunctionTriggerEvent_6 = false;
    isDirty_handlerExportFunctionTriggerEvent_7 = false;
    isDirty_handlerExportFunctionTriggerEvent_8 = false;
    isDirty_handlerExportFunctionTriggerEvent_9 = false;
    isDirty_handlerExportFunctionTriggerEvent_10 = false;
    isDirty_responseSink = false;
  }

  @Override
  public void init() {
    initCalled = true;
    auditEvent(Lifecycle.LifecycleEvent.Init);
    //initialise dirty lookup map
    isDirty("test");
    contextService_Writer_categoryWriter1.init();
    contextService_Writer_transactionWriter0.init();
    transactionStore_2.init();
    bankAccountNode_3.init();
    spendingMonitorNode_1.init();
    statementPublisherNode_0.init();
    afterEvent();
  }

  @Override
  public void start() {
    if (!initCalled) {
      throw new RuntimeException("init() must be called before start()");
    }
    processing = true;
    auditEvent(Lifecycle.LifecycleEvent.Start);
    bankAccountNode_3.start();
    spendingMonitorNode_1.start();
    statementPublisherNode_0.start();
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
    transactionStore_2.tearDown();
    contextService_Writer_transactionWriter0.tearDown();
    contextService_Writer_categoryWriter1.tearDown();
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
          handlerExportFunctionTriggerEvent_10, () -> isDirty_handlerExportFunctionTriggerEvent_10);
      dirtyFlagSupplierMap.put(
          handlerExportFunctionTriggerEvent_2, () -> isDirty_handlerExportFunctionTriggerEvent_2);
      dirtyFlagSupplierMap.put(
          handlerExportFunctionTriggerEvent_3, () -> isDirty_handlerExportFunctionTriggerEvent_3);
      dirtyFlagSupplierMap.put(
          handlerExportFunctionTriggerEvent_4, () -> isDirty_handlerExportFunctionTriggerEvent_4);
      dirtyFlagSupplierMap.put(
          handlerExportFunctionTriggerEvent_5, () -> isDirty_handlerExportFunctionTriggerEvent_5);
      dirtyFlagSupplierMap.put(
          handlerExportFunctionTriggerEvent_6, () -> isDirty_handlerExportFunctionTriggerEvent_6);
      dirtyFlagSupplierMap.put(
          handlerExportFunctionTriggerEvent_7, () -> isDirty_handlerExportFunctionTriggerEvent_7);
      dirtyFlagSupplierMap.put(
          handlerExportFunctionTriggerEvent_8, () -> isDirty_handlerExportFunctionTriggerEvent_8);
      dirtyFlagSupplierMap.put(
          handlerExportFunctionTriggerEvent_9, () -> isDirty_handlerExportFunctionTriggerEvent_9);
      dirtyFlagSupplierMap.put(responseSink, () -> isDirty_responseSink);
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
          handlerExportFunctionTriggerEvent_10,
          (b) -> isDirty_handlerExportFunctionTriggerEvent_10 = b);
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
      dirtyFlagUpdateMap.put(
          handlerExportFunctionTriggerEvent_6,
          (b) -> isDirty_handlerExportFunctionTriggerEvent_6 = b);
      dirtyFlagUpdateMap.put(
          handlerExportFunctionTriggerEvent_7,
          (b) -> isDirty_handlerExportFunctionTriggerEvent_7 = b);
      dirtyFlagUpdateMap.put(
          handlerExportFunctionTriggerEvent_8,
          (b) -> isDirty_handlerExportFunctionTriggerEvent_8 = b);
      dirtyFlagUpdateMap.put(
          handlerExportFunctionTriggerEvent_9,
          (b) -> isDirty_handlerExportFunctionTriggerEvent_9 = b);
      dirtyFlagUpdateMap.put(responseSink, (b) -> isDirty_responseSink = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_bankAccountNode_3() {
    return isDirty_handlerExportFunctionTriggerEvent_3
        | isDirty_handlerExportFunctionTriggerEvent_5
        | isDirty_handlerExportFunctionTriggerEvent_6
        | isDirty_handlerExportFunctionTriggerEvent_7
        | isDirty_handlerExportFunctionTriggerEvent_8
        | isDirty_handlerExportFunctionTriggerEvent_9
        | isDirty_handlerExportFunctionTriggerEvent_10
        | isDirty_responseSink;
  }

  private boolean guardCheck_spendingMonitorNode_1() {
    return isDirty_handlerExportFunctionTriggerEvent_1
        | isDirty_handlerExportFunctionTriggerEvent_2
        | isDirty_handlerExportFunctionTriggerEvent_3
        | isDirty_handlerExportFunctionTriggerEvent_4;
  }

  private boolean guardCheck_statementPublisherNode_0() {
    return isDirty_handlerExportFunctionTriggerEvent_0 | isDirty_responseSink;
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
  public BankEventProcessor newInstance() {
    return new BankEventProcessor();
  }

  @Override
  public BankEventProcessor newInstance(Map<Object, Object> contextMap) {
    return new BankEventProcessor();
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
