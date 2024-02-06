package com.fluxtion.example.cookbook.ml.linearregression.api;

import java.util.function.Consumer;

public interface OpportunityNotifier {
    void setNotificationSink(Consumer<PotentialOpportunity> notifierSink);
    void setProfitTrigger(double profitTrigger);
    void setEnableNotifications(boolean enableNotifications);
}
