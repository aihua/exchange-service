package biz.hengartner.euroexchange.app.ecb;

import biz.hengartner.euroexchange.app.ecb.model.CubeWithTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(JUnit4.class)
public class ParserTest {

    private static final String XML_DAILY = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<gesmes:Envelope xmlns:gesmes=\"http://www.gesmes.org/xml/2002-08-01\" xmlns=\"http://www.ecb.int/vocabulary/2002-08-01/eurofxref\">\n" +
            "\t<gesmes:subject>Reference rates</gesmes:subject>\n" +
            "\t<gesmes:Sender>\n" +
            "\t\t<gesmes:name>European Central Bank</gesmes:name>\n" +
            "\t</gesmes:Sender>\n" +
            "\t<Cube>\n" +
            "\t\t<Cube time='2016-01-19'>\n" +
            "\t\t\t<Cube currency='USD' rate='1.0868'/>\n" +
            "\t\t\t<Cube currency='JPY' rate='128.12'/>\n" +
            "\t\t\t<Cube currency='BGN' rate='1.9558'/>\n" +
            "\t\t\t<Cube currency='CZK' rate='27.023'/>\n" +
            "\t\t\t<Cube currency='DKK' rate='7.4628'/>\n" +
            "\t\t\t<Cube currency='GBP' rate='0.76473'/>\n" +
            "\t\t\t<Cube currency='HUF' rate='314.56'/>\n" +
            "\t\t\t<Cube currency='PLN' rate='4.4384'/>\n" +
            "\t\t\t<Cube currency='RON' rate='4.5305'/>\n" +
            "\t\t\t<Cube currency='SEK' rate='9.3231'/>\n" +
            "\t\t\t<Cube currency='CHF' rate='1.0922'/>\n" +
            "\t\t\t<Cube currency='NOK' rate='9.6209'/>\n" +
            "\t\t\t<Cube currency='HRK' rate='7.6555'/>\n" +
            "\t\t\t<Cube currency='RUB' rate='85.2445'/>\n" +
            "\t\t\t<Cube currency='TRY' rate='3.3030'/>\n" +
            "\t\t\t<Cube currency='AUD' rate='1.5673'/>\n" +
            "\t\t\t<Cube currency='BRL' rate='4.3758'/>\n" +
            "\t\t\t<Cube currency='CAD' rate='1.5750'/>\n" +
            "\t\t\t<Cube currency='CNY' rate='7.1496'/>\n" +
            "\t\t\t<Cube currency='HKD' rate='8.4841'/>\n" +
            "\t\t\t<Cube currency='IDR' rate='15009.55'/>\n" +
            "\t\t\t<Cube currency='ILS' rate='4.3025'/>\n" +
            "\t\t\t<Cube currency='INR' rate='73.4840'/>\n" +
            "\t\t\t<Cube currency='KRW' rate='1306.89'/>\n" +
            "\t\t\t<Cube currency='MXN' rate='19.6912'/>\n" +
            "\t\t\t<Cube currency='MYR' rate='4.7530'/>\n" +
            "\t\t\t<Cube currency='NZD' rate='1.6763'/>\n" +
            "\t\t\t<Cube currency='PHP' rate='51.740'/>\n" +
            "\t\t\t<Cube currency='SGD' rate='1.5592'/>\n" +
            "\t\t\t<Cube currency='THB' rate='39.428'/>\n" +
            "\t\t\t<Cube currency='ZAR' rate='18.1115'/>\n" +
            "\t\t</Cube>\n" +
            "\t</Cube>\n" +
            "</gesmes:Envelope>";

    public static final int NUMBER_OF_CURRENCIES = 31;

    @Test
    public void verifyDailyXml() throws IOException {
        // given
        EurofxXmlParser parser = new EurofxXmlParser();

        // when
        List<CubeWithTime> cubeWithTimeList = parser.parseXml(XML_DAILY);

        // then
        assertThat(cubeWithTimeList.size(), equalTo(1));
        assertThat(cubeWithTimeList.get(0).getCubes().size(), equalTo(NUMBER_OF_CURRENCIES));
    }

    @Test
    public void verify90DayXml() throws IOException {
        // given
        EurofxXmlParser parser = new EurofxXmlParser();

        // when
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("ecb-api-output/eurofxref-hist-90d.xml").getFile());
        List<CubeWithTime> cubeWithTimeList = parser.parseXml(file);

        // then
        assertThat(cubeWithTimeList.size(), equalTo(62));
        assertThat(cubeWithTimeList.get(0).getCubes().size(), equalTo(NUMBER_OF_CURRENCIES));
    }
}