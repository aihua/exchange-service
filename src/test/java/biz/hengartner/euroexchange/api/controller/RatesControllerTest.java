package biz.hengartner.euroexchange.api.controller;

import biz.hengartner.euroexchange.Application;
import biz.hengartner.euroexchange.api.domain.Rate;
import biz.hengartner.euroexchange.api.domain.RatesRepository;
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

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class RatesControllerTest {

    @Autowired
    private RatesRepository ratesRepository;

    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void retrieveByCurrencyAndDate() {
        // given
        Rate rate = new Rate(null, "USD", new BigDecimal("1.21"), "2016-01-01");
        ratesRepository.saveAndFlush(rate);

        when().
                get("/api/rates/" + rate.getCurrency() + "/" + rate.getDate()).
        then().
                statusCode(HttpStatus.OK.value()).
                body(containsString(rate.getRate().toString()));
    }
}