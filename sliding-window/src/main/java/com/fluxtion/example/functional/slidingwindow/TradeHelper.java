package com.fluxtion.example.functional.slidingwindow;

import com.fluxtion.example.functional.slidingwindow.Main.Trade;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class TradeHelper {
    private static ScheduledFuture<?> tradeGeneratorExecutor;

    public static void publishTrades(Consumer<Object> tradeConsumer, int secondsRunLength) {
        long now = System.currentTimeMillis();
        String[] ids = new String[]{"BARX", "ABCD", "APPL", "MSFT", "IBM", "TOCK", "META", "LLVM", "KDER", "PPL"};
        Random random = new Random();
        tradeGeneratorExecutor = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try{
                tradeConsumer.accept(new Object());
                tradeConsumer.accept(
                        new Trade(ids[random.nextInt(ids.length)],
                                (int)(100 * (1 + random.nextDouble()))/100.0,
                                10+ random.nextInt(500),
                                (System.currentTimeMillis() - now)));
            }catch (Exception e){
                System.out.println("error:" + e);
            }
            if ((System.currentTimeMillis() - now) > 1000 * secondsRunLength) {
                stopGenerator();
            }
        }, 500, 300, TimeUnit.MILLISECONDS);
    }

    private static void stopGenerator() {
        tradeGeneratorExecutor.cancel(true);
        System.exit(0);
    }
}
