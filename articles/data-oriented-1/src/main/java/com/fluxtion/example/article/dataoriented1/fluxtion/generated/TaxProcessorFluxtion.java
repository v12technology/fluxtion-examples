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
package com.fluxtion.example.article.dataoriented1.fluxtion.generated;

import com.fluxtion.example.article.dataoriented1.Events.BookSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.BookTaxRateEvent;
import com.fluxtion.example.article.dataoriented1.Events.FoodSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.FoodTaxRateEvent;
import com.fluxtion.example.article.dataoriented1.Events.HardwareSaleEvent;
import com.fluxtion.example.article.dataoriented1.Events.HardwareTaxRateEvent;
import com.fluxtion.example.article.dataoriented1.Events.TaxLiabilityNotificationThresholdEvent;
import com.fluxtion.example.article.dataoriented1.Events.TaxPaymentEvent;
import com.fluxtion.example.article.dataoriented1.fluxtion.BookSaleHandler;
import com.fluxtion.example.article.dataoriented1.fluxtion.FoodSaleHandler;
import com.fluxtion.example.article.dataoriented1.fluxtion.HardwareSaleHandler;
import com.fluxtion.example.article.dataoriented1.fluxtion.TaxLiabilityAlerter;
import com.fluxtion.example.article.dataoriented1.fluxtion.TaxLiabilityCalculator;
import com.fluxtion.example.article.dataoriented1.fluxtion.TaxLiabilityThresholdMonitor;
import com.fluxtion.runtime.EventProcessor;
import com.fluxtion.runtime.StaticEventProcessor;
import com.fluxtion.runtime.audit.Auditor;
import com.fluxtion.runtime.audit.EventLogManager;
import com.fluxtion.runtime.audit.NodeNameAuditor;
import com.fluxtion.runtime.callback.CallbackDispatcherImpl;
import com.fluxtion.runtime.callback.InternalEventProcessor;
import com.fluxtion.runtime.event.Event;
import com.fluxtion.runtime.input.EventFeed;
import com.fluxtion.runtime.input.SubscriptionManagerNode;
import com.fluxtion.runtime.lifecycle.BatchHandler;
import com.fluxtion.runtime.lifecycle.Lifecycle;
import com.fluxtion.runtime.node.MutableEventProcessorContext;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/*
 *
 * <pre>
 * generation time                 : 2023-03-26T11:48:55.439828
 * eventProcessorGenerator version : 8.1.12
 * api version                     : 8.1.12
 * </pre>
 * @author Greg Higgins
 */
