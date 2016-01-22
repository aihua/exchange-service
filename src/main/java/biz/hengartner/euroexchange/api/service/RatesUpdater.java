package biz.hengartner.euroexchange.api.service;

import biz.hengartner.euroexchange.api.domain.Rate;
import biz.hengartner.euroexchange.api.domain.RatesRepository;
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
public class RatesUpdater {

    @Autowired
    private RatesRepository ratesRepository;

    public void updateDaily() {
        EurofxRetriever retriever = new EurofxRetriever();
        try {
            CubeWithTime cubeWithTime = retriever.fetchDailyCubes();
            updateRatesFor(cubeWithTime);
        } catch (IOException e) {
            log.warn("Failed to update rates!", e);
        }
    }

    public void updateHistoric() {
        EurofxRetriever retriever = new EurofxRetriever();
        try {
            List<CubeWithTime> cubeWithTimeList = retriever.fetchHistoricCubes();
            for(CubeWithTime cubeWithTime: cubeWithTimeList) {
                updateRatesFor(cubeWithTime);
            }
        } catch (IOException e) {
            log.warn("Failed to update rates!", e);
        }
    }

    private void updateRatesFor(CubeWithTime cubeWithTime) {
        LocalDate date = LocalDate.parse(cubeWithTime.getTime(), DateTimeFormatter.ISO_DATE);
        for(Cube cube : cubeWithTime.getCubes()) {
            Rate rate = new Rate(cube.getCurrency(), new BigDecimal(cube.getRate()), date);
            ratesRepository.save(rate);
        }
    }
}