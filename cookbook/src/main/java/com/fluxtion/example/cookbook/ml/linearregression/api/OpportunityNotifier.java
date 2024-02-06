package com.fluxtion.example.cookbook.ml.linearregression.api;

import java.util.function.Consumer;

public interface OpportunityNotifier {
    default void setNotificationSink(Consumer<Object> notifierSink){}
    default void setProfitTrigger(double profitTrigger){}
    default void setEnableNotifications(boolean enableNotifications){}
}
