package statystech.aqaframework.DataObjects.OmsDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineItemsItem{

	@JsonProperty("total")
	private double total;

	@JsonProperty("quantity")
	private int quantity;

	@JsonProperty("price")
	private double price;

	@JsonProperty("product_id")
	private int productId;

	@Override
 	public String toString(){
		return 
			"LineItemsItem{" + 
			"total = '" + total + '\'' + 
			",quantity = '" + quantity + '\'' + 
			",price = '" + price + '\'' + 
			",product_id = '" + productId + '\'' + 
			"}";
		}
}