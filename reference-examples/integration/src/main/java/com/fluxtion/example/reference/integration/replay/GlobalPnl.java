package com.fluxtion.example.reference.integration.replay;

import com.fluxtion.runtime.annotations.OnTrigger;
import com.fluxtion.runtime.annotations.Start;
import com.fluxtion.runtime.annotations.builder.Inject;
import com.fluxtion.runtime.output.SinkPublisher;
import com.fluxtion.runtime.time.Clock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GlobalPnl {
    public Clock clock = Clock.DEFAULT_CLOCK;
    private final List<BookPnl> bookPnlList;
    private final transient DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    public GlobalPnl(List<BookPnl> bookPnlList) {
        this.bookPnlList = new ArrayList<>();
        this.bookPnlList.addAll(bookPnlList);
    }

    @Start
    public void start() {
        System.out.println("time,globalPnl");
    }

    @OnTrigger
    public boolean calculate() {
        String time = dateFormat.format(new Date(clock.getProcessTime()));
        int pnl = bookPnlList.stream().mapToInt(BookPnl::getPnl).sum();
        System.out.println(time + "," + pnl);
        return true;
    }
}
