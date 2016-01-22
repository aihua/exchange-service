package biz.hengartner.euroexchange.api.controller;

import biz.hengartner.euroexchange.api.domain.Rate;
import biz.hengartner.euroexchange.api.domain.RatesRepository;
import biz.hengartner.euroexchange.api.service.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequestMapping("/api/rates")
@Controller
public class RatesController {

    @Autowired
    private RatesRepository ratesRepository;

    @Autowired
    private StatusService statusService;

    @RequestMapping(method = RequestMethod.GET, path = "/{currency}/{date}")
    @ResponseBody
    public ResponseEntity<Rate> rateByCurrencyAndDate(
            @PathVariable("currency") String currency,
            @PathVariable("date") String dateString) {

        log.info("rates by currency: {}, date: {}", currency, dateString);

        if (!statusService.isReady()) {
            log.warn("service is not ready, will not query database!");
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }

        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
        Rate rate = ratesRepository.findByCurrencyAndDate(currency, date);
        if (rate != null) {
            return new ResponseEntity<>(rate, HttpStatus.OK);
        }

        log.warn("unable to find rate for currency: {}, date: {}", currency, dateString);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
