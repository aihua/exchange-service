package biz.hengartner.euroexchange.ecb;

import biz.hengartner.euroexchange.ecb.model.CubeWithTime;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

/**
 * Fetches XML from ECB webservice and returns POJOs.
 */
@Service
public class EurofxRetrieveService {

    String URL_DAILY = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    String URL_HIST = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml\n";

    public CubeWithTime fetchDailyCubes() throws IOException {
        return cubeWithTimesList(URL_DAILY).get(0);
    }

    public List<CubeWithTime> fetchHistoricCubes() throws IOException {
        return cubeWithTimesList(URL_HIST);
    }

    private List<CubeWithTime> cubeWithTimesList(String url) throws IOException {
        // Doesn't work :-( .. parser fails on the byte-order-mark or something.
        // URL parserInput = new URL(url);

        String parserInput = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();

        EurofxXmlParser parser = new EurofxXmlParser();
        return parser.parseXml(parserInput);
    }
}
