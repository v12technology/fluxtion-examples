package com.fluxtion.casestudy.oilsensor.generated;

import com.fluxtion.api.annotations.OnEvent;
import com.fluxtion.extension.declarative.api.log.MsgBuilder;
/**
 * Generated notificationToLogger. 
 *
 * @author Greg Higgins
 */
public class MsgBuilder51 extends MsgBuilder{

    //source operand inputs
    public com.fluxtion.casestudy.oilsensor.generated.GreaterThanDecorator_49 source_GreaterThanDecorator_49_50;

    @OnEvent
    public boolean buildMessage() {
        if(!isGoodToLog())
            return false;
        msgSink.append("critical max exceeded:");
        msgSink.append(((com.fluxtion.casestudy.oilsensor.events.SoilPressure)source_GreaterThanDecorator_49_50.event()).value());
        msgSink.append("");
        msgSink.append('\n');
        return true;
    }
}

