package com.acme.mytrader.strategy;

import com.acme.mytrader.execution.ExecutionServiceImpl;
import com.acme.mytrader.price.PriceListener;
import com.acme.mytrader.price.PriceSourceImpl;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.mockito.Mockito.*;

public class TradingStrategyTest {

    @SneakyThrows
    @Test
    public void testAutoBuyForSuccessfulBuy() {
        ExecutionServiceImpl tradeExecutionService = Mockito.mock(ExecutionServiceImpl.class);
        PriceSourceImpl priceSource = new MockPriceSource("IBM", 25.00);
        ArgumentCaptor<String> securityCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Double> priceCaptor = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Integer> volumeCaptor = ArgumentCaptor.forClass(Integer.class);
        TradingStrategy tradingStrategy = new TradingStrategy(tradeExecutionService, priceSource);
        tradingStrategy.autoBuy("IBM", 50.00, 10);
        verify(tradeExecutionService, times(1))
                .buy(securityCaptor.capture(), priceCaptor.capture(), volumeCaptor.capture());
        Assert.assertEquals(securityCaptor.getValue(), "IBM");
        Assert.assertEquals(priceCaptor.getValue(), 25.00, 2);
        Assert.assertEquals(volumeCaptor.getValue().longValue(), 10);
    }

    @SneakyThrows
    @Test
    public void testAutoBuyForNotSuccessfulBuy() {
        ExecutionServiceImpl tradeExecutionService = Mockito.mock(ExecutionServiceImpl.class);
        PriceSourceImpl priceSource = new MockPriceSource("IBM", 25.00);

        TradingStrategy tradingStrategy = new TradingStrategy(tradeExecutionService, priceSource);
        tradingStrategy.autoBuy("NOIBM", 50.00, 10);
        verifyZeroInteractions(tradeExecutionService);
    }

    private class MockPriceSource extends PriceSourceImpl {

        String security;
        double price;

        MockPriceSource(String security, double price) {
            this.security = security;
            this.price = price;
        }

        private final List<PriceListener> priceListeners = new CopyOnWriteArrayList<>();

        @Override
        public void addPriceListener(PriceListener listener) {
            priceListeners.add(listener);
        }

        @Override
        public void removePriceListener(PriceListener listener) {
            priceListeners.remove(listener);
        }

        @Override
        public void run() {
            priceListeners.forEach(priceListener -> priceListener.priceUpdate(security, price));
        }
    }
}

