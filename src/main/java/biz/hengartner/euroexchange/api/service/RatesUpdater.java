package biz.hengartner.euroexchange.api.service;

import biz.hengartner.euroexchange.api.domain.Rate;
import biz.hengartner.euroexchange.ecb.EurofxRetriever;
import biz.hengartner.euroexchange.ecb.model.Cube;
import biz.hengartner.euroexchange.ecb.model.CubeWithTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public abstract class RatesUpdater {

    @Autowired
    private RatesService ratesService;

    public void update() {
        EurofxRetriever retriever = new EurofxRetriever();
        try {
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
        LocalDate date = LocalDate.parse(cubeWithTime.getTime(), DateTimeFormatter.ISO_DATE);
        for(Cube cube : cubeWithTime.getCubes()) {
            Rate rate = new Rate(cube.getCurrency(), new BigDecimal(cube.getRate()), date);
            ratesService.insertOrUpdate(rate);
        }
    }
}