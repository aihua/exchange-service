package biz.hengartner.euroexchange.app.api.conversion;

import biz.hengartner.euroexchange.app.api.rates.Rate;
import biz.hengartner.euroexchange.app.api.rates.RatesService;
import biz.hengartner.euroexchange.app.api.status.StatusService;
import biz.hengartner.euroexchange.app.util.DateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;

@Slf4j
@RequestMapping("/api/conversion")
@Controller
public class ConversionController {

    @Autowired
    private RatesService ratesService;

    @Autowired
    private StatusService statusService;

    @RequestMapping(method = RequestMethod.GET, path = "/{fromCurrency}/{toCurrency}/{amount}")
    @ResponseBody
    public ResponseEntity<BigDecimal> convert(
            @PathVariable("fromCurrency") String fromCurrency,
            @PathVariable("toCurrency") String toCurrency,
            @PathVariable("amount") BigDecimal amount,
            @DateTimeFormat(pattern= DateHelper.REQUEST_PARAM_ISO_DATE_FORMAT)
            @RequestParam(name = "date", required = false) LocalDate date) {

        log.info("convert rates. fromCurrency: {}, toCurrency: {}, amount: {}, date: {}", fromCurrency, toCurrency, amount, date);

        if (!statusService.isReady()) {
            log.warn("service is not ready, will not query database!");
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }

        if (date == null) {
            date = LocalDate.now();
        }

        Rate fromRate = ratesService.findByCurrencyAndDate(fromCurrency, date);
        Rate toRate = ratesService.findByCurrencyAndDate(toCurrency, date);

        log.debug("fromRate: {}, toRate: {}", fromRate, toRate);

        if (fromRate == null || toRate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BigDecimal convertedAmount = amount.divide(fromRate.getRate(), MathContext.DECIMAL128).multiply(toRate.getRate());

        return new ResponseEntity<>(convertedAmount, HttpStatus.OK);
    }

}