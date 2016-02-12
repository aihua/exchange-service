package biz.hengartner.euroexchange.app.api.rates.updater;

import biz.hengartner.euroexchange.app.ecb.EurofxRetriever;
import biz.hengartner.euroexchange.app.ecb.model.CubeWithTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Fetch & store todays rates.
 */
@Slf4j
@Component
public class DailyRatesUpdater extends RatesUpdater {

    @Override
    protected List<CubeWithTime> loadCubeWithTimeList(EurofxRetriever retriever) throws IOException {
        return Arrays.asList(retriever.fetchDailyCubes());
    }

}