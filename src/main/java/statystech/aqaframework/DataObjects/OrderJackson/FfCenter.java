package statystech.aqaframework.DataObjects.OrderJackson;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FfCenter {

	@JsonProperty("ff_center_name")
	private String ffCenterName;

	@JsonProperty("batches")
	private List<BatchesItem> batches;

	@JsonProperty("assigned_qty")
	private String assignedQty;

	@JsonProperty("ff_center_id")
	private String ffCenterId;

	@Override
 	public String toString(){
		return 
			"FfCentersItem{" + 
			"ff_center_name = '" + ffCenterName + '\'' + 
			",batches = '" + batches + '\'' + 
			",assigned_qty = '" + assignedQty + '\'' + 
			",ff_center_id = '" + ffCenterId + '\'' + 
			"}";
		}
}