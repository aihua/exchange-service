package biz.hengartner.euroexchange.api.service;

import biz.hengartner.euroexchange.api.domain.Rate;
import biz.hengartner.euroexchange.api.domain.RatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RatesService {

    @Autowired
    private RatesRepository ratesRepository;

    public Rate insertOrUpdate(Rate rate) {
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

}
