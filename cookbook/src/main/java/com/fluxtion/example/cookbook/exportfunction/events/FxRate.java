package com.fluxtion.example.cookbook.exportfunction.events;

import com.fluxtion.runtime.event.DefaultEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FxRate extends DefaultEvent {

    private double rate;

    public FxRate(String filterId, double rate) {
        super(filterId);
        this.rate = rate;
    }
}
