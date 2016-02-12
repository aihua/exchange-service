package biz.hengartner.euroexchange.app.ecb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * <pre>
 * {@code ...
 *   <Cube>  <!-- <== REPRESENTS THIS CUBE -->
 *       <Cube time='2016-01-21'>
 *           <Cube currency='USD' rate='1.0893'/>
 *   ...
 * } </pre>
 */
@Data
public class CubeWrapper {

    @JsonProperty("Cube")
    private List<CubeWithTime> cubeWithTime;

}
