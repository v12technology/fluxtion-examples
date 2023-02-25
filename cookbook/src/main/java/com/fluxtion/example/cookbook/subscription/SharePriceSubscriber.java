package com.fluxtion.example.cookbook.subscription;

import com.fluxtion.runtime.annotations.Initialise;
import com.fluxtion.runtime.annotations.OnEventHandler;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.input.SubscriptionManager;

public class SharePriceSubscriber {

    private final String symbolId;
    @Inject
    public SubscriptionManager subscriptionManager;

    public SharePriceSubscriber(String symbolId) {
        this.symbolId = symbolId;
    }

    @Initialise
    public void init() {
        subscriptionManager.subscribe(symbolId);
    }

    @OnEventHandler(filterVariable = "symbolId")
    public void AssetPrice(SharePrice assetPriceUpdate) {
        System.out.println("subscriber:" + symbolId + " -> " + assetPriceUpdate);
    }
}
