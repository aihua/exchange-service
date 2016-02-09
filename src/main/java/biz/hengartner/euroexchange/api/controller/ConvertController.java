package biz.hengartner.euroexchange.api.controller;

import biz.hengartner.euroexchange.api.domain.Rate;
import biz.hengartner.euroexchange.api.domain.RatesRepository;
import biz.hengartner.euroexchange.api.service.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequestMapping("/api/convert")
@Controller
public class ConvertController {

    @Autowired
    private RatesRepository ratesRepository;

    @Autowired
    private StatusService statusService;

    @RequestMapping(method = RequestMethod.GET, path = "/{fromCurrency}/{toCurrency}/{amount}")
    @ResponseBody
    public ResponseEntity<BigDecimal> convert(
            @PathVariable("fromCurrency") String fromCurrency,
            @PathVariable("toCurrency") String toCurrency,
            @PathVariable("amount") BigDecimal amount,
            // TODO: dateString -> use date type
            @RequestParam(name = "date", required = false) String dateString) {

        log.info("convert rates. fromCurrency: {}, toCurrency: {}, amount: {}, date: {}", fromCurrency, toCurrency, amount, dateString);

        if (!statusService.isReady()) {
            log.warn("service is not ready, will not query database!");
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }

        LocalDate date = parseDateOrNowIfEmpty(dateString);

        Rate fromRate = ratesRepository.findByCurrencyAndDate(fromCurrency, date);
        Rate toRate = ratesRepository.findByCurrencyAndDate(toCurrency, date);

        log.debug("fromRate: {}, toRate: {}", fromRate, toRate);

        if (fromRate == null || toRate == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        BigDecimal convertedAmount = amount.divide(fromRate.getRate(), MathContext.DECIMAL128).multiply(toRate.getRate());

        return new ResponseEntity<>(convertedAmount, HttpStatus.OK);
    }

    private LocalDate parseDateOrNowIfEmpty(@RequestParam(name = "date", required = true) String dateString) {
        LocalDate date = null;
        if (dateString != null) {
            date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
        } else {
            date = LocalDate.now();
        }
        return date;
    }

}
