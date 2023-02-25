package com.fluxtion.example.cookbook.subscription;

import com.fluxtion.runtime.event.Event;

public record SharePrice(String symbolId, double price) implements Event {
    public String filterString() {
        return symbolId;
    }
}
