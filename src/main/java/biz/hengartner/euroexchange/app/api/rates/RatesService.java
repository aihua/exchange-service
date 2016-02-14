package biz.hengartner.euroexchange.app.api.rates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class RatesService {

    @Autowired
    private RatesRepository ratesRepository;

    public Rate insertOrUpdate(Rate rate) {
        Assert.notNull(rate, "Rate must not be null");
        Rate existing = ratesRepository.findByCurrencyAndDate(rate.getCurrency(), rate.getDate());

        if (existing != null) {
            existing.setRate(rate.getRate());
            return ratesRepository.save(existing);
        }

        return ratesRepository.save(rate);
    }

    public List<Rate> save(Rate... rates) {
        return ratesRepository.save(Arrays.asList(rates));
    }

    public Rate findByCurrencyAndDate(String currency, LocalDate date) {
        Assert.notNull(currency, "Currency must not be null");
        Assert.notNull(date, "Date must not be null");
        return ratesRepository.findByCurrencyAndDate(currency, date);
    }

    public List<String> currencies() {
        return ratesRepository.currencies();
    }

}
