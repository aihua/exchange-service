package biz.hengartner.euroexchange.app.api.conversion;

import biz.hengartner.euroexchange.app.api.rates.Rate;
import biz.hengartner.euroexchange.app.api.rates.RateNotFoundException;
import biz.hengartner.euroexchange.app.api.rates.RatesService;
import biz.hengartner.euroexchange.app.api.status.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;

@Slf4j
@Service("apiConversionService")
public class ConversionService {

    @Autowired
    private RatesService ratesService;

    @Autowired
    private StatusService statusService;

    /**
     * Convert amount from a currency to another currency.
     */
    public BigDecimal convert(
            String fromCurrency,
            String toCurrency,
            BigDecimal amount,
            LocalDate date) throws RateNotFoundException {
        Assert.notNull(fromCurrency, "FromCurrency must not be null");
        Assert.notNull(toCurrency, "ToCurrency must not be null");
        Assert.notNull(amount, "Amount must not be null");
        Assert.notNull(date, "Date must not be null");

        log.info("convert rates. fromCurrency: {}, toCurrency: {}, amount: {}, date: {}", fromCurrency, toCurrency, amount, date);

        Rate fromRate = ratesService.findByCurrencyAndDateOrThrowException(fromCurrency, date);
        Rate toRate = ratesService.findByCurrencyAndDateOrThrowException(toCurrency, date);

        log.debug("fromRate: {}, toRate: {}", fromRate, toRate);

        return convertAmount(amount, fromRate, toRate);
    }

    private BigDecimal convertAmount(BigDecimal amount, Rate fromRate, Rate toRate) {
        return convertAmount(amount, fromRate.getRate(), toRate.getRate());
    }

    BigDecimal convertAmount(BigDecimal amount, BigDecimal fromRate, BigDecimal toRate) {
        return amount.divide(fromRate, MathContext.DECIMAL128).multiply(toRate);
    }

}
