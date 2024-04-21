package com.fluxtion.example;

import com.fluxtion.compiler.Fluxtion;
import com.fluxtion.example.imperative.helloworld.BreachNotifier;
import com.fluxtion.example.imperative.helloworld.DataSumCalculator;
import com.fluxtion.example.imperative.helloworld.Event_A;
import com.fluxtion.example.imperative.helloworld.Event_B;

public class Main {

    public static void main(String[] args) {
        var eventProcessor = Fluxtion.interpret(new BreachNotifier());
        eventProcessor.init();

        //use the service api
        DataSumCalculator dataSumCalculator = eventProcessor.getExportedService();
        dataSumCalculator.updateA(new Event_A(34.4));
        dataSumCalculator.updateB(new Event_B(52.1));
        dataSumCalculator.updateA(new Event_A(105));
        dataSumCalculator.updateA(new Event_A(12.4));
    }
}