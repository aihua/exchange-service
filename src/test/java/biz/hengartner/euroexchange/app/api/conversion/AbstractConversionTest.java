package biz.hengartner.euroexchange.app.api.conversion;

import biz.hengartner.euroexchange.app.api.rates.Rate;
import biz.hengartner.euroexchange.app.api.rates.RatesRepository;
import biz.hengartner.euroexchange.app.api.rates.RatesService;
import biz.hengartner.euroexchange.app.api.status.StatusService;
import biz.hengartner.euroexchange.app.util.DateHelper;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AbstractConversionTest {

    @Autowired
    private RatesRepository ratesRepository;

    @Autowired
    private RatesService ratesService;

    @Autowired
    protected ConversionService conversionService;

    @Autowired
    protected StatusService statusService;

    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        ratesRepository.deleteAll();
        statusService.reset();
    }

    protected void addRates(String dateString, String usdRate, String chfRate) {
        addRates(DateHelper.parseIsoDate(dateString), usdRate, chfRate);
    }

    protected void addRates(LocalDate date, String usdRate, String chfRate) {
        Rate fromRate = new Rate(null, "USD", new BigDecimal(usdRate), date);
        Rate toRate = new Rate(null, "CHF", new BigDecimal(chfRate), date);
        ratesService.save(fromRate, toRate);
        statusService.setReady();
    }

}