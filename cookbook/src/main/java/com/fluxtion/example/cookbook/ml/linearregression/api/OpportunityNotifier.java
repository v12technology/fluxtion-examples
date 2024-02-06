package com.fluxtion.example.cookbook.ml.linearregression.api;

import java.util.function.Consumer;

public interface OpportunityNotifier {

    void publishOn();
    void publishOff();
    default void setNotificationSink(Consumer<Object> notifierSink){}
}
