package com.fluxtion.example.cookbook.pnl_flatmap.events;

import com.fluxtion.example.cookbook.pnl_flatmap.refdata.Instrument;

public record TradeLeg(Instrument instrument, double volume) {
}
