package biz.hengartner.euroexchange.app.ecb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * <pre>
 * {@code ...
 *   <Cube>
 *       <Cube time='2016-01-21'> <!-- <== REPRESENTS THIS CUBE -->
 *           <Cube currency='USD' rate='1.0893'/>
 *   ...
 * } </pre>
 */
@Data
public class CubeWithTime {

    private String time;

    @JsonProperty("Cube")
    private List<Cube> cubes;

}
