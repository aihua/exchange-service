package biz.hengartner.euroexchange.api.service;

import biz.hengartner.euroexchange.api.domain.RatesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RatesUpdateScheduler
{
    @Autowired
    private RatesRepository ratesRepository;

    @Scheduled(fixedDelay = 60 * 60 * 1000) // execute once per hour
    public void fetchRates() {
        new HistoricalRatesUpdater().update();
        new DailyRatesUpdater().update();
    }

}