package com.fluxtion.example.cookbook.ml.linearregression.pipeline;

import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSalesDetailsPostProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface HouseFilters {

    Logger log = LoggerFactory.getLogger(HouseFilters.class);
    static boolean bedroomWithinRange(HouseSalesDetailsPostProcess houseSalesDetailsPostProcess) {
        if (houseSalesDetailsPostProcess.bedrooms() > 1 & houseSalesDetailsPostProcess.bedrooms() < 8) {
            return true;
        }
        log.info("bedroom fail ignoring {}",houseSalesDetailsPostProcess);
        return false;
    }

    static boolean correctLocation(HouseSalesDetailsPostProcess houseSalesDetailsPostProcess) {
        if (houseSalesDetailsPostProcess.locationCategory() > 0) {
            return true;
        }
        log.info("location fail ignoring {}",houseSalesDetailsPostProcess);
        return false;
    }
}
