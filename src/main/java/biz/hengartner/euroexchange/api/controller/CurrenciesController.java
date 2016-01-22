package biz.hengartner.euroexchange.api.controller;

import biz.hengartner.euroexchange.api.domain.RatesRepository;
import biz.hengartner.euroexchange.api.service.StatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/api/currencies")
@Controller
public class CurrenciesController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RatesRepository ratesRepository;

    @Autowired
    private StatusService statusService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<String>> currencies() {
        if (!statusService.isReady()) {
            log.warn("service is not ready, will not query database!");
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<>(ratesRepository.currencies(), HttpStatus.OK);
    }

}
