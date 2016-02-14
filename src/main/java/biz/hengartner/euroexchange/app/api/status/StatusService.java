package biz.hengartner.euroexchange.app.api.status;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service status.
 *
 * Query this service to find out if exchange-rates have loaded and are available.
 */
@Slf4j
@Service
public class StatusService {

    private boolean isReady = false;

    public boolean isReady() {
        return isReady;
    }

    public void setReady() {
        isReady = true;
        log.info("status isReady:true");
    }

    public void reset() {
        isReady = false;
        log.info("status isReady:false");
    }

}
