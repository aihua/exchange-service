package biz.hengartner.euroexchange.app.ecb;

import biz.hengartner.euroexchange.app.ecb.model.CubeWithTime;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Fetches XML from ECB webservice and returns POJOs.
 */
public class EurofxRetriever {

    String URL_DAILY = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    String URL_HIST = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml";

    public CubeWithTime fetchDailyCubes() throws IOException {
        return cubeWithTimesList(URL_DAILY).get(0);
    }

    public List<CubeWithTime> fetchHistoricCubes() throws IOException {
        return cubeWithTimesList(URL_HIST);
    }

    private List<CubeWithTime> cubeWithTimesList(String url) throws IOException {
        URL parserInput = new URL(url);
        EurofxXmlParser parser = new EurofxXmlParser();
        return parser.parseXml(parserInput);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new EurofxRetriever().fetchDailyCubes().getCubes().size());

    }
}
