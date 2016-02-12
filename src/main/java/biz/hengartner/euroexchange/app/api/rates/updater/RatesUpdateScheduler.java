package biz.hengartner.euroexchange.app.api.rates.updater;

import biz.hengartner.euroexchange.app.api.rates.RatesService;
import biz.hengartner.euroexchange.app.api.status.StatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@ConditionalOnProperty("biz.hengartner.euroexchange.rates.scheduler")
@Component
public class RatesUpdateScheduler {

    @Autowired
    private StatusService statusService;

    @Autowired
    private RatesService ratesService;

    @Autowired
    private HistoricalRatesUpdater historicalRatesUpdater;

    @Autowired
    private DailyRatesUpdater dailyRatesUpdater;

    @Scheduled(fixedDelay = 60 * 60 * 1000) // execute once per hour
    public void fetchRates() {
        dailyRatesUpdater.update();
        historicalRatesUpdater.update();
        statusService.setReady();
    }

}
