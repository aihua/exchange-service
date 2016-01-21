package biz.hengartner.euroexchange.ecb.model;

import lombok.Data;

/**
 * <pre>
 * {@code ...
 *   <Cube>
 *       <Cube time='2016-01-21'>
 *           <Cube currency='USD' rate='1.0893'/><!-- <== REPRESENTS THIS CUBE -->
 *   ...
 * } </pre>
 */
@Data
public class Cube {

    private String currency;
    private String rate;

}
