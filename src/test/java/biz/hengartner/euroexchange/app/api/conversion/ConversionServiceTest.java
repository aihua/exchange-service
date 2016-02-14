package biz.hengartner.euroexchange.app.api.conversion;

import biz.hengartner.euroexchange.Application;
import biz.hengartner.euroexchange.app.api.rates.RateNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class ConversionServiceTest extends AbstractConversionTest {

    @Test
    public void shouldConvert() throws RateNotFoundException {
        // given
        final LocalDate date = LocalDate.now();
        final String usdRate = "1.116"; // USD/EUR = 1.116, 1EUR=1.12USD, 1USD=0.90EUR
        final String chfRate = "1.1054"; // CHF/EUR = 1.1054 1EUR=1.11CHF, 1CHF=0.90EUR
        addRates(date, usdRate, chfRate);

        // when
        BigDecimal result = conversionService.convert("USD", "CHF", BigDecimal.ONE, date);

        // then
        BigDecimal expected = new BigDecimal("0.99");
        assertThat(result, is(closeTo(expected, new BigDecimal("0.01"))));
    }

    @Test(expected = RateNotFoundException.class)
    public void shouldNotFindFirstCurrency() throws RateNotFoundException {
        final LocalDate date = LocalDate.now();
        final String usdRate = "1.1";
        final String chfRate = "1.2";
        addRates(date, usdRate, chfRate);

        conversionService.convert("A", "CHF", BigDecimal.ONE, date);
    }

    @Test(expected = RateNotFoundException.class)
    public void shouldNotFindSecondCurrency() throws RateNotFoundException {
        final LocalDate date = LocalDate.now();
        final String usdRate = "1.1";
        final String chfRate = "1.2";
        addRates(date, usdRate, chfRate);

        conversionService.convert("USD", "B", BigDecimal.ONE, date);
    }

    @Test(expected = RateNotFoundException.class)
    public void shouldNotFindCurrencyForDate() throws RateNotFoundException {
        final LocalDate date = LocalDate.now();
        final String usdRate = "1.1";
        final String chfRate = "1.2";
        addRates(date, usdRate, chfRate);

        conversionService.convert("USD", "CHF", BigDecimal.ONE, date.minusDays(1));
    }

}