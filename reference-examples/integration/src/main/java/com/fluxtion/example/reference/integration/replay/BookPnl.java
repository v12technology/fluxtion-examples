package com.fluxtion.example.reference.integration.replay;

import com.fluxtion.runtime.annotations.OnEventHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BookPnl {

    private final String bookName;
    private transient int pnl;

    @OnEventHandler(filterVariable = "bookName")
    public boolean pnlUpdate(PnlUpdate pnlUpdate) {
        pnl = pnlUpdate.getAmount();
        return true;
    }
}
