package biz.hengartner.euroexchange.app.api.rates;

import biz.hengartner.euroexchange.app.util.DateHelper;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RateNotFoundException extends Exception {

    public RateNotFoundException(String currency, LocalDate date) {
        super("Rate not found. Currency: " + currency + ", date: " + DateHelper.toIsoFormat(date));
    }

    public RateNotFoundException(String message) {
        super(message);
    }

    public RateNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
