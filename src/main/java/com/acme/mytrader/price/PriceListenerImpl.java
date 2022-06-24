package com.acme.mytrader.price;

import com.acme.mytrader.execution.ExecutionService;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class PriceListenerImpl implements PriceListener {

    private final String security;
    private final double priceForBuying;
    private final int value;
    private final ExecutionService executionService;

    private boolean biddingContinues = true;

    /**
     * The method of acquisition of securities when the necessary conditions are met
     * @param security
     * @param price
     */
    @Override
    public void priceUpdate(String security, double price) {
        if (canBuy(security, price)) {
            executionService.buy(security, price, value);
            biddingContinues = false;
        }
    }

    /**
     * The method of checking the possibility and advisability of buying
     * @param security
     * @param price
     * @return
     */
    private boolean canBuy(String security, double price) {
        return (biddingContinues) && this.security.equals(security) && (price < this.priceForBuying);
    }
}