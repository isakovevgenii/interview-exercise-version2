package com.acme.mytrader.strategy;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListenerImpl;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PriceListenerImplTest {

    @Test
    public void testInitialize() {
        ExecutionService executionService = Mockito.mock(ExecutionService.class);

        PriceListenerImpl buyPriceListener = new PriceListenerImpl(
                "IBM",
                50.00,
                100,
                executionService,
                true);

        Assert.assertEquals(buyPriceListener.getSecurity(), "IBM");
        Assert.assertEquals(buyPriceListener.getPriceForBuying(), 50.00, 2);
        Assert.assertEquals(buyPriceListener.getValue(), 100);
        Assert.assertEquals(buyPriceListener.isBiddingContinues(), true);
    }

    @Test
    public void testBuy() {
        ExecutionService executionService = Mockito.mock(ExecutionService.class);
        ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> acDouble = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Integer> acInteger = ArgumentCaptor.forClass(Integer.class);

        PriceListenerImpl buyPriceListener = new PriceListenerImpl(
                "IBM",
                50.00,
                100,
                executionService,
                true);
        buyPriceListener.priceUpdate("IBM", 25.00);

        verify(executionService, times(1))
                .buy(acString.capture(), acDouble.capture(), acInteger.capture());
        Assert.assertEquals(acString.getValue(), "IBM");
        Assert.assertEquals(acDouble.getValue(), 25.00, 2);
        Assert.assertEquals(acInteger.getValue().longValue(), 100);
        Assert.assertFalse(buyPriceListener.isBiddingContinues());
    }

    @Test
    public void testNotBuy() {
        ExecutionService executionService = Mockito.mock(ExecutionService.class);
        ArgumentCaptor<String> acString = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> acDouble = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Integer> acInteger = ArgumentCaptor.forClass(Integer.class);

        PriceListenerImpl buyPriceListener = new PriceListenerImpl(
                "IBM",
                50.00,
                100,
                executionService,
                true
        );
        buyPriceListener.priceUpdate("IBM", 55.00);

        verify(executionService, times(0))
                .buy(acString.capture(), acDouble.capture(), acInteger.capture());
        Assert.assertTrue(buyPriceListener.isBiddingContinues());
    }
}
