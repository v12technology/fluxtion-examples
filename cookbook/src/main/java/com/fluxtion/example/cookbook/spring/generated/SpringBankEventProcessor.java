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
package com.fluxtion.example.cookbook.spring.generated;

import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.example.cookbook.spring.node.AccountNode;
import com.fluxtion.example.cookbook.spring.node.BankTransactionStore;
import com.fluxtion.example.cookbook.spring.node.CreditCheckNode;
import com.fluxtion.example.cookbook.spring.node.TransactionResponsePublisher;
import com.fluxtion.example.cookbook.spring.service.Account;
import com.fluxtion.example.cookbook.spring.service.BankingOperations;
import com.fluxtion.example.cookbook.spring.service.CreditCheck;
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
 * generation time                 : 2023-07-28T16:11:18.765128
 * eventProcessorGenerator version : 9.0.24
 * api version                     : 9.0.24
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
 * </ul>
 *
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public class SpringBankEventProcessor
    implements EventProcessor<SpringBankEventProcessor>,
        StaticEventProcessor,
        InternalEventProcessor,
        BatchHandler,
        Lifecycle,
        CreditCheck,
        Account,
        BankingOperations {

  //Node declarations
  private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  public final NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  private final SubscriptionManagerNode subscriptionManager = new SubscriptionManagerNode();
  private final MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  public final TransactionResponsePublisher transactionResponsePublisher =
      new TransactionResponsePublisher();
  public final AccountNode accountBean = new AccountNode();
  public final CreditCheckNode creditCheck = new CreditCheckNode();
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_0 =
      new ExportFunctionTrigger(
          com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_0
              .class);
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
  public final BankTransactionStore transactionStore = new BankTransactionStore();
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_4 =
      new ExportFunctionTrigger(
          com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_4
              .class);
  private final ExportFunctionTrigger handlerExportFunctionTriggerEvent_5 =
      new ExportFunctionTrigger(
          com.fluxtion.runtime.callback.ExportFunctionTriggerEvent.ExportFunctionTriggerEvent_5
              .class);
  //Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(8);
  private final IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(8);

  private boolean isDirty_accountBean = false;
  private boolean isDirty_creditCheck = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_0 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_1 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_2 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_3 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_4 = false;
  private boolean isDirty_handlerExportFunctionTriggerEvent_5 = false;
  //Forked declarations

  //Filter constants

  public SpringBankEventProcessor(Map<Object, Object> contextMap) {
    context.replaceMappings(contextMap);
    accountBean.setTransactionResponsePublisher(transactionResponsePublisher);
    accountBean.setTriggered(false);
    transactionStore.setOpenForBusiness(false);
    transactionStore.setTransactionResponsePublisher(transactionResponsePublisher);
    transactionStore.setTransactionSource(creditCheck);
    transactionStore.setTriggered(false);
    creditCheck.setTransactionResponsePublisher(transactionResponsePublisher);
    creditCheck.setTransactionSource(accountBean);
    creditCheck.setTriggered(false);
    handlerExportFunctionTriggerEvent_0.setFunctionPointerList(
        Arrays.asList(accountBean, accountBean));
    handlerExportFunctionTriggerEvent_1.setFunctionPointerList(
        Arrays.asList(accountBean, accountBean));
    handlerExportFunctionTriggerEvent_2.setFunctionPointerList(
        Arrays.asList(creditCheck, creditCheck));
    handlerExportFunctionTriggerEvent_3.setFunctionPointerList(
        Arrays.asList(creditCheck, creditCheck));
    handlerExportFunctionTriggerEvent_4.setFunctionPointerList(
        Arrays.asList(transactionStore, transactionStore));
    handlerExportFunctionTriggerEvent_5.setFunctionPointerList(
        Arrays.asList(transactionStore, transactionStore));
    //node auditors
    initialiseAuditor(nodeNameLookup);
    subscriptionManager.setSubscribingEventProcessor(this);
    context.setEventProcessorCallback(this);
  }

  public SpringBankEventProcessor() {
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
    }
  }

  public void blackListAccount(int arg0) {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    creditCheck.blackListAccount(arg0);
    creditCheck.setTriggered(false);
    handleEvent((ExportFunctionTriggerEvent_2) handlerExportFunctionTriggerEvent_2.getEvent());
    processing = false;
  }

  public void closedForBusiness() {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    transactionStore.closedForBusiness();
    transactionStore.setTriggered(true);
    handleEvent((ExportFunctionTriggerEvent_5) handlerExportFunctionTriggerEvent_5.getEvent());
    processing = false;
  }

  public void credit(int arg0, double arg1) {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    accountBean.credit(arg0, arg1);
    accountBean.setTriggered(true);
    handleEvent((ExportFunctionTriggerEvent_1) handlerExportFunctionTriggerEvent_1.getEvent());
    processing = false;
  }

  public void debit(int arg0, double arg1) {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    accountBean.debit(arg0, arg1);
    accountBean.setTriggered(true);
    handleEvent((ExportFunctionTriggerEvent_0) handlerExportFunctionTriggerEvent_0.getEvent());
    processing = false;
  }

  public void openForBusiness() {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    transactionStore.openForBusiness();
    transactionStore.setTriggered(true);
    handleEvent((ExportFunctionTriggerEvent_4) handlerExportFunctionTriggerEvent_4.getEvent());
    processing = false;
  }

  public void whiteListAccount(int arg0) {
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    creditCheck.whiteListAccount(arg0);
    creditCheck.setTriggered(false);
    handleEvent((ExportFunctionTriggerEvent_3) handlerExportFunctionTriggerEvent_3.getEvent());
    processing = false;
  }

  public void handleEvent(ExportFunctionTriggerEvent_0 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_0 =
        handlerExportFunctionTriggerEvent_0.onEvent(typedEvent);
    if (guardCheck_accountBean()) {
      isDirty_accountBean = accountBean.triggered();
    }
    if (guardCheck_creditCheck()) {
      isDirty_creditCheck = creditCheck.triggered();
    }
    if (guardCheck_transactionStore()) {
      transactionStore.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_1 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_1 =
        handlerExportFunctionTriggerEvent_1.onEvent(typedEvent);
    if (guardCheck_accountBean()) {
      isDirty_accountBean = accountBean.triggered();
    }
    if (guardCheck_creditCheck()) {
      isDirty_creditCheck = creditCheck.triggered();
    }
    if (guardCheck_transactionStore()) {
      transactionStore.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_2 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_2 =
        handlerExportFunctionTriggerEvent_2.onEvent(typedEvent);
    if (guardCheck_creditCheck()) {
      isDirty_creditCheck = creditCheck.triggered();
    }
    if (guardCheck_transactionStore()) {
      transactionStore.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_3 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_3 =
        handlerExportFunctionTriggerEvent_3.onEvent(typedEvent);
    if (guardCheck_creditCheck()) {
      isDirty_creditCheck = creditCheck.triggered();
    }
    if (guardCheck_transactionStore()) {
      transactionStore.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_4 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_4 =
        handlerExportFunctionTriggerEvent_4.onEvent(typedEvent);
    if (guardCheck_transactionStore()) {
      transactionStore.triggered();
    }
    //event stack unwind callbacks
    afterEvent();
  }

  public void handleEvent(ExportFunctionTriggerEvent_5 typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_handlerExportFunctionTriggerEvent_5 =
        handlerExportFunctionTriggerEvent_5.onEvent(typedEvent);
    if (guardCheck_transactionStore()) {
      transactionStore.triggered();
    }
    //event stack unwind callbacks
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
    }
  }

  public void triggerCalculation() {
    buffering = false;
    String typedEvent = "No event information - buffered dispatch";
    if (guardCheck_accountBean()) {
      isDirty_accountBean = accountBean.triggered();
    }
    if (guardCheck_creditCheck()) {
      isDirty_creditCheck = creditCheck.triggered();
    }
    if (guardCheck_transactionStore()) {
      transactionStore.triggered();
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
    auditor.nodeRegistered(accountBean, "accountBean");
    auditor.nodeRegistered(transactionStore, "transactionStore");
    auditor.nodeRegistered(creditCheck, "creditCheck");
    auditor.nodeRegistered(transactionResponsePublisher, "transactionResponsePublisher");
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

    nodeNameLookup.processingComplete();
    isDirty_accountBean = false;
    isDirty_creditCheck = false;
    isDirty_handlerExportFunctionTriggerEvent_0 = false;
    isDirty_handlerExportFunctionTriggerEvent_1 = false;
    isDirty_handlerExportFunctionTriggerEvent_2 = false;
    isDirty_handlerExportFunctionTriggerEvent_3 = false;
    isDirty_handlerExportFunctionTriggerEvent_4 = false;
    isDirty_handlerExportFunctionTriggerEvent_5 = false;
  }

  @Override
  public void init() {
    initCalled = true;
    auditEvent(Lifecycle.LifecycleEvent.Init);
    //initialise dirty lookup map
    isDirty("test");
    accountBean.init();
    creditCheck.init();
    transactionStore.init();
    afterEvent();
  }

  @Override
  public void start() {
    if (!initCalled) {
      throw new RuntimeException("init() must be called before start()");
    }
    processing = true;
    auditEvent(Lifecycle.LifecycleEvent.Start);
    accountBean.start();
    creditCheck.start();
    transactionStore.start();
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
      dirtyFlagSupplierMap.put(accountBean, () -> isDirty_accountBean);
      dirtyFlagSupplierMap.put(creditCheck, () -> isDirty_creditCheck);
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
    }
    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(accountBean, (b) -> isDirty_accountBean = b);
      dirtyFlagUpdateMap.put(creditCheck, (b) -> isDirty_creditCheck = b);
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
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_accountBean() {
    return isDirty_handlerExportFunctionTriggerEvent_0
        | isDirty_handlerExportFunctionTriggerEvent_1;
  }

  private boolean guardCheck_transactionStore() {
    return isDirty_creditCheck
        | isDirty_handlerExportFunctionTriggerEvent_4
        | isDirty_handlerExportFunctionTriggerEvent_5;
  }

  private boolean guardCheck_creditCheck() {
    return isDirty_accountBean
        | isDirty_handlerExportFunctionTriggerEvent_2
        | isDirty_handlerExportFunctionTriggerEvent_3;
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
  public SpringBankEventProcessor newInstance() {
    return new SpringBankEventProcessor();
  }

  @Override
  public SpringBankEventProcessor newInstance(Map<Object, Object> contextMap) {
    return new SpringBankEventProcessor();
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
