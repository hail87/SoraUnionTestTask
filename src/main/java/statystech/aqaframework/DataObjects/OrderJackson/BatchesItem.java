package statystech.aqaframework.DataObjects.OrderJackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BatchesItem{

	@JsonProperty("number")
	private String number;

	@JsonProperty("qty")
	private String qty;

	@JsonProperty("ff_center_id")
	private String ffCenterId;

	@Override
 	public String toString(){
		return 
			"BatchesItem{" + 
			"number = '" + number + '\'' + 
			",qty = '" + qty + '\'' + 
			",ff_center_id = '" + ffCenterId + '\'' + 
			"}";
		}
}