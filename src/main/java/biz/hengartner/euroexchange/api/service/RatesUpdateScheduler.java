package biz.hengartner.euroexchange.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RatesUpdateScheduler
{

    @Scheduled(fixedDelay = 60 * 60 * 1000) // execute once per hour
    public void fetchRates() {
        new HistoricalRatesUpdater().update();
        new DailyRatesUpdater().update();
    }

}