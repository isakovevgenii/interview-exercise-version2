package com.acme.mytrader.strategy;

import com.acme.mytrader.execution.ExecutionServiceImpl;
import com.acme.mytrader.price.PriceListenerImpl;
import com.acme.mytrader.price.PriceSourceImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 */
@AllArgsConstructor
@Builder
@Getter
public class TradingStrategy {

    private final ExecutionServiceImpl executionService;
    private final PriceSourceImpl priceSource;

    /**
     * Method for automatic purchase of securities
     * @param security
     * @param priceForBuying
     * @param volume
     * @throws InterruptedException
     */
    public void autoBuy(String security, double priceForBuying, int volume) throws InterruptedException {

        PriceListenerImpl priceListener = new PriceListenerImpl(
                security,
                priceForBuying,
                volume,
                executionService,
                true
        );

        priceSource.addPriceListener(priceListener);
        Thread thread = new Thread(priceSource);
        thread.start();
        thread.join();
        priceSource.removePriceListener(priceListener);
    }

    /**
     * Test launch of the trader application
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        TradingStrategy tradingStrategy = new TradingStrategy(new ExecutionServiceImpl(1), new PriceSourceImpl());
        tradingStrategy.autoBuy("IBM", 50.00, 1);
    }
}