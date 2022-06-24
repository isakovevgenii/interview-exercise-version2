package com.acme.mytrader.price;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@NoArgsConstructor
@Getter
public class PriceSourceImpl implements PriceSource, Runnable {

    private final List<PriceListener> priceListeners = new CopyOnWriteArrayList<>();

    private static final List<String> SECURITIES = Arrays.asList("IBM");

    private static final double RANGE_MIN = 1.00;
    private static final double RANGE_MAX = 100.00;

    /**
     * Adding securities to the price change simulator
     * @param listener
     */
    @Override
    public void addPriceListener(PriceListener listener) {
        this.priceListeners.add(listener);
    }

    /**
     * Removing securities to the price change simulator
     * @param listener
     */
    @Override
    public void removePriceListener(PriceListener listener) {
        this.priceListeners.remove(listener);
    }

    /**
     * The method of simulating changes in the price of securities
     */
    @Override
    public void run() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            String security = SECURITIES.get(rand.nextInt(SECURITIES.size()));
            double price = RANGE_MIN + (RANGE_MAX - RANGE_MIN) * rand.nextDouble();
            priceListeners.forEach(priceListener -> priceListener.priceUpdate(security, price));
        }
    }
}
