package biz.hengartner.euroexchange.app.api.conversion;

import biz.hengartner.euroexchange.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ConversionControllerTest extends AbstractConversionTest {

    @Test
    public void convertUSDtoCHFwithEmptyDB() {
        statusService.setReady();

        when().
                get("/api/conversion/USD/CHF/5.50?date=2016-01-21").
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
                get("/api/conversion/USD/CHF/5?date=" + dateString).
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
                get("/api/conversion/USD/CHF/1").
        then().
                statusCode(HttpStatus.OK.value()).
                body(containsString(expectedResult));
    }

    @Test
    public void convertUSDtoCHFnoDateValues2() {
        final LocalDate date = LocalDate.now();
        final String usdRate = "0.5"; // USD/EUR = 1.116, 1EUR=1.12USD, 1USD=0.90EUR
        final String chfRate = "4"; // CHF/EUR = 1.1054 1EUR=1.11CHF, 1CHF=0.90EUR
        addRates(date, usdRate, chfRate);

        String expectedResult = "40.0";

        when().
                get("/api/conversion/USD/CHF/5").
        then().
                statusCode(HttpStatus.OK.value()).
                body(containsString(expectedResult));
    }

}