package statystech.aqaframework.DataObjects.OmsDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Discounts{

	@JsonProperty("discount_total")
	private int discountTotal;

	@Override
 	public String toString(){
		return 
			"Discounts{" + 
			"discount_total = '" + discountTotal + '\'' + 
			"}";
		}
}