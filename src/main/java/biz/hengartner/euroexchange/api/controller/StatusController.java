package biz.hengartner.euroexchange.api.controller;

import biz.hengartner.euroexchange.api.domain.RatesRepository;
import biz.hengartner.euroexchange.api.service.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/api/status")
@Controller
public class StatusController {

    @Autowired
    private RatesRepository ratesRepository;

    @Autowired
    private StatusService statusService;


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> status() {
        Map<String, Boolean> response = new HashMap<>();
        response.put("isReady", statusService.isReady());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
