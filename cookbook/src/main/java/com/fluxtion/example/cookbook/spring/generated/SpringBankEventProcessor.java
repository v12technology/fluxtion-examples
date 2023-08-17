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
import com.fluxtion.example.cookbook.spring.data.Transaction;
import com.fluxtion.example.cookbook.spring.node.AccountNode;
import com.fluxtion.example.cookbook.spring.node.CentralTransactionProcessor;
import com.fluxtion.example.cookbook.spring.node.CreditCheckNode;
import com.fluxtion.example.cookbook.spring.node.ResponsePublisher;
import com.fluxtion.example.cookbook.spring.service.Account;
import com.fluxtion.example.cookbook.spring.service.BankingOperations;
import com.fluxtion.example.cookbook.spring.service.CreditCheck;
import com.fluxtion.runtime.EventProcessorContext;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.audit.EventLogManager;
import com.fluxtion.runtime.audit.NodeNameAuditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.callback.ExportFunctionAuditEvent;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.input.EventFeed;
import com.fluxtion.runtime.input.SubscriptionManagerNode;
import com.fluxtion.runtime.node.ForkedTriggerTask;
import com.fluxtion.runtime.node.MutableEventProcessorContext;
import com.fluxtion.runtime.node.MutableEventProcessorContext;
import java.util.Map;

