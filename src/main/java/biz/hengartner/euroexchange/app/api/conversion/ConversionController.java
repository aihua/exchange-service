package biz.hengartner.euroexchange.app.api.conversion;

import biz.hengartner.euroexchange.app.api.rates.RateNotFoundException;
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
import java.time.LocalDate;

/**
 * Convert an amount between two currencies.
 */
@Slf4j
@RequestMapping("/api/conversion")
@Controller
public class ConversionController {

    @Autowired
    private ConversionService conversionService;

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

        LocalDate conversionDate = date;
        if (conversionDate == null) {
            conversionDate = LocalDate.now();
        }

        try {
            BigDecimal convertedAmount = conversionService.convert(fromCurrency, toCurrency, amount, conversionDate);
            return new ResponseEntity<>(convertedAmount, HttpStatus.OK);
        } catch (RateNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