@SuppressWarnings({"deprecation", "unchecked", "rawtypes"})
public class TaxProcessorFluxtion implements
        EventProcessor<TaxProcessorFluxtion>, StaticEventProcessor, InternalEventProcessor, BatchHandler, Lifecycle {

    public final NodeNameAuditor nodeNameLookup = new NodeNameAuditor();
    //Node declarations
    private final BookSaleHandler bookSaleHandler_3 = new BookSaleHandler();
    private final CallbackDispatcherImpl callbackDispatcher = new CallbackDispatcherImpl();
    private final FoodSaleHandler foodSaleHandler_4 = new FoodSaleHandler();
    private final HardwareSaleHandler hardwareSaleHandler_5 = new HardwareSaleHandler();
    private final SubscriptionManagerNode subscriptionManager = new SubscriptionManagerNode();
    private final MutableEventProcessorContext context = new MutableEventProcessorContext(nodeNameLookup, callbackDispatcher, subscriptionManager, callbackDispatcher);
    private final TaxLiabilityCalculator taxLiabilityCalculator_2 = new TaxLiabilityCalculator(bookSaleHandler_3, foodSaleHandler_4, hardwareSaleHandler_5);
    private final TaxLiabilityThresholdMonitor taxLiabilityThresholdMonitor_1 = new TaxLiabilityThresholdMonitor(taxLiabilityCalculator_2);
    private final TaxLiabilityAlerter taxLiabilityAlerter_0 = new TaxLiabilityAlerter(taxLiabilityThresholdMonitor_1);
    private final IdentityHashMap<Object, BooleanSupplier> dirtyFlagSupplierMap = new IdentityHashMap<>(5);
    private final IdentityHashMap<Object, Consumer<Boolean>> dirtyFlagUpdateMap = new IdentityHashMap<>(5);
    //Dirty flags
    private boolean initCalled = false;
    private boolean processing = false;
    private boolean buffering = false;
    private boolean isDirty_bookSaleHandler_3 = false;
    private boolean isDirty_foodSaleHandler_4 = false;
    private boolean isDirty_hardwareSaleHandler_5 = false;
    private boolean isDirty_taxLiabilityCalculator_2 = false;
    private boolean isDirty_taxLiabilityThresholdMonitor_1 = false;
    //Filter constants


    public TaxProcessorFluxtion(Map<Object, Object> contextMap) {
        context.replaceMappings(contextMap);
        //node auditors
        initialiseAuditor(nodeNameLookup);
        subscriptionManager.setSubscribingEventProcessor(this);
        context.setEventProcessorCallback(this);
    }

    public TaxProcessorFluxtion() {
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
        switch (event) {
            case BookSaleEvent typedEvent -> handleEvent(typedEvent);
            case BookTaxRateEvent typedEvent -> handleEvent(typedEvent);
            case FoodSaleEvent typedEvent -> handleEvent(typedEvent);
            case FoodTaxRateEvent typedEvent -> handleEvent(typedEvent);
            case HardwareSaleEvent typedEvent -> handleEvent(typedEvent);
            case HardwareTaxRateEvent typedEvent -> handleEvent(typedEvent);
            case TaxLiabilityNotificationThresholdEvent typedEvent -> handleEvent(typedEvent);
            case TaxPaymentEvent typedEvent -> handleEvent(typedEvent);
            default -> {
            }
        }
    }


    public void handleEvent(BookSaleEvent typedEvent) {
        auditEvent(typedEvent);
        //Default, no filter methods
        isDirty_bookSaleHandler_3 = bookSaleHandler_3.handleSale(typedEvent);
        if (guardCheck_taxLiabilityCalculator_2()) {
            isDirty_taxLiabilityCalculator_2 = taxLiabilityCalculator_2.hasTaxLiabilityChanged();
        }
        if (guardCheck_taxLiabilityThresholdMonitor_1()) {
            isDirty_taxLiabilityThresholdMonitor_1 = taxLiabilityThresholdMonitor_1.isTaxLiabilityBreached();
        }
        if (guardCheck_taxLiabilityAlerter_0()) {
            taxLiabilityAlerter_0.publishTaxLiabilityWarning();
        }
        //event stack unwind callbacks
        afterEvent();
    }

    public void handleEvent(BookTaxRateEvent typedEvent) {
        auditEvent(typedEvent);
        //Default, no filter methods
        isDirty_bookSaleHandler_3 = bookSaleHandler_3.handleUpdatedTaxRate(typedEvent);
        if (guardCheck_taxLiabilityCalculator_2()) {
            isDirty_taxLiabilityCalculator_2 = taxLiabilityCalculator_2.hasTaxLiabilityChanged();
        }
        if (guardCheck_taxLiabilityThresholdMonitor_1()) {
            isDirty_taxLiabilityThresholdMonitor_1 = taxLiabilityThresholdMonitor_1.isTaxLiabilityBreached();
        }
        if (guardCheck_taxLiabilityAlerter_0()) {
            taxLiabilityAlerter_0.publishTaxLiabilityWarning();
        }
        //event stack unwind callbacks
        afterEvent();
    }

    public void handleEvent(FoodSaleEvent typedEvent) {
        auditEvent(typedEvent);
        //Default, no filter methods
        isDirty_foodSaleHandler_4 = foodSaleHandler_4.handleSale(typedEvent);
        if (guardCheck_taxLiabilityCalculator_2()) {
            isDirty_taxLiabilityCalculator_2 = taxLiabilityCalculator_2.hasTaxLiabilityChanged();
        }
        if (guardCheck_taxLiabilityThresholdMonitor_1()) {
            isDirty_taxLiabilityThresholdMonitor_1 = taxLiabilityThresholdMonitor_1.isTaxLiabilityBreached();
        }
        if (guardCheck_taxLiabilityAlerter_0()) {
            taxLiabilityAlerter_0.publishTaxLiabilityWarning();
        }
        //event stack unwind callbacks
        afterEvent();
    }

    public void handleEvent(FoodTaxRateEvent typedEvent) {
        auditEvent(typedEvent);
        //Default, no filter methods
        isDirty_foodSaleHandler_4 = foodSaleHandler_4.handleUpdatedTaxRate(typedEvent);
        if (guardCheck_taxLiabilityCalculator_2()) {
            isDirty_taxLiabilityCalculator_2 = taxLiabilityCalculator_2.hasTaxLiabilityChanged();
        }
        if (guardCheck_taxLiabilityThresholdMonitor_1()) {
            isDirty_taxLiabilityThresholdMonitor_1 = taxLiabilityThresholdMonitor_1.isTaxLiabilityBreached();
        }
        if (guardCheck_taxLiabilityAlerter_0()) {
            taxLiabilityAlerter_0.publishTaxLiabilityWarning();
        }
        //event stack unwind callbacks
        afterEvent();
    }

    public void handleEvent(HardwareSaleEvent typedEvent) {
        auditEvent(typedEvent);
        //Default, no filter methods
        isDirty_hardwareSaleHandler_5 = hardwareSaleHandler_5.handleSale(typedEvent);
        if (guardCheck_taxLiabilityCalculator_2()) {
            isDirty_taxLiabilityCalculator_2 = taxLiabilityCalculator_2.hasTaxLiabilityChanged();
        }
        if (guardCheck_taxLiabilityThresholdMonitor_1()) {
            isDirty_taxLiabilityThresholdMonitor_1 = taxLiabilityThresholdMonitor_1.isTaxLiabilityBreached();
        }
        if (guardCheck_taxLiabilityAlerter_0()) {
            taxLiabilityAlerter_0.publishTaxLiabilityWarning();
        }
        //event stack unwind callbacks
        afterEvent();
    }

    public void handleEvent(HardwareTaxRateEvent typedEvent) {
        auditEvent(typedEvent);
        //Default, no filter methods
        isDirty_hardwareSaleHandler_5 = hardwareSaleHandler_5.handleUpdatedTaxRate(typedEvent);
        if (guardCheck_taxLiabilityCalculator_2()) {
            isDirty_taxLiabilityCalculator_2 = taxLiabilityCalculator_2.hasTaxLiabilityChanged();
        }
        if (guardCheck_taxLiabilityThresholdMonitor_1()) {
            isDirty_taxLiabilityThresholdMonitor_1 = taxLiabilityThresholdMonitor_1.isTaxLiabilityBreached();
        }
        if (guardCheck_taxLiabilityAlerter_0()) {
            taxLiabilityAlerter_0.publishTaxLiabilityWarning();
        }
        //event stack unwind callbacks
        afterEvent();
    }

    public void handleEvent(TaxLiabilityNotificationThresholdEvent typedEvent) {
        auditEvent(typedEvent);
        //Default, no filter methods
        isDirty_taxLiabilityThresholdMonitor_1 = taxLiabilityThresholdMonitor_1.thresholdUpdated(typedEvent);
        if (guardCheck_taxLiabilityAlerter_0()) {
            taxLiabilityAlerter_0.publishTaxLiabilityWarning();
        }
        //event stack unwind callbacks
        afterEvent();
    }

    public void handleEvent(TaxPaymentEvent typedEvent) {
        auditEvent(typedEvent);
        //Default, no filter methods
        isDirty_taxLiabilityCalculator_2 = taxLiabilityCalculator_2.taxPayment(typedEvent);
        if (guardCheck_taxLiabilityThresholdMonitor_1()) {
            isDirty_taxLiabilityThresholdMonitor_1 = taxLiabilityThresholdMonitor_1.isTaxLiabilityBreached();
        }
        if (guardCheck_taxLiabilityAlerter_0()) {
            taxLiabilityAlerter_0.publishTaxLiabilityWarning();
        }
        //event stack unwind callbacks
        afterEvent();
    }

    public void bufferEvent(Object event) {
        buffering = true;
        switch (event) {
            case BookSaleEvent typedEvent -> {
                auditEvent(typedEvent);
                isDirty_bookSaleHandler_3 = bookSaleHandler_3.handleSale(typedEvent);
                //event stack unwind callbacks
            }
            case BookTaxRateEvent typedEvent -> {
                auditEvent(typedEvent);
                isDirty_bookSaleHandler_3 = bookSaleHandler_3.handleUpdatedTaxRate(typedEvent);
                //event stack unwind callbacks
            }
            case FoodSaleEvent typedEvent -> {
                auditEvent(typedEvent);
                isDirty_foodSaleHandler_4 = foodSaleHandler_4.handleSale(typedEvent);
                //event stack unwind callbacks
            }
            case FoodTaxRateEvent typedEvent -> {
                auditEvent(typedEvent);
                isDirty_foodSaleHandler_4 = foodSaleHandler_4.handleUpdatedTaxRate(typedEvent);
                //event stack unwind callbacks
            }
            case HardwareSaleEvent typedEvent -> {
                auditEvent(typedEvent);
                isDirty_hardwareSaleHandler_5 = hardwareSaleHandler_5.handleSale(typedEvent);
                //event stack unwind callbacks
            }
            case HardwareTaxRateEvent typedEvent -> {
                auditEvent(typedEvent);
                isDirty_hardwareSaleHandler_5 = hardwareSaleHandler_5.handleUpdatedTaxRate(typedEvent);
                //event stack unwind callbacks
            }
            case TaxLiabilityNotificationThresholdEvent typedEvent -> {
                auditEvent(typedEvent);
                isDirty_taxLiabilityThresholdMonitor_1 = taxLiabilityThresholdMonitor_1.thresholdUpdated(typedEvent);
                //event stack unwind callbacks
            }
            case TaxPaymentEvent typedEvent -> {
                auditEvent(typedEvent);
                isDirty_taxLiabilityCalculator_2 = taxLiabilityCalculator_2.taxPayment(typedEvent);
                //event stack unwind callbacks
            }
            default -> {
            }
        }
    }

    public void triggerCalculation() {
        buffering = false;
        String typedEvent = "No event information - buffered dispatch";
        if (guardCheck_taxLiabilityCalculator_2()) {
            isDirty_taxLiabilityCalculator_2 = taxLiabilityCalculator_2.hasTaxLiabilityChanged();
        }
        if (guardCheck_taxLiabilityThresholdMonitor_1()) {
            isDirty_taxLiabilityThresholdMonitor_1 = taxLiabilityThresholdMonitor_1.isTaxLiabilityBreached();
        }
        if (guardCheck_taxLiabilityAlerter_0()) {
            taxLiabilityAlerter_0.publishTaxLiabilityWarning();
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
        auditor.nodeRegistered(bookSaleHandler_3, "bookSaleHandler_3");
        auditor.nodeRegistered(foodSaleHandler_4, "foodSaleHandler_4");
        auditor.nodeRegistered(hardwareSaleHandler_5, "hardwareSaleHandler_5");
        auditor.nodeRegistered(taxLiabilityAlerter_0, "taxLiabilityAlerter_0");
        auditor.nodeRegistered(taxLiabilityCalculator_2, "taxLiabilityCalculator_2");
        auditor.nodeRegistered(taxLiabilityThresholdMonitor_1, "taxLiabilityThresholdMonitor_1");
        auditor.nodeRegistered(callbackDispatcher, "callbackDispatcher");
        auditor.nodeRegistered(subscriptionManager, "subscriptionManager");
        auditor.nodeRegistered(context, "context");
    }


    private void afterEvent() {
        nodeNameLookup.processingComplete();
        isDirty_bookSaleHandler_3 = false;
        isDirty_foodSaleHandler_4 = false;
        isDirty_hardwareSaleHandler_5 = false;
        isDirty_taxLiabilityCalculator_2 = false;
        isDirty_taxLiabilityThresholdMonitor_1 = false;
    }

    @Override
    public void init() {
        initCalled = true;
        //initialise dirty lookup map
        isDirty("test");

    }

    @Override
    public void start() {
        if (!initCalled) {
            throw new RuntimeException("init() must be called before start()");
        }

    }

    @Override
    public void stop() {
        if (!initCalled) {
            throw new RuntimeException("init() must be called before stop()");
        }

    }

    @Override
    public void tearDown() {
        initCalled = false;
        nodeNameLookup.tearDown();
        subscriptionManager.tearDown();
    }

    @Override
    public void batchPause() {

    }

    @Override
    public void batchEnd() {

    }

    @Override
    public boolean isDirty(Object node) {
        if (dirtyFlagSupplierMap.isEmpty()) {
            dirtyFlagSupplierMap.put(bookSaleHandler_3, () -> isDirty_bookSaleHandler_3);
            dirtyFlagSupplierMap.put(foodSaleHandler_4, () -> isDirty_foodSaleHandler_4);
            dirtyFlagSupplierMap.put(hardwareSaleHandler_5, () -> isDirty_hardwareSaleHandler_5);
            dirtyFlagSupplierMap.put(taxLiabilityCalculator_2, () -> isDirty_taxLiabilityCalculator_2);
            dirtyFlagSupplierMap.put(taxLiabilityThresholdMonitor_1, () -> isDirty_taxLiabilityThresholdMonitor_1);
        }
        return dirtyFlagSupplierMap.getOrDefault(node, StaticEventProcessor.ALWAYS_FALSE).getAsBoolean();
    }

    @Override
    public void setDirty(Object node, boolean dirtyFlag) {
        if (dirtyFlagUpdateMap.isEmpty()) {
            dirtyFlagUpdateMap.put(bookSaleHandler_3, (b) -> isDirty_bookSaleHandler_3 = b);
            dirtyFlagUpdateMap.put(foodSaleHandler_4, (b) -> isDirty_foodSaleHandler_4 = b);
            dirtyFlagUpdateMap.put(hardwareSaleHandler_5, (b) -> isDirty_hardwareSaleHandler_5 = b);
            dirtyFlagUpdateMap.put(taxLiabilityCalculator_2, (b) -> isDirty_taxLiabilityCalculator_2 = b);
            dirtyFlagUpdateMap.put(taxLiabilityThresholdMonitor_1, (b) -> isDirty_taxLiabilityThresholdMonitor_1 = b);
        }
        dirtyFlagUpdateMap.get(node).accept(dirtyFlag);
    }

    private boolean guardCheck_taxLiabilityAlerter_0() {
        return isDirty_taxLiabilityThresholdMonitor_1;
    }

    private boolean guardCheck_taxLiabilityCalculator_2() {
        return isDirty_bookSaleHandler_3 |
                isDirty_foodSaleHandler_4 |
                isDirty_hardwareSaleHandler_5;
    }

    private boolean guardCheck_taxLiabilityThresholdMonitor_1() {
        return isDirty_taxLiabilityCalculator_2;
    }


    @Override
    public <T> T getNodeById(String id) throws NoSuchFieldException {
        return nodeNameLookup.getInstanceById(id);
    }

    @Override
    public <A extends Auditor> A getAuditorById(String id) throws NoSuchFieldException, IllegalAccessException {
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
    public TaxProcessorFluxtion newInstance() {
        return new TaxProcessorFluxtion();
    }

    @Override
    public TaxProcessorFluxtion newInstance(Map<Object, Object> contextMap) {
        return new TaxProcessorFluxtion();
    }

    @Override
    public String getLastAuditLogRecord() {
        try {
            EventLogManager eventLogManager = (EventLogManager) this.getClass().getField(EventLogManager.NODE_NAME).get(this);
            return eventLogManager.lastRecordAsString();
        } catch (Throwable e) {
            return "";
        }
    }
}