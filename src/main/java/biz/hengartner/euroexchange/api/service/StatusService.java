package biz.hengartner.euroexchange.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

}
