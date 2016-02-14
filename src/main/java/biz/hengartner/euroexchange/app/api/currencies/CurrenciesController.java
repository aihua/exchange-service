package biz.hengartner.euroexchange.app.api.currencies;

import biz.hengartner.euroexchange.app.api.rates.RatesService;
import biz.hengartner.euroexchange.app.api.status.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@RequestMapping("/api/currencies")
@Controller
public class CurrenciesController {

    @Autowired
    private RatesService ratesService;

    @Autowired
    private StatusService statusService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<String>> currencies() {
        if (!statusService.isReady()) {
            log.warn("service is not ready, will not query database!");
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }

        return new ResponseEntity<>(ratesService.currencies(), HttpStatus.OK);
    }

}
