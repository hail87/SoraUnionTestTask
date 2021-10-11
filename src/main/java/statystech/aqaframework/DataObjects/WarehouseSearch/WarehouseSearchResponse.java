package statystech.aqaframework.DataObjects.WarehouseSearch;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseSearchResponse {

	@JsonProperty("orders")
	private List<OrdersItem> orders;

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"orders = '" + orders + '\'' + 
			"}";
		}
}