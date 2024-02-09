package com.fluxtion.example.cookbook.ml.linearregression.pipeline;

import com.fluxtion.example.cookbook.ml.linearregression.api.HouseSaleDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface HousePipelineFunctions {

    Logger log = LoggerFactory.getLogger(HousePipelineFunctions.class);

    static boolean bedroomWithinRangeFilter(HouseSaleDetails houseSalesDetailsPostProcess) {
        if (houseSalesDetailsPostProcess.getBedrooms() > 1 & houseSalesDetailsPostProcess.getBedrooms() < 8) {
            return true;
        }
        log.info("bedroom fail ignoring {}",houseSalesDetailsPostProcess);
        return false;
    }

    static boolean correctLocationFilter(HouseSaleDetails houseSalesDetailsPostProcess) {
        char c = houseSalesDetailsPostProcess.getLocationZip().toCharArray()[0];
        if (c == 'A' | c == 'B' | c == 'C' | c == 'a' | c == 'b' | c == 'b') {
            return true;
        }
        log.info("location fail ignoring {}",houseSalesDetailsPostProcess);
        return false;
    }

    static double squared(double in){
        return in * in;
    }

    static void logIncomingRecord(HouseSaleDetails houseSaleDetails){
        log.info("new sale:{}", houseSaleDetails);
    }

    static void logValidRecord(HouseSaleDetails houseSalesDetailsPostProcess){
        log.info("validated:{}", houseSalesDetailsPostProcess);
    }
}
