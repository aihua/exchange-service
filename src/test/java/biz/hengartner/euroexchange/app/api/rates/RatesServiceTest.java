package biz.hengartner.euroexchange.app.api.rates;

import biz.hengartner.euroexchange.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest()
public class RatesServiceTest {

    @Autowired
    private RatesService ratesService;

    @Autowired
    private RatesRepository ratesRepository;

    @Before
    public void setUp() {
        ratesRepository.deleteAll();
    }

    @Test
    public void createNewRecord() {
        // given
        Rate rate = new Rate(null, "USD", new BigDecimal("1.21"), LocalDate.now());

        // when
        ratesService.insertOrUpdate(rate);

        // then
        assertThat(ratesRepository.count(), equalTo(1L));
    }

    @Test
    public void updateExistingRecord() {
        // given
        Rate rate = new Rate(null, "USD", new BigDecimal("1.21"), LocalDate.now());
        ratesService.insertOrUpdate(rate);

        Rate duplicate = new Rate(rate.getCurrency(), new BigDecimal("2.1"), rate.getDate());

        // when
        ratesService.insertOrUpdate(duplicate);

        // then
        assertThat(ratesRepository.count(), equalTo(1L));
        Rate newRate = ratesRepository.findAll().get(0);
        assertTrue(newRate.getRate().compareTo(duplicate.getRate()) == 0);
    }

    @Test
    public void retrieveByCurrencyAndDate() {
        // given
        Rate rate = new Rate(null, "USD", new BigDecimal("1.21"), LocalDate.now());
        ratesService.save(rate);

        // when
        Rate retrievedRate = ratesService.findByCurrencyAndDate(rate.getCurrency(), rate.getDate());

        // then
        assertThat(retrievedRate, equalTo(rate));
    }


    @Test(expected = RateNotFoundException.class)
    public void shouldNotFindRate() throws RateNotFoundException {
        ratesService.findByCurrencyAndDateOrThrowException("USD", LocalDate.now());
    }

    @Test(expected = JpaSystemException.class)
    public void duplicateCurrencyAndDateShouldFail() {
        Rate rate = new Rate(null, "USD", new BigDecimal("1.21"), LocalDate.now());
        ratesService.save(rate);

        Rate duplicate = new Rate(rate.getCurrency(), rate.getRate(), rate.getDate());
        ratesService.save(duplicate); // fails
    }
}