package com.fluxtion.example.cookbook.parallel;

import com.fluxtion.runtime.annotations.OnEventHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class RequestHandler {

    private long startTime;
    @OnEventHandler
    public boolean stringRequest(String in){
        log.debug("request received:{}", in);
        startTime = System.currentTimeMillis();
        return true;
    }
}