import java.util.IdentityHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 *
 *
 * <pre>
 * generation time                 : 2023-08-17T12:40:11.486503
 * eventProcessorGenerator version : 9.1.4
 * api version                     : 9.1.4
 * </pre>
 *
 * Event classes supported:
 *
 * <ul>
 *   <li>com.fluxtion.compiler.generation.model.ExportFunctionMarker
 *   <li>com.fluxtion.example.cookbook.spring.data.Transaction
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
        BankingOperations,
        CreditCheck,
        Account {

  //Node declarations
  private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
  public final NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
  public final ResponsePublisher responsePublisher = new ResponsePublisher();
  public final AccountNode accountBean = new AccountNode();
  public final CreditCheckNode creditCheck = new CreditCheckNode();
  private final SubscriptionManagerNode subscriptionManager = new SubscriptionManagerNode();
  private final MutableEventProcessorContext context =
      new MutableEventProcessorContext(
          nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
  public final CentralTransactionProcessor transactionStore = new CentralTransactionProcessor();
  private ExportFunctionAuditEvent functionAudit = new ExportFunctionAuditEvent();
  //Dirty flags
  private boolean initCalled = false;
  private boolean processing = false;
  private boolean buffering = false;
  private final IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap =
      new IdentityHashMap<>(2);
  private final IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap =
      new IdentityHashMap<>(2);

  private boolean isDirty_accountBean = false;
  private boolean isDirty_creditCheck = false;
  //Forked declarations

  //Filter constants

  public SpringBankEventProcessor(Map<Object, Object> contextMap) {
    context.replaceMappings(contextMap);
    accountBean.setResponsePublisher(responsePublisher);
    transactionStore.setOpenForBusiness(false);
    transactionStore.setResponsePublisher(responsePublisher);
    transactionStore.setTransactionSource(creditCheck);
    creditCheck.setResponsePublisher(responsePublisher);
    creditCheck.setTransactionSource(accountBean);
    //node auditors
    initialiseAuditor(nodeNameLookup);
    subscriptionManager.setSubscribingEventProcessor(this);
    context.setEventProcessorCallback(this);
  }

  public SpringBankEventProcessor() {
    this(null);
  }

  @Override
  public void init() {
    initCalled = true;
    auditEvent(Lifecycle.LifecycleEvent.Init);
    //initialise dirty lookup map
    isDirty("test");

    afterEvent();
  }

  @Override
  public void start() {
    if (!initCalled) {
      throw new RuntimeException("init() must be called before start()");
    }
    processing = true;
    auditEvent(Lifecycle.LifecycleEvent.Start);
    transactionStore.startProcessor();
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
    if (event instanceof com.fluxtion.example.cookbook.spring.data.Transaction) {
      Transaction typedEvent = (Transaction) event;
      handleEvent(typedEvent);
    }
  }

  public void handleEvent(Transaction typedEvent) {
    auditEvent(typedEvent);
    //Default, no filter methods
    isDirty_accountBean = accountBean.replayTransaction(typedEvent);
    //event stack unwind callbacks
    accountBean.afterEventRequest();
    afterEvent();
  }
  //EVENT DISPATCH - END

  //EXPORTED SERVICE FUNCTIONS - START
  @Override
  public boolean debit(int arg0, double arg1) {
    //String typedEvent = "No event information - export function";
    functionAudit.setFunctionDescription(
        "public boolean com.fluxtion.example.cookbook.spring.node.AccountNode.debit(int,double)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    auditEvent(functionAudit);
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    isDirty_accountBean = accountBean.debit(arg0, arg1);
    if (guardCheck_creditCheck()) {
      isDirty_creditCheck = creditCheck.checkCredit();
    }
    if (guardCheck_transactionStore()) {
      transactionStore.tryCommit();
    }
    //event stack unwind callbacks
    accountBean.afterEventRequest();
    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
    return true;
  }

  @Override
  public boolean deposit(int arg0, double arg1) {
    //String typedEvent = "No event information - export function";
    functionAudit.setFunctionDescription(
        "public boolean com.fluxtion.example.cookbook.spring.node.AccountNode.deposit(int,double)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    auditEvent(functionAudit);
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    isDirty_accountBean = accountBean.deposit(arg0, arg1);
    if (guardCheck_creditCheck()) {
      isDirty_creditCheck = creditCheck.checkCredit();
    }
    if (guardCheck_transactionStore()) {
      transactionStore.tryCommit();
    }
    //event stack unwind callbacks
    accountBean.afterEventRequest();
    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
    return true;
  }

  @Override
  public void blackListAccount(int arg0) {
    //String typedEvent = "No event information - export function";
    functionAudit.setFunctionDescription(
        "public void com.fluxtion.example.cookbook.spring.node.CreditCheckNode.blackListAccount(int)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    auditEvent(functionAudit);
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    isDirty_creditCheck = true;
    creditCheck.blackListAccount(arg0);
    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
  }

  @Override
  public void closeAccount(int arg0) {
    //String typedEvent = "No event information - export function";
    functionAudit.setFunctionDescription(
        "public void com.fluxtion.example.cookbook.spring.node.AccountNode.closeAccount(int)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    auditEvent(functionAudit);
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    isDirty_accountBean = true;
    accountBean.closeAccount(arg0);
    //event stack unwind callbacks
    accountBean.afterEventRequest();
    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
  }

  @Override
  public void closedForBusiness() {
    //String typedEvent = "No event information - export function";
    functionAudit.setFunctionDescription(
        "public void com.fluxtion.example.cookbook.spring.node.CentralTransactionProcessor.closedForBusiness()");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    auditEvent(functionAudit);
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    transactionStore.closedForBusiness();
    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
  }

  @Override
  public void openAccount(int arg0) {
    //String typedEvent = "No event information - export function";
    functionAudit.setFunctionDescription(
        "public void com.fluxtion.example.cookbook.spring.node.AccountNode.openAccount(int)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    auditEvent(functionAudit);
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    isDirty_accountBean = true;
    accountBean.openAccount(arg0);
    //event stack unwind callbacks
    accountBean.afterEventRequest();
    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
  }

  @Override
  public void openForBusiness() {
    //String typedEvent = "No event information - export function";
    functionAudit.setFunctionDescription(
        "public void com.fluxtion.example.cookbook.spring.node.CentralTransactionProcessor.openForBusiness()");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    auditEvent(functionAudit);
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    transactionStore.openForBusiness();
    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
  }

  @Override
  public void publishBalance(int arg0) {
    //String typedEvent = "No event information - export function";
    functionAudit.setFunctionDescription(
        "public void com.fluxtion.example.cookbook.spring.node.AccountNode.publishBalance(int)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    auditEvent(functionAudit);
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    isDirty_accountBean = true;
    accountBean.publishBalance(arg0);
    //event stack unwind callbacks
    accountBean.afterEventRequest();
    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
  }

  @Override
  public void setDataStore(com.fluxtion.example.cookbook.spring.service.DataStore arg0) {
    //String typedEvent = "No event information - export function";
    functionAudit.setFunctionDescription(
        "public void com.fluxtion.example.cookbook.spring.node.CentralTransactionProcessor.setDataStore(com.fluxtion.example.cookbook.spring.service.DataStore)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    auditEvent(functionAudit);
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    transactionStore.setDataStore(arg0);
    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
  }

  @Override
  public void whiteListAccount(int arg0) {
    //String typedEvent = "No event information - export function";
    functionAudit.setFunctionDescription(
        "public void com.fluxtion.example.cookbook.spring.node.CreditCheckNode.whiteListAccount(int)");
    ExportFunctionAuditEvent typedEvent = functionAudit;
    auditEvent(functionAudit);
    if (buffering) {
      triggerCalculation();
    }
    processing = true;
    isDirty_creditCheck = true;
    creditCheck.whiteListAccount(arg0);
    afterEvent();
    callbackDispatcher.dispatchQueuedCallbacks();
    processing = false;
  }
  //EXPORTED SERVICE FUNCTIONS - END

  public void bufferEvent(Object event) {
    buffering = true;
    if (event instanceof com.fluxtion.example.cookbook.spring.data.Transaction) {
      Transaction typedEvent = (Transaction) event;
      auditEvent(typedEvent);
      isDirty_accountBean = true;
      accountBean.replayTransaction(typedEvent);
    }
  }

  public void triggerCalculation() {
    buffering = false;
    String typedEvent = "No event information - buffered dispatch";
    if (guardCheck_creditCheck()) {
      isDirty_creditCheck = true;
      creditCheck.checkCredit();
    }
    if (guardCheck_transactionStore()) {
      transactionStore.tryCommit();
    }
    //event stack unwind callbacks
    accountBean.afterEventRequest();
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
    auditor.nodeRegistered(responsePublisher, "responsePublisher");
    auditor.nodeRegistered(callbackDispatcher, "callbackDispatcher");
    auditor.nodeRegistered(subscriptionManager, "subscriptionManager");
    auditor.nodeRegistered(context, "context");
  }

  private void afterEvent() {

    nodeNameLookup.processingComplete();
    isDirty_accountBean = false;
    isDirty_creditCheck = false;
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
      dirtyFlagSupplierMap.put(accountBean, () -> isDirty_accountBean);
      dirtyFlagSupplierMap.put(creditCheck, () -> isDirty_creditCheck);
    }
    return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE);
  }

  @Override
  public void setDirty(Object node, boolean dirtyFlag) {
    if (dirtyFlagUpdateMap.isEmpty()) {
      dirtyFlagUpdateMap.put(accountBean, (b) -> isDirty_accountBean = b);
      dirtyFlagUpdateMap.put(creditCheck, (b) -> isDirty_creditCheck = b);
    }
    dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
  }

  private boolean guardCheck_transactionStore() {
    return isDirty_creditCheck;
  }

  private boolean guardCheck_creditCheck() {
    return isDirty_accountBean;
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
