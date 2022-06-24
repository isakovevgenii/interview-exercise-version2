package com.acme.mytrader.execution;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExecutionServiceImpl implements ExecutionService {

    private final int id;

    /**
     * Method of issuing securities purchase
     * @param security
     * @param price
     * @param volume
     */
    @Override
    public void buy(String security, double price, int volume) {
        System.out.printf("\n Bought %d %s securities at a price of %.2f", volume, security, price);
    }

    /**
     * Method of issuing securities sale
     * @param security
     * @param price
     * @param volume
     */
    @Override
    public void sell(String security, double price, int volume) {
        System.out.printf("\n Sell %d %s securities at a price of %.2f", volume, security, price);
    }
}