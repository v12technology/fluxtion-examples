package com.fluxtion.example.cookbook.subscription.imperative;

import com.fluxtion.example.cookbook.subscription.SharePriceEvent;
import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.TearDown;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.input.SubscriptionManager;

public class SharePriceNode {

    private final String symbolId;
    @Inject
    public SubscriptionManager subscriptionManager;

    public SharePriceNode(String symbolId) {
        this.symbolId = symbolId;
    }

    @Initialise
    public void init() {
        subscriptionManager.subscribe(symbolId);
    }

    @TearDown
    public void tearDown(){
        subscriptionManager.unSubscribe(symbolId);
    }

    @OnEventHandler(filterVariable = "symbolId")
    public boolean priceUpdated(SharePriceEvent assetPriceUpdate) {
        System.out.println("SharePriceNode:" + symbolId + " -> " + assetPriceUpdate);
        return true;
    }
}
