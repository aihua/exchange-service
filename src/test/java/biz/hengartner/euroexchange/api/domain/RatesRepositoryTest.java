package biz.hengartner.euroexchange.api.domain;

import biz.hengartner.euroexchange.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest()
public class RatesRepositoryTest {

    @Autowired
    private RatesRepository ratesRepository;

    @Test
    public void retrieveByCurrencyAndDate() {
        // given
        Rate rate = new Rate(null, "USD", new BigDecimal("1.21"), LocalDate.now());
        ratesRepository.saveAndFlush(rate);

        // when
        Rate retrievedRate = ratesRepository.findByCurrencyAndDate(rate.getCurrency(), rate.getDate());

        // then
        assertThat(retrievedRate, equalTo(rate));
    }

    @Test(expected = JpaSystemException.class)
    public void duplicateCurrencyAndDateShouldFail() {
        // given
        Rate rate = new Rate(null, "USD", new BigDecimal("1.21"), LocalDate.now());
        ratesRepository.saveAndFlush(rate);

        Rate duplicate = new Rate(rate.getCurrency(), new BigDecimal("2.1"), rate.getDate());
        ratesRepository.saveAndFlush(duplicate); // fails
    }
}