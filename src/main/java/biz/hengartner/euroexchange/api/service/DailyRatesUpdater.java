package biz.hengartner.euroexchange.api.service;

import biz.hengartner.euroexchange.ecb.EurofxRetriever;
import biz.hengartner.euroexchange.ecb.model.CubeWithTime;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class DailyRatesUpdater extends RatesUpdater {

    @Override
    protected List<CubeWithTime> loadCubeWithTimeList(EurofxRetriever retriever) throws IOException {
        return Arrays.asList(retriever.fetchDailyCubes());
    }

}