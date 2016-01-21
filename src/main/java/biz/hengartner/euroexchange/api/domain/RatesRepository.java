package biz.hengartner.euroexchange.api.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

@Transactional
public interface RatesRepository extends JpaRepository<Rate, Long> {

    Rate findByCurrencyAndDate(@Param("currency") String currency, @Param("date") String date);

}
