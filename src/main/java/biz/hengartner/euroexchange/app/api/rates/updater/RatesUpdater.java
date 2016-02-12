package biz.hengartner.euroexchange.app.api.rates.updater;

import biz.hengartner.euroexchange.app.api.rates.Rate;
import biz.hengartner.euroexchange.app.api.rates.RatesService;
import biz.hengartner.euroexchange.app.util.DateHelper;
import biz.hengartner.euroexchange.app.ecb.EurofxRetriever;
import biz.hengartner.euroexchange.app.ecb.model.Cube;
import biz.hengartner.euroexchange.app.ecb.model.CubeWithTime;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Fetch rates from xml-feed and create/update values locally.
 */
@Slf4j
@Setter
public abstract class RatesUpdater {

    @Autowired
    private RatesService ratesService;

    public void update() {
        log.info("Updating rates!");

        if (ratesService == null) {
            throw new IllegalStateException("ratesService is null!");
        }

        try {
            EurofxRetriever retriever = new EurofxRetriever();
            List<CubeWithTime> cubeWithTimeList = loadCubeWithTimeList(retriever);
            for(CubeWithTime cubeWithTime: cubeWithTimeList) {
                updateRatesFor(cubeWithTime);
            }
        } catch (IOException e) {
            log.warn("Failed to update rates!", e);
        }
    }

    protected abstract List<CubeWithTime> loadCubeWithTimeList(EurofxRetriever retriever) throws IOException;

    private void updateRatesFor(CubeWithTime cubeWithTime) {
        LocalDate date = DateHelper.parseIsoDate(cubeWithTime.getTime());
        for(Cube cube : cubeWithTime.getCubes()) {
            Rate rate = new Rate(cube.getCurrency(), new BigDecimal(cube.getRate()), date);
            ratesService.insertOrUpdate(rate);
        }
    }

}