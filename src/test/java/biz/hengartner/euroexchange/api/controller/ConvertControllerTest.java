package biz.hengartner.euroexchange.api.controller;

import biz.hengartner.euroexchange.Application;
import biz.hengartner.euroexchange.api.domain.Rate;
import biz.hengartner.euroexchange.api.domain.RatesRepository;
import biz.hengartner.euroexchange.api.service.RatesService;
import biz.hengartner.euroexchange.api.service.StatusService;
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
import java.time.format.DateTimeFormatter;

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
                get("/api/convert/USD/CHF/10.90?date=2016-01-21").
        then().
                statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void convertUSDtoCHFwithDate() {
        String dateString = "2016-01-21";
        LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
        Rate fromRate = new Rate(null, "USD", new BigDecimal("10"), date);
        Rate toRate = new Rate(null, "CHF", new BigDecimal("20"), date);
        ratesService.save(fromRate, toRate);

        statusService.setReady();

        when().
                get("/api/convert/USD/CHF/5?date=" + dateString).
        then().
                statusCode(HttpStatus.OK.value()).
                body(containsString("10.00"));

    }

    @Test
    public void convertUSDtoCHFnoDateValues1() {
        LocalDate date = LocalDate.now();
        Rate fromRate = new Rate(null, "USD", new BigDecimal("1.116"), date); // USD/EUR = 1.116, 1EUR=1.12USD, 1USD=0.90EUR
        Rate toRate = new Rate(null, "CHF", new BigDecimal("1.1054"), date); // CHF/EUR = 1.1054 1EUR=1.11CHF, 1CHF=0.90EUR
        ratesService.save(fromRate, toRate);

        statusService.setReady();

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


}