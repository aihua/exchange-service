package biz.hengartner.euroexchange.app.ecb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties({"Sender","subject"})
@Data
public class Envelope {

    @JsonProperty("Cube")
    CubeWrapper cubeWrapper;
}
