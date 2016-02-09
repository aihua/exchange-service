package biz.hengartner.euroexchange.api.controller;

import biz.hengartner.euroexchange.Application;
import biz.hengartner.euroexchange.api.domain.Rate;
import biz.hengartner.euroexchange.api.domain.RatesRepository;
import biz.hengartner.euroexchange.api.service.RatesService;
import biz.hengartner.euroexchange.api.service.StatusService;
import biz.hengartner.euroexchange.api.util.DateHelper;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ConvertControllerTest {

    @Autowired
    private RatesRepository ratesRepository;

    @Autowired
    private RatesService ratesService;

    @Autowired
    private StatusService statusService;

    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        ratesRepository.deleteAll();
        statusService.reset();
    }

    @Test
    public void convertUSDtoCHFwithEmptyDB() {
        statusService.setReady();

        when().
                get("/api/convert/USD/CHF/5.50?date=2016-01-21").
        then().
                statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void convertUSDtoCHFwithDate() {
        String dateString = "2016-01-21";
        String usdRate = "10";
        String chfRate = "20";
        addRates(dateString, usdRate, chfRate);

        when().
                get("/api/convert/USD/CHF/5?date=" + dateString).
        then().
                statusCode(HttpStatus.OK.value()).
                body(containsString("10.00"));

    }

    @Test
    public void convertUSDtoCHFnoDateValues1() {
        final LocalDate date = LocalDate.now();
        final String usdRate = "1.116"; // USD/EUR = 1.116, 1EUR=1.12USD, 1USD=0.90EUR
        final String chfRate = "1.1054"; // CHF/EUR = 1.1054 1EUR=1.11CHF, 1CHF=0.90EUR
        addRates(date, usdRate, chfRate);

        String expectedResult = "0.99"; // CHF/USD=0.98991, 1USD=0.99CHF, 1CHF=1.01USD

        when().
                get("/api/convert/USD/CHF/1").
        then().
                statusCode(HttpStatus.OK.value()).
                body(containsString(expectedResult));
    }

    @Test
    public void convertUSDtoCHFnoDateValues2() {
        LocalDate date = LocalDate.now();
        Rate fromRate = new Rate(null, "USD", new BigDecimal("0.5"), date);
        Rate toRate = new Rate(null, "CHF", new BigDecimal("4"), date);
        ratesService.save(fromRate, toRate);

        statusService.setReady();

        String expectedResult = "40.0";

        when().
                get("/api/convert/USD/CHF/5").
        then().
                statusCode(HttpStatus.OK.value()).
                body(containsString(expectedResult));
    }

    private void addRates(String dateString, String usdRate, String chfRate) {
        addRates(DateHelper.parseIsoDate(dateString), usdRate, chfRate);
    }

    private void addRates(LocalDate date, String usdRate, String chfRate) {
        Rate fromRate = new Rate(null, "USD", new BigDecimal(usdRate), date);
        Rate toRate = new Rate(null, "CHF", new BigDecimal(chfRate), date);
        ratesService.save(fromRate, toRate);
        statusService.setReady();
    }

}