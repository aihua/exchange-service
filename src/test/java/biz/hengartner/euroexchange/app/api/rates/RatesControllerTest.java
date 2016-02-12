package biz.hengartner.euroexchange.app.api.rates;

import biz.hengartner.euroexchange.Application;
import biz.hengartner.euroexchange.app.api.status.StatusService;
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
public class RatesControllerTest {

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
    public void notAvailable() {
        statusService.setReady();

        when().
                get("/api/rates/USD/2016-01-21").
        then().
                statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void notReady() {
        when().
                get("/api/rates/USD/2016-01-21").
                then().
                statusCode(HttpStatus.SERVICE_UNAVAILABLE.value());
    }

    @Test
    public void retrieveByCurrencyAndDate() {
        // given
        Rate rate = new Rate(null, "USD", new BigDecimal("1.21"), LocalDate.now());
        ratesRepository.saveAndFlush(rate);

        statusService.setReady();

        when().
                get("/api/rates/" + rate.getCurrency() + "/" + rate.getDate()).
        then().
                statusCode(HttpStatus.OK.value()).
                body(containsString(rate.getRate().toString()));
    }
}