package biz.hengartner.euroexchange.api.controller;

import biz.hengartner.euroexchange.Application;
import biz.hengartner.euroexchange.api.domain.Rate;
import biz.hengartner.euroexchange.api.domain.RatesRepository;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ConvertControllerTest {

    @Autowired
    private RatesRepository ratesRepository;

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
        ratesRepository.saveAndFlush(fromRate);
        ratesRepository.saveAndFlush(toRate);

        statusService.setReady();

        when().
                get("/api/convert/USD/CHF/10.90?date=" + dateString).
        then().
                statusCode(HttpStatus.OK.value());
    }


    @Test
    public void convertUSDtoCHFnoDate() {
        LocalDate date = LocalDate.now();
        Rate fromRate = new Rate(null, "USD", new BigDecimal("10"), date);
        Rate toRate = new Rate(null, "CHF", new BigDecimal("20"), date);
        ratesRepository.saveAndFlush(fromRate);
        ratesRepository.saveAndFlush(toRate);

        statusService.setReady();

        when().
                get("/api/convert/USD/CHF/10.90").
        then().
                statusCode(HttpStatus.OK.value());
    }


}