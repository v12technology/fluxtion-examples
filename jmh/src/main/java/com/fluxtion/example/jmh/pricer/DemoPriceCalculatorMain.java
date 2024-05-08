package com.fluxtion.example.jmh.pricer;

import com.fluxtion.example.jmh.pricer.generated.PriceLadderProcessor;
import com.fluxtion.example.jmh.pricer.node.PriceDistributor;

import java.util.Random;

public class DemoPriceCalculatorMain {

    public static void main(String[] args) {
        PriceLadderProcessor priceProcessor = new PriceLadderProcessor();
        priceProcessor.init();

        PriceDistributor priceDistributor = new PriceDistributor();
        priceProcessor.setPriceDistributor(priceDistributor);
        //set some skew and max levels
        priceProcessor.setSkew(10);
        priceProcessor.setLevels(4);


        PriceLadder priceLadderIn = nextRandomPriceLadder();
        System.out.println("Input PriceLadder : " + priceLadderIn);
        priceProcessor.newPriceLadder(priceLadderIn);

        System.out.println("Output PriceLadder: " + priceDistributor.getPriceLadder());

    }

    public static PriceLadder nextRandomPriceLadder() {
        PriceLadder priceLadder = new PriceLadder();
        Random r = new Random();

        int[] bidPrice = priceLadder.getBidPrices();
        bidPrice[0] = 1240 - (r.nextInt(100) + 10);
        bidPrice[1] = bidPrice[0] - (r.nextInt(100) + 10);
        bidPrice[2] = bidPrice[1] - (r.nextInt(100) + 10);
        bidPrice[3] = bidPrice[2] - (r.nextInt(100) + 10);
        bidPrice[4] = bidPrice[3] - (r.nextInt(100) + 10);

        int[] askPrice = priceLadder.getAskPrices();
        askPrice[0] = 1380 + (r.nextInt(100) + 10);
        askPrice[1] = askPrice[0] + (r.nextInt(100) + 10);
        askPrice[2] = askPrice[1] + (r.nextInt(100) + 10);
        askPrice[3] = askPrice[2] + (r.nextInt(100) + 10);
        askPrice[4] = askPrice[3] + (r.nextInt(100) + 10);

        int[] bidSize = priceLadder.getBidSizes();
        bidSize[0] = 100 + r.nextInt(100);
        bidSize[1] = 100 + r.nextInt(100);
        bidSize[2] = 100 + r.nextInt(100);
        bidSize[3] = 100 + r.nextInt(100);
        bidSize[4] = 100 + r.nextInt(100);

        int[] askSize = priceLadder.getAskSizes();
        askSize[0] = 100 + r.nextInt(100);
        askSize[1] = 100 + r.nextInt(100);
        askSize[2] = 100 + r.nextInt(100);
        askSize[3] = 100 + r.nextInt(100);
        askSize[4] = 100 + r.nextInt(100);

        return priceLadder;
    }

    public static PriceLadder[] generateRandomPriceLadders(int count) {
        PriceLadder[] priceLadders = new PriceLadder[count];
        for (int i = 0; i < count; i++) {
            priceLadders[i] = nextRandomPriceLadder();
        }
        return priceLadders;
    }
}
