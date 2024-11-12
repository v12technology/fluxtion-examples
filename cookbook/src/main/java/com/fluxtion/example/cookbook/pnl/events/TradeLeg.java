package com.fluxtion.example.cookbook.pnl.events;

import com.fluxtion.example.cookbook.pnl.refdata.Instrument;

public record TradeLeg(Instrument instrument, double volume) {
}
