package com.fluxtion.example.cookbook.lottery.auditor;

import com.fluxtion.runtime.audit.LogRecord;
import com.fluxtion.runtime.audit.LogRecordListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FluxtionSlf4jAuditor implements LogRecordListener {
    @Override
    public void processLogRecord(LogRecord logRecord) {
        log.info(logRecord.toString());
    }
}
