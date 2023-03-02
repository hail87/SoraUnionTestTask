package statystech.aqaframework.DataObjects.ResellerPortal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Reseller {

	@JsonProperty("reseller_name")
	private String resellerName;

	@JsonProperty("reseller_id")
	private int resellerId;
}