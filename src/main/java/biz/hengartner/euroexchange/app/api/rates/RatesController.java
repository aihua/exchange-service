package biz.hengartner.euroexchange.app.api.rates;

import biz.hengartner.euroexchange.app.api.status.StatusService;
import biz.hengartner.euroexchange.app.util.DateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;

@Slf4j
@RequestMapping("/api/rates")
@Controller
public class RatesController {

    @Autowired
    private RatesService ratesService;

    @Autowired
    private StatusService statusService;

    @RequestMapping(method = RequestMethod.GET, path = "/{currency}/{date}")
    @ResponseBody
    public ResponseEntity<Rate> rateByCurrencyAndDate(
            @PathVariable("currency") String currency,
            @DateTimeFormat(pattern=DateHelper.REQUEST_PARAM_ISO_DATE_FORMAT)
            @PathVariable("date") LocalDate date) {

        log.info("rates by currency: {}, date: {}", currency, date);

        if (!statusService.isReady()) {
            log.warn("service is not ready, will not query database!");
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }

        try {
            Rate rate = ratesService.findByCurrencyAndDateOrThrowException(currency, date);
            return new ResponseEntity<>(rate, HttpStatus.OK);
        } catch (RateNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
   }

}
